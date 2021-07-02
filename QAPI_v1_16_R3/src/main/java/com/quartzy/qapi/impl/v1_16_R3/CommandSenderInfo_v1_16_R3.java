package com.quartzy.qapi.impl.v1_16_R3;

import com.quartzy.qapi.command.CommandSenderInfo;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CommandSenderInfo_v1_16_R3 implements CommandSenderInfo{
    private CommandListenerWrapper wrapper;
    
    public void setWrapper(CommandListenerWrapper wrapper){
        this.wrapper = wrapper;
    }
    
    @Override
    public boolean hasPermission(int level, String bukkitPerm){
        return wrapper.hasPermission(level, bukkitPerm);
    }
    
    @Override
    public boolean hasPermission(int level){
        return wrapper.hasPermission(level);
    }
    
    @Override
    public Vector getPosition(){
        return new Vector(wrapper.getPosition().x, wrapper.getPosition().y, wrapper.getPosition().z);
    }
    
    @Override
    public World getWorld(){
        return wrapper.getWorld().getWorld();
    }
    
    @Override
    public CommandSender getSender(){
        return wrapper.getBukkitSender();
    }
    
    @Override
    public Player getPlayer(){
        return wrapper.getBukkitSender() instanceof Player ? ((Player) wrapper.getBukkitSender()) : null;
    }
    
    @Override
    public boolean isPlayer(){
        return wrapper.getBukkitSender() instanceof Player;
    }
}
