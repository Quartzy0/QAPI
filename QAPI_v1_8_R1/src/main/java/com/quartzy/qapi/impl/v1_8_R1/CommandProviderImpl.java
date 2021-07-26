package com.quartzy.qapi.impl.v1_8_R1;

import com.quartzy.qapi.command.ArgumentTypeEnum;
import com.quartzy.qapi.command.CommandProvider;
import com.quartzy.qapi.command.CommandSenderInfo;
import com.quartzy.qapi.command.LiteralNode;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;

import java.lang.reflect.Field;
import java.util.Map;

public class CommandProviderImpl implements CommandProvider{
    private static Field commandMapField;
    
    static {
        try{
            commandMapField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            commandMapField.setAccessible(true);
        } catch(NoSuchFieldException e){
            e.printStackTrace();
        }
    }
    
    @Override
    public void registerCommand(LiteralNode node){
        ((CraftServer) Bukkit.getServer()).getCommandMap().register("qapi", new CommandController(node));
    }
    
    @Override
    public void unregisterCommand(String commandName){
        try{
            Map<String, Command> o = (Map<String, Command>) commandMapField.get(((CraftServer) Bukkit.getServer()).getCommandMap());
            o.remove(commandName);
        } catch(IllegalAccessException e){
            e.printStackTrace();
        }
    }
    
    @Override
    public CommandSenderInfo createSenderInstance(){
        return new CommandSenderInfo_v1_8_R1(null, null);
    }
    
    @Override
    public Class<?> returnClassFromType(ArgumentTypeEnum type){
        return null;
    }
}
