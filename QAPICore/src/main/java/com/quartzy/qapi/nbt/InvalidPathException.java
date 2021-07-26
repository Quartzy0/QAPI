package com.quartzy.qapi.nbt;

import com.quartzy.qapi.StringRange;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

public class InvalidPathException extends Exception{
    private StringRange range;
    private String fullPath;
    
    public InvalidPathException(String message, StringRange range, String fullPath){
        super(message);
        this.range = range;
        this.fullPath = fullPath;
    }
    
    public void send(CommandSender sender){
        TextComponent component = new TextComponent(this.getMessage());
        component.setColor(ChatColor.RED);
        if(range!=null){
            TextComponent component1 = new TextComponent(this.fullPath.substring(0, range.getStart()));
            TextComponent component2 = new TextComponent(this.fullPath.substring(range.getStart(), range.getEnd()));
            TextComponent component3 = new TextComponent(this.fullPath.substring(range.getStart(), range.getEnd()));
            component1.setColor(ChatColor.GRAY);
            component2.setColor(ChatColor.GRAY);
            component2.setUnderlined(true);
            component3.setColor(ChatColor.GRAY);
        }
        sender.spigot().sendMessage(component);
    }
}
