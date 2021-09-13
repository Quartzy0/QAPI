package com.quartzy.qapi.impl.v1_9_R2;

import com.quartzy.qapi.StringRange;
import com.quartzy.qapi.command.*;

import java.util.HashMap;

public class CommandExecutorInfo_v1_9_R2 implements CommandExecutorInfo{
    private final CommandSenderInfo_v1_9_R2 sender;
    private HashMap<String, CommandController.ParsedArgument> arguments;
    private final String fullString;
    private final String argString;
    
    public CommandExecutorInfo_v1_9_R2(CommandSenderInfo_v1_9_R2 sender, HashMap<String, CommandController.ParsedArgument> arguments, String fullString, String argString){
        this.sender = sender;
        this.arguments = arguments;
        this.fullString = fullString;
        this.argString = argString;
    }
    
    public void setArguments(HashMap<String, CommandController.ParsedArgument> arguments){
        this.arguments = arguments;
    }
    
    @Override
    public CommandSenderInfo sender(){
        return sender;
    }
    
    @Override
    public Object getArgument(String argumentName, ArgumentType type){
        return arguments.get(argumentName).value;
    }
    
    @Override
    public String getCommandString(){
        return fullString;
    }
    
    @Override
    public String getArgumentString(String argumentName){
        return arguments.get(argumentName).range.get(argString);
    }
    
    @Override
    public StringRange getArgumentRange(String argumentName){
        return arguments.get(argumentName).range.add(fullString.length()-argString.length());
    }
    
    public CommandSenderInfo_v1_9_R2 getSender(){
        return sender;
    }
}
