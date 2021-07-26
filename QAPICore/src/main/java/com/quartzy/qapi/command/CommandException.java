package com.quartzy.qapi.command;

import com.quartzy.qapi.QAPI;
import com.quartzy.qapi.StringRange;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.ChatColor;

public class CommandException extends Exception{
    
    public static final int ERROR_COMMAND_OUT_LENGTH = 40;
    
    private String argument;
    private int index;
    private Object[] args;
    private boolean translated;
    
    public CommandException(String argument, String message, Object... args) {
        super(String.format(message, args));
        this.argument = argument;
        this.index = -1;
        this.translated = false;
        this.args = args;
    }
    
    public CommandException(String argument, boolean translated, String message, Object... args) {
        super(String.format(message, args));
        this.argument = argument;
        this.index = -1;
        this.translated = translated;
        this.args = args;
    }
    
    public CommandException(int index, String message, Object... args) {
        super(String.format(message, args));
        this.index = index;
        this.argument = null;
        this.translated = false;
        this.args = args;
    }
    
    public CommandException(int index, boolean translated, String message, Object... args) {
        super(message);
        this.index = index;
        this.argument = null;
        this.args = args;
        this.translated = translated;
    }
    
    public String getArgument(){
        return argument;
    }
    
    public void send(CommandExecutorInfo info){
        StringRange argumentRange;
        String commandString = info.getCommandString();
        if(this.argument==null || (argumentRange = info.getArgumentRange(this.argument))==null){
            if(this.index!=-1){
                commandString = commandString.substring(0, this.index) + ChatColor.RESET;
            }else{
                if(translated){
                    TranslatableComponent textComponent = new TranslatableComponent(this.getMessage(), this.args);
                    textComponent.setColor(net.md_5.bungee.api.ChatColor.RED);
                    info.sender().getSender().spigot().sendMessage(textComponent);
                }else{
                    TextComponent textComponent = new TextComponent(String.format(this.getMessage(), this.args));
                    textComponent.setColor(net.md_5.bungee.api.ChatColor.RED);
                    info.sender().getSender().spigot().sendMessage(textComponent);
                }
                return;
            }
        }else{
            commandString = commandString.substring(0, argumentRange.getStart()) + ChatColor.UNDERLINE + info.getArgumentString(argument) + ChatColor.RESET;
        }
        commandString = commandString.trim();
        if(commandString.length()>CommandException.ERROR_COMMAND_OUT_LENGTH){
            commandString = "..." + commandString.substring(commandString.length()-CommandException.ERROR_COMMAND_OUT_LENGTH+3);
        }
    
        BaseComponent msgComponent;
        if(translated){
            msgComponent = new TranslatableComponent(this.getMessage(), this.args);
        }else{
            msgComponent = new TextComponent(String.format(this.getMessage(), this.args));
            
        }
        msgComponent.setColor(net.md_5.bungee.api.ChatColor.RED);
        TextComponent component = new TextComponent("\n" + commandString);
        component.setColor(net.md_5.bungee.api.ChatColor.GRAY);
        msgComponent.addExtra(component);
        TextComponent component1 = new TextComponent(" <--[HERE]");
        component1.setColor(net.md_5.bungee.api.ChatColor.RED);
        msgComponent.addExtra(component1);
    
        QAPI.qapi().sendMessage(info.sender().getSender(), msgComponent);
    }
}
