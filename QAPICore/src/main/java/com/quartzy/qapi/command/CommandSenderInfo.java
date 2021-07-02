package com.quartzy.qapi.command;


import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public interface CommandSenderInfo{
    boolean hasPermission(int level, String bukkitPerm);
    
    boolean hasPermission(int level);
    
    Vector getPosition();
    
    World getWorld();
    
    CommandSender getSender();
    
    Player getPlayer();
    
    boolean isPlayer();
}
