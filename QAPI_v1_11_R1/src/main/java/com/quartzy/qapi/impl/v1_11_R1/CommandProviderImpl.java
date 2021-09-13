package com.quartzy.qapi.impl.v1_11_R1;

import com.quartzy.qapi.command.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_11_R1.CraftServer;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
public class CommandProviderImpl implements CommandProvider{
    private static Field commandMapField;
    
    static {
        try{
            commandMapField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            commandMapField.setAccessible(true);
        } catch(NoSuchFieldException e){
            e.printStackTrace();
        }
    }
    
    private final HashMap<String, CommandController> commandControllers = new HashMap<>();
    
    @Override
    public void registerCommand(LiteralNode node){
        CommandController command = new CommandController(node);
        commandControllers.put(node.getName(), command);
        ((CraftServer) Bukkit.getServer()).getCommandMap().register("qapi", command);
    }
    
    @Override
    public void registerCommand(LiteralNode node, String path){
        if(path.isEmpty()){
            registerCommand(node);
            return;
        }
        int endIndex = path.indexOf('.');
        endIndex = endIndex==-1 ? path.length() : endIndex;
        String firstNode = path.substring(0, endIndex);
        String[] newPath = endIndex==path.length() ? new String[0] : path.substring(endIndex+1).split("\\.");
        CommandController commandController = commandControllers.get(firstNode);
        if(commandController!=null){
            Node<?> currentNode = commandController.getCommandNode();
            pathLoop:
            for(String s : newPath){
                for(int j = 0; j < currentNode.getChildren().size(); j++){
                    Node<?> child = currentNode.getChildren().get(j);
                    if(child.getName().equals(s)){
                        currentNode = child;
                        continue pathLoop;
                    }
                }
                LiteralNode node1 = new LiteralNode(s);
                currentNode.addChild(node1);
                currentNode = node1;
            }
            currentNode.addChild(node);
        }else{
            LiteralNode masterNode = new LiteralNode(firstNode);
            Node<?> currentNode = masterNode;
            for(String s : newPath){
                LiteralNode node1 = new LiteralNode(s);
                currentNode.addChild(node1);
                currentNode = node1;
            }
            currentNode.addChild(node);
            this.registerCommand(masterNode);
        }
    }
    
    @Override
    public void unregisterCommand(String commandName){
        try{
            Map<String, Command> o = (Map<String, Command>) commandMapField.get(((CraftServer) Bukkit.getServer()).getCommandMap());
            o.remove(commandName);
        } catch(IllegalAccessException e){
            e.printStackTrace();
        }
    }
    
    @Override
    public CommandSenderInfo createSenderInstance(){
        return new CommandSenderInfo_v1_11_R1(null, null);
    }
    
    @Override
    public Class<?> returnClassFromType(ArgumentType type){
        return null;
    }
}
