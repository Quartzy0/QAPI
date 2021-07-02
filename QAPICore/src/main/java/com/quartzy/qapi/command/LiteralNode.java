package com.quartzy.qapi.command;

public class LiteralNode extends Node<LiteralNode>{
    
    public LiteralNode(String name){
        super(name);
    }
    
    @Override
    public LiteralNode getThis(){
        return this;
    }
}
