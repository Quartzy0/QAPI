package com.quartzy.qapi.command;

public interface CommandProvider{
    void registerCommand(LiteralNode node);
    
    void unregisterCommand(String commandName);
    
    CommandSenderInfo createSenderInstance();
    
    Class<?> returnClassFromType(ArgumentTypeEnum type);
}
