package com.quartzy.qapi.command;

public class LiteralNode extends Node<LiteralNode>{
    
    private final String[] aliases;
    
    public LiteralNode(String name, String[] aliases){
        super(name);
        this.aliases = aliases;
    }
    
    public LiteralNode(String name){
        super(name);
        this.aliases = null;
    }
    
    @Override
    public LiteralNode getThis(){
        return this;
    }
    
    public String[] getAliases(){
        return aliases;
    }
}
