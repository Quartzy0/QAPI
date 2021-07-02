package com.quartzy.qapi.command;

public interface CommandProvider{
    void registerCommand(LiteralNode node);
    
    CommandSenderInfo createSenderInstance();
    
    Class<?> returnClassFromType(ArgumentTypeEnum type);
}
