package com.quartzy.qapi.impl.v1_8_R2;

import com.quartzy.qapi.command.CommandSenderInfo;
import com.quartzy.qapi.command.Node;
import net.minecraft.server.v1_8_R2.ICommandListener;
import net.minecraft.server.v1_8_R2.MinecraftServer;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.craftbukkit.v1_8_R2.CraftServer;
import org.bukkit.craftbukkit.v1_8_R2.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.v1_8_R2.command.ProxiedNativeCommandSender;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.util.Vector;

public class CommandSenderInfo_v1_8_R2 implements CommandSenderInfo{
    private final CommandSender sender;
    private ICommandListener listener;
    private final Node<?> node;
    
    public CommandSenderInfo_v1_8_R2(CommandSender sender, Node<?> node){
        this.sender = sender;
        if (sender instanceof Player) {
            this.listener = ((CraftPlayer)sender).getHandle();
        } else if (sender instanceof BlockCommandSender) {
            this.listener =  ((CraftBlockCommandSender)sender).getTileEntity();
        } else if (sender instanceof CommandMinecart) {
            this.listener =  ((CraftMinecartCommand)sender).getHandle();
        } else if (sender instanceof RemoteConsoleCommandSender) {
            this.listener = MinecraftServer.getServer();
        } else if (sender instanceof ConsoleCommandSender) {
            this.listener = ((CraftServer)sender.getServer()).getServer();
        } else if (sender instanceof ProxiedCommandSender) {
            this.listener = ((ProxiedNativeCommandSender)sender).getHandle();
        }
        this.node = node;
    }
    
    @Override
    public boolean hasPermission(int level, String bukkitPerm){
        if(!bukkitPerm.isEmpty())return sender.hasPermission(bukkitPerm);
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
        return (getSender() instanceof Player);
    }
    
    public ICommandListener getListener(){
        return listener;
    }
}
