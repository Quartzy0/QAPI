package com.quartzy.qapi.impl.v1_8_R1;

import com.quartzy.qapi.command.CommandSenderInfo;
import com.quartzy.qapi.command.Node;
import net.minecraft.server.v1_8_R1.ICommandListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.craftbukkit.v1_8_R1.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.v1_8_R1.command.CraftConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftMinecartCommand;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.util.Vector;

public class CommandSenderInfo_v1_8_R1 implements CommandSenderInfo{
    private CommandSender sender;
    private ICommandListener listener;
    private Node node;
    
    public CommandSenderInfo_v1_8_R1(CommandSender sender, Node node){
        this.sender = sender;
        if(sender instanceof CraftEntity){
            this.listener = ((CraftEntity) sender).getHandle();
        }else if(sender instanceof CraftBlockCommandSender){
            this.listener = ((CraftBlockCommandSender) sender).getTileEntity();
        }else if(sender instanceof CommandMinecart){
            this.listener = ((CraftMinecartCommand) sender).getHandle();
        }
        this.node = node;
    }
    
    @Override
    public boolean hasPermission(int level, String bukkitPerm){
        return this.listener.a(level, bukkitPerm);
    }
    
    @Override
    public boolean hasPermission(int level){
        return listener.a(level, "minecraft.command." + node.getName());
    }
    
    @Override
    public Vector getPosition(){
        return new Vector(listener.d().a, listener.d().b, listener.d().c);
    }
    
    @Override
    public World getWorld(){
        return listener.getWorld().getWorld();
    }
    
    @Override
    public CommandSender getSender(){
        return this.sender;
    }
    
    @Override
    public Player getPlayer(){
        return isPlayer() ? (Player) getSender() : null;
    }
    
    @Override
    public boolean isPlayer(){
        return getSender() instanceof Player;
    }
    
    public ICommandListener getListener(){
        return listener;
    }
    
    public Node getNode(){
        return node;
    }
}
