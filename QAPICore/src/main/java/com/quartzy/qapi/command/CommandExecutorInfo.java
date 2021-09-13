package com.quartzy.qapi.command;

import com.quartzy.qapi.StringRange;

public interface CommandExecutorInfo{
    
    CommandSenderInfo sender();
    
    Object getArgument(String argumentName, ArgumentType type);
    
    String getCommandString();
    
    default String getCommandName(){
        String commandString = this.getCommandString();
        int slashIndex = commandString.indexOf('/');
        commandString = slashIndex!=-1 ? commandString.substring(slashIndex) : commandString;
        return commandString.substring(0, commandString.indexOf(' '));
    }
    
    String getArgumentString(String argumentName);
    
    StringRange getArgumentRange(String argumentName);
}
