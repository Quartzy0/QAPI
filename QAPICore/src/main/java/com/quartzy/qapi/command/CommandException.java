package com.quartzy.qapi.command;

public class CommandException extends Exception{
    
    public static final int ERROR_COMMAND_OUT_LENGTH = 40;
    
    private String argument;
    
    public CommandException(String message, Object... args) {
        this(null, message, args);
    }
    
    public CommandException(String argument, String message, Object... args) {
        super(String.format(message, args));
        this.argument = argument;
    }
    
    public String getArgument(){
        return argument;
    }
}
