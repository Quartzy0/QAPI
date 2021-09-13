package com.quartzy.qapi.command;

public interface CommandProvider{
    void registerCommand(LiteralNode node);
    
    default void registerCommand(LiteralNode node, String path){
        registerCommand(node);
    }
    
    void unregisterCommand(String commandName);
    
    default void reloadCommands(){}
    
    CommandSenderInfo createSenderInstance();
    
    Class<?> returnClassFromType(ArgumentType type);
}
