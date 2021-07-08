package com.quartzy.qapi.impl.v1_9_R2;

import com.quartzy.qapi.command.ArgumentTypeEnum;
import com.quartzy.qapi.command.CommandProvider;
import com.quartzy.qapi.command.CommandSenderInfo;
import com.quartzy.qapi.command.LiteralNode;

public class CommandProviderImpl implements CommandProvider{
    @Override
    public void registerCommand(LiteralNode node){
    
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
