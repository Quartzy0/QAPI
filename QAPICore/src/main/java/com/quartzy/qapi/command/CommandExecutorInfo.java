package com.quartzy.qapi.command;

public interface CommandExecutorInfo{
    
    CommandSenderInfo sender();
    
    Object getArgument(String argumentName, ArgumentTypeEnum type);
}
