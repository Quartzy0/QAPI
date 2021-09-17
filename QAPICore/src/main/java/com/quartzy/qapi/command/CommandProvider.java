package com.quartzy.qapi.command;

public interface CommandProvider{
    void registerCommand(LiteralNode node, String path);
    
    void unregisterCommand(String commandName);
    
    default void reloadCommands(){}
    
    CommandSenderInfo createSenderInstance();
    
    Class<?> returnClassFromType(ArgumentType type);
}
