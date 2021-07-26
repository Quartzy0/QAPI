package com.quartzy.qapi.impl.v1_8_R2;

import com.quartzy.qapi.command.ArgumentTypeEnum;
import com.quartzy.qapi.command.CommandProvider;
import com.quartzy.qapi.command.CommandSenderInfo;
import com.quartzy.qapi.command.LiteralNode;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;

import java.util.Map;

public class CommandProviderImpl implements CommandProvider{
    @Override
    public void registerCommand(LiteralNode node){
    
    }
    
    @Override
    public void unregisterCommand(String commandName){
    
    }
    
    @Override
    public CommandSenderInfo createSenderInstance(){
        return null;
    }
    
    @Override
    public Class<?> returnClassFromType(ArgumentTypeEnum type){
        return null;
    }
}
