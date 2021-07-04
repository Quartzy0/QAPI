package com.quartzy.qapi.command;

import com.quartzy.qapi.StringRange;

public interface CommandExecutorInfo{
    
    CommandSenderInfo sender();
    
    Object getArgument(String argumentName, ArgumentTypeEnum type);
    
    String getCommandString();
    
    String getArgumentString(String argumentName);
    
    StringRange getArgumentRange(String argumentName);
}
