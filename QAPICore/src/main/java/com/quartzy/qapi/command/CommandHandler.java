package com.quartzy.qapi.command;

import com.quartzy.qapi.QAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandHandler{
    public static final String ARGUMENT_SEPARATOR = " ";
    
    public static final char ARGUMENT_SEPARATOR_CHAR = ' ';
    
    private static final String USAGE_OPTIONAL_OPEN = "[";
    private static final String USAGE_OPTIONAL_CLOSE = "]";
    private static final String USAGE_REQUIRED_OPEN = "(";
    private static final String USAGE_REQUIRED_CLOSE = ")";
    private static final String USAGE_OR = "|";
    
    
    private static HashMap<String, Command> commands = new HashMap<>();
    
    public static <T extends Command> T addCommand(Class<T> commandClass){
        if(!commandClass.isAnnotationPresent(CommandExecutor.class))return null;
        CommandExecutor commandExecutorA = commandClass.getAnnotation(CommandExecutor.class);
        T commandInstance = null;
        try{
            Constructor<T> constructor = commandClass.getConstructor();
            commandInstance = constructor.newInstance();
        } catch(NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        }
        if(commandInstance==null) return null;
        
        LiteralNode masterNode = new LiteralNode(commandExecutorA.value());
        Method[] declaredMethods = commandClass.getDeclaredMethods();
        methodLoop:
        for(int i = 0; i < declaredMethods.length; i++){
            if(declaredMethods[i].isAnnotationPresent(ArgumentExecutor.class)){
                ArgumentExecutor annotation = declaredMethods[i].getAnnotation(ArgumentExecutor.class);
                String path = annotation.value();
                String[] pathNames = path.split("\\.");
                Node currentNode = masterNode;
                Parameter[] parameters = declaredMethods[i].getParameters();
                List<Argument> arguments = new ArrayList<>();
                List<WeakReference<Node>> defaultNodes = new ArrayList<>();
                int defaultIndex = -1;
                int senderIndex = -1;
                boolean senderOnlyPlayer = false;
                for(int i1 = 0; i1 < parameters.length; i1++){
                    if(parameters[i1].isAnnotationPresent(Sender.class)){
                        senderIndex = i1;
                        senderOnlyPlayer = parameters[i1].getType().isAssignableFrom(Player.class);
                        break;
                    }
                }
                pathLoop:
                for(int i1 = 0; i1 < pathNames.length; i1++){
                    Argument parameterByName = findParameterByName(pathNames[i1], parameters);
                    for(Object child : currentNode.getChildren()){
                        Node node = (Node) child;
                        if(node.getName().equals(pathNames[i1])){
                            currentNode = node;
                            if(parameterByName!=null){
                                arguments.add(parameterByName);
                            }
                            continue pathLoop;
                        }
                    }
                    Node newNode;
                    if(parameterByName!=null){
                        arguments.add(parameterByName);
                        newNode = new ArgumentNode(pathNames[i1]);
                        ((ArgumentNode) newNode).setType(parameterByName.type());
                        switch(parameterByName.type()) {
                            case INTEGER:
                                ((ArgumentNode) newNode).setMaxI(parameterByName.max()>Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) parameterByName.max());
                                ((ArgumentNode) newNode).setMinI(parameterByName.min()<Integer.MIN_VALUE ? Integer.MIN_VALUE : (int) parameterByName.min());
                                break;
                            case LONG:
                                ((ArgumentNode) newNode).setMaxL(parameterByName.max());
                                ((ArgumentNode) newNode).setMinL(parameterByName.min());
                                break;
                            case FLOAT:
                                ((ArgumentNode) newNode).setMaxF(parameterByName.maxD()>Float.MAX_VALUE ? Float.MAX_VALUE : (int) parameterByName.maxD());
                                ((ArgumentNode) newNode).setMinF(parameterByName.minD()>-Float.MAX_VALUE ? -Float.MAX_VALUE : (int) parameterByName.minD());
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
                        if(!parameterByName.defaultB().equals(BoolUnset.UNSET) || parameterByName.defaultD()!=Double.MIN_VALUE || parameterByName.defaultF()!=Float.MIN_VALUE || parameterByName.defaultI()!=Integer.MIN_VALUE || parameterByName.defaultL()!=Long.MIN_VALUE){
                            defaultNodes.add(new WeakReference<>(currentNode));
                            if(defaultIndex==-1) defaultIndex = i1;
                        }else if(defaultIndex!=-1){
                            System.err.println("All values after the default value must also have a default value (method " + declaredMethods[i].getName() + " in class " + commandClass.getName() + "). The command will be skipped");
                            continue methodLoop;
                        }
                    }else{
                        newNode = new LiteralNode(pathNames[i1]);
                    }
                    currentNode.addChild(newNode);
                    currentNode = newNode;
                }
                int finalI = i;
                Command finalCommandInstance = commandInstance;
                int finalDefaultIndex = defaultIndex;
                boolean finalSenderOnlyPlayer = senderOnlyPlayer;
                int finalSenderIndex = senderIndex;
                for(int j = 0; j < defaultNodes.size(); j++){
                    WeakReference<Node> defaultNode = defaultNodes.get(j);
                    defaultNode.get().executes(info -> {
                        try{
                            if(finalSenderOnlyPlayer && !info.sender().isPlayer()){
                                info.sender().getSender().sendMessage(ChatColor.RED + "This command can only be used by players!");
                                return 1;
                            }
                            List<Object> argumentValues = new ArrayList<>();
                            for(int k = 0; k < arguments.size(); k++){
                                Argument argument = arguments.get(k);
                                if(k < finalDefaultIndex){
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
                            
                            if(finalSenderIndex!=-1)
                                argumentValues.add(finalSenderIndex, info.sender().getSender());
                            
                            executeFunction(info, declaredMethods[finalI], finalCommandInstance, argumentValues.toArray(new Object[argumentValues.size()]));
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        return 1;
                    });
                }
                currentNode.executes(info -> {
                    try{
                        if(finalSenderOnlyPlayer && !info.sender().isPlayer()){
                            info.sender().getSender().sendMessage(ChatColor.RED + "This command can only be used by players!");
                            return 1;
                        }
                        
                        List<Object> argumentValues = new ArrayList<>();
                        for(Argument argument : arguments){
                            argumentValues.add(info.getArgument(argument.name(), argument.type()));
                        }
    
                        if(finalSenderIndex!=-1)
                            argumentValues.add(finalSenderIndex, info.sender().getSender());
    
                        executeFunction(info, declaredMethods[finalI], finalCommandInstance, argumentValues.toArray(new Object[argumentValues.size()]));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    return 1;
                });
            }
        }
        QAPI.commandProvider().registerCommand(masterNode);
        return commandInstance;
    }
    
    public static Command removeCommand(String command){
        QAPI.commandProvider().unregisterCommand(command);
        return commands.remove(command);
    }
    
    private static int executeFunction(CommandExecutorInfo info, Method method, Command instance, Object... args){
        try{
            method.invoke(instance, args);
        }catch(IllegalArgumentException e){
            Class<?>[] parameterTypes = method.getParameterTypes();
            for(int i = 0; i < parameterTypes.length; i++){
                System.out.println(parameterTypes[i].getName());
            }
            System.out.println(" ");
            System.out.println(" ");
            for(int i = 0; i < args.length; i++){
                System.out.println(args[i]==null ? "null" : args[i].getClass().getName());
            }
        } catch(InvocationTargetException e){
            Throwable targetException = e.getTargetException();
            if(targetException instanceof CommandException){
                CommandException exception = (CommandException) targetException;
                exception.send(info);
            } else {
                e.printStackTrace();
            }
        } catch(IllegalAccessException e){
            e.printStackTrace();
        }
        return 1;
    }
    
    private static Argument findParameterByName(String name, Parameter[] parameters){
        for(int i = 0; i < parameters.length; i++){
                if(!parameters[i].isAnnotationPresent(Argument.class)){
                    System.err.println("Parameter " + parameters[i].getName() + " is not annotated with the @Argument annotation");
                    continue;
                }
    
            Argument annotation = parameters[i].getAnnotation(Argument.class);
            if(annotation.name().equals(name)) return annotation;
        }
        return null;
    }
}
