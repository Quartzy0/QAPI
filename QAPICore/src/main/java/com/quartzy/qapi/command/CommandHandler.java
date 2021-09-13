package com.quartzy.qapi.command;

import com.quartzy.qapi.QAPI;
import com.quartzy.qapi.StringUtil;
import com.quartzy.qapi.command.converter.MaterialToItemStack;
import com.quartzy.qapi.command.converter.MaterialToXMaterial;
import com.quartzy.qapi.command.converter.TypeConverter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class CommandHandler{
    private static final Logger log = Bukkit.getLogger();
    
    private static final HashMap<String, CommandData> commands = new HashMap<>();
    private static final HashMap<Class<?>, TypeConverter<?, ?>> typeConverters = new HashMap<>();
    
    public static <T, S> void addTypeConverter(TypeConverter<T, S> converter){
        typeConverters.put(converter.getTypeOut(), converter);
    }
    
    static {
        addTypeConverter(new MaterialToItemStack());
    
        addTypeConverter(new MaterialToXMaterial());
    }
    
    public static <T extends Command> T addCommand(Class<T> commandClass){
        if(!commandClass.isAnnotationPresent(CommandExecutor.class)) return null;
        CommandExecutor commandExecutorA = commandClass.getAnnotation(CommandExecutor.class);
        T commandInstance = null;
        try{
            Constructor<T> constructor = commandClass.getConstructor();
            commandInstance = constructor.newInstance();
        } catch(NoSuchMethodException e){
            log.severe(() -> {
                if(commandClass.getEnclosingClass() != null && !Modifier.isStatic(commandClass.getModifiers())){
                    return "Class " + commandClass.getName() + " does not have an empty constructor! (An inner class must be static in order to not require an instance of its parent class)";
                } else{
                    return "Class " + commandClass.getName() + " does not have an empty constructor!";
                }
            });
        } catch(InstantiationException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        }
        if(commandInstance == null) return null;
        
        String fullPath = commandExecutorA.value();
        int beginIndex = fullPath.lastIndexOf('.');
        String lastNode = fullPath;
        String pathRest = "";
        if(beginIndex != -1){
            lastNode = fullPath.substring(beginIndex + 1);
            pathRest = fullPath.substring(0, beginIndex);
        }
        LiteralNode masterNode = new LiteralNode(lastNode);
        Method[] declaredMethods = commandClass.getDeclaredMethods();
        
        methodLoop:
        for(Method declaredMethod : declaredMethods){
            if(declaredMethod.isAnnotationPresent(ArgumentExecutor.class)){
                ArgumentExecutor annotation = declaredMethod.getAnnotation(ArgumentExecutor.class);
                String path = annotation.value();
                String[] pathNames = path.split("\\.");
                Node<?> currentNode = masterNode;
                Parameter[] parameters = declaredMethod.getParameters();
                List<Argument> arguments = new ArrayList<>();
                List<WeakReference<Node<?>>> defaultNodes = new ArrayList<>();
                int defaultIndex = -1;
                int senderIndex = -1;
                boolean senderOnlyPlayer = false;
                StringBuilder methodPath = new StringBuilder(pathRest).append(currentNode.name);
                
                for(int i1 = 0; i1 < parameters.length; i1++){
                    if(parameters[i1].isAnnotationPresent(Sender.class)){
                        senderIndex = i1;
                        senderOnlyPlayer = Player.class.isAssignableFrom(parameters[i1].getType());
                        break;
                    }
                }
                pathLoop:
                for(int i1 = 0; i1 < pathNames.length; i1++){
                    Argument parameterByName = findParameterByName(pathNames[i1], parameters);
                    for(Object child : currentNode.getChildren()){
                        Node<?> node = (Node<?>) child;
                        if(node.getName().equals(pathNames[i1])){
                            currentNode = node;
                            if(parameterByName != null){
                                arguments.add(parameterByName);
                            }
                            methodPath.append('.').append(currentNode.name);
                            continue pathLoop;
                        }
                    }
                    Node<?> newNode;
                    if(parameterByName != null){
                        arguments.add(parameterByName);
                        newNode = new ArgumentNode(pathNames[i1]);
                        ArgumentType type = parameterByName.type();
                        ((ArgumentNode) newNode).setType(type);
                        if(type.minVersion != null && QAPI.version().lower(type.minVersion)){
                            log.severe("Parameter type " + type.name() + " is only accessible in versions " + type.minVersion.name + " and higher (method " + declaredMethod.getName() + " in class " + commandClass.getName() + "). Skipping method.");
                            continue methodLoop;
                        }
                        switch(type){
                            case INTEGER:
                                ((ArgumentNode) newNode).setMaxI(parameterByName.max() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) parameterByName.max());
                                ((ArgumentNode) newNode).setMinI(parameterByName.min() < Integer.MIN_VALUE ? Integer.MIN_VALUE : (int) parameterByName.min());
                                break;
                            case LONG:
                                ((ArgumentNode) newNode).setMaxL(parameterByName.max());
                                ((ArgumentNode) newNode).setMinL(parameterByName.min());
                                break;
                            case FLOAT:
                                ((ArgumentNode) newNode).setMaxF(parameterByName.maxD() > Float.MAX_VALUE ? Float.MAX_VALUE : (int) parameterByName.maxD());
                                ((ArgumentNode) newNode).setMinF(parameterByName.minD() > -Float.MAX_VALUE ? -Float.MAX_VALUE : (int) parameterByName.minD());
                                break;
                            case DOUBLE:
                                ((ArgumentNode) newNode).setMaxD(parameterByName.maxD());
                                ((ArgumentNode) newNode).setMinD(parameterByName.minD());
                                break;
                            case ANGLE:
                                ((ArgumentNode) newNode).setMaxF(180);
                                ((ArgumentNode) newNode).setMinF(-180);
                                break;
                        }
                        if(!parameterByName.defaultB().equals(BoolUnset.UNSET) || parameterByName.defaultD() != Double.MIN_VALUE || parameterByName.defaultF() != Float.MIN_VALUE || parameterByName.defaultI() != Integer.MIN_VALUE || parameterByName.defaultL() != Long.MIN_VALUE){
                            defaultNodes.add(new WeakReference<>(newNode));
                            if(defaultIndex == -1){
                                defaultIndex = i1;
                                defaultNodes.add(new WeakReference<>(currentNode));
                            }
                        } else if(defaultIndex != -1){
                            log.severe("All values after the default value must also have a default value (method " + declaredMethod.getName() + " in class " + commandClass.getName() + "). This method will be skipped.");
                            continue methodLoop;
                        }
                    } else{
                        newNode = new LiteralNode(pathNames[i1]);
                    }
                    currentNode.addChild(newNode);
                    currentNode = newNode;
                    methodPath.append('.').append(currentNode.name);
                }
                
                Predicate<CommandSenderInfo> requirement = commandSenderInfo -> commandSenderInfo.hasPermission(annotation.permissionLevel(), annotation.permission());
                
                for(int j = 0;j<defaultNodes.size();j++){
                    Node<?> node = defaultNodes.get(j).get();
                    if(node == null) continue;
                    String s = methodPath.toString();
                    int i = StringUtil.indexOf(s, 0, defaultIndex+j+1, '.');
                    String fullArgumentPath = s.substring(0, i);
                    commands.put(fullArgumentPath, new CommandData(declaredMethod, commandInstance, senderOnlyPlayer, senderIndex, arguments, defaultIndex, fullArgumentPath));
                    node.require(requirement);
                    node.executes(info -> {
                        try{
                            CommandData commandData = commands.get(fullArgumentPath);
                            
                            if(commandData.playerOnly && !info.sender().isPlayer()){
                                info.sender().getSender().sendMessage(ChatColor.RED + "This command can only be used by players!");
                                return 1;
                            }
                            List<Object> argumentValues = new ArrayList<>();
                            for(int k = 0; k < commandData.arguments.size(); k++){
                                Argument argument = commandData.arguments.get(k);
                                if(k < commandData.defaultsIndex){
                                    argumentValues.add(info.getArgument(argument.name(), argument.type()));
                                } else{
                                    if(!argument.defaultB().equals(BoolUnset.UNSET)){
                                        argumentValues.add(argument.defaultB().value);
                                    } else if(argument.defaultD() != Double.MIN_VALUE){
                                        argumentValues.add(argument.defaultD());
                                    } else if(argument.defaultF() != Float.MIN_VALUE){
                                        argumentValues.add(argument.defaultF());
                                    } else if(argument.defaultI() != Integer.MIN_VALUE){
                                        argumentValues.add(argument.defaultI());
                                    } else if(argument.defaultL() != Long.MIN_VALUE){
                                        argumentValues.add(argument.defaultL());
                                    } else{
                                        argumentValues.add(argument.defaultS());
                                    }
                                }
                            }
                            
                            if(commandData.senderIndex != -1)
                                argumentValues.add(commandData.senderIndex, info.sender().getSender());
                            
                            executeFunction(commandData, info, argumentValues.toArray(new Object[argumentValues.size()]));
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                        return 1;
                    });
                }
                
                if(!defaultNodes.isEmpty())continue;
                String fullArgumentPath = methodPath.toString();
                commands.put(fullArgumentPath, new CommandData(declaredMethod, commandInstance, senderOnlyPlayer, senderIndex, arguments, defaultIndex, fullArgumentPath));
                currentNode.require(requirement);
                currentNode.executes(info -> {
                    try{
                        CommandData commandData = commands.get(fullArgumentPath);
                        
                        if(commandData.playerOnly && !info.sender().isPlayer()){
                            info.sender().getSender().sendMessage(ChatColor.RED + "This command can only be used by players!");
                            return 1;
                        }
                        
                        List<Object> argumentValues = new ArrayList<>();
                        for(Argument argument : commandData.arguments){
                            argumentValues.add(info.getArgument(argument.name(), argument.type()));
                        }
                        
                        if(commandData.senderIndex != -1)
                            argumentValues.add(commandData.senderIndex, info.sender().getSender());
                        
                        executeFunction(commandData, info, argumentValues.toArray(new Object[argumentValues.size()]));
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                    return 1;
                });
            }
        }
        QAPI.commandProvider().registerCommand(masterNode, pathRest);
        return commandInstance;
    }
    
    public static void removeCommand(String command){
        QAPI.commandProvider().unregisterCommand(command);
    }
    
    private static void executeFunction(CommandData commandData, CommandExecutorInfo info, Object... args){
        Method method = commandData.method;
        Object[] argsCopy = new Object[args.length];
        for(int i = 0; i < commandData.convertersUsed.length; i++){
            if(commandData.convertersUsed[i] == null){
                argsCopy[i] = args[i];
                continue;
            }
            argsCopy[i] = commandData.convertersUsed[i].convert(args[i]);
        }
        try{
            method.invoke(commandData.instance, argsCopy);
        } catch(IllegalArgumentException e){
            log.severe(() -> {
                StringBuilder s = new StringBuilder("\nParameter types dont match!!!\n\nExpected types:\n");
                Class<?>[] parameterTypes = method.getParameterTypes();
                for(Class<?> parameterType : parameterTypes){
                    s.append("\t").append(parameterType.getName()).append('\n');
                }
                s.append("\nTypes provided:\n");
                for(Object arg : argsCopy){
                    s.append("\t").append(arg == null ? "null" : arg.getClass().getName());
                }
                return s.toString();
            });
        } catch(InvocationTargetException e){
            Throwable targetException = e.getTargetException();
            if(targetException instanceof CommandException){
                CommandException exception = (CommandException) targetException;
                exception.send(info);
            } else{
                e.printStackTrace();
            }
        } catch(IllegalAccessException e){
            e.printStackTrace();
        }
    }
    
    private static Argument findParameterByName(String name, Parameter[] parameters){
        for(Parameter parameter : parameters){
            if(parameter.isAnnotationPresent(Sender.class)) continue;
            if(!parameter.isAnnotationPresent(Argument.class)){
                System.err.println("Parameter " + parameter.getName() + " (" + parameter.getDeclaringExecutable().getName() + " in " + parameter.getDeclaringExecutable().getDeclaringClass().getName() + ") is not annotated with the @Argument annotation");
                continue;
            }
            
            Argument annotation = parameter.getAnnotation(Argument.class);
            if(annotation.name().equals(name)) return annotation;
        }
        return null;
    }
    
    private static class CommandData{
        public final Method method;
        public final Command instance;
        public final Class<?>[] parameterTypes;
        public final TypeConverter<?, ?>[] convertersUsed;
        public final boolean playerOnly;
        public final int senderIndex;
        public final List<Argument> arguments;
        public final int defaultsIndex;
        public final String fullPath;
        
        private CommandData(Method method, Command instance, boolean playerOnly, int senderIndex, List<Argument> arguments, int defaultsIndex, String fullPath){
            this.method = method;
            this.instance = instance;
            this.parameterTypes = method.getParameterTypes();
            this.defaultsIndex = defaultsIndex;
            this.fullPath = fullPath;
            this.convertersUsed = new TypeConverter<?, ?>[this.parameterTypes.length];
            for(int i = 0; i < this.parameterTypes.length; i++){
                TypeConverter<?, ?> typeConverter = typeConverters.get(this.parameterTypes[i]);
                if(typeConverter==null || !typeConverter.getTypeOut().isAssignableFrom(this.parameterTypes[i]))continue;
                this.convertersUsed[i] = typeConverter;
            }
            this.playerOnly = playerOnly;
            this.senderIndex = senderIndex;
            this.arguments = arguments;
        }
    }
}
