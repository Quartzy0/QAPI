package com.quartzy.qapi.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class Node<T extends Node<T>>{
    protected List<Node> children;
    protected Predicate<CommandSenderInfo> requirement;
    protected Executor executor;
    protected String name;
    
    public Node(String name){
        this.children = new ArrayList<>();
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public Predicate<CommandSenderInfo> getRequirement(){
        return requirement;
    }
    
    public abstract T getThis();
    
    public T require(Predicate<CommandSenderInfo> requirement){
        this.requirement = requirement;
        return getThis();
    }
    
    public T executes(Executor executor){
        this.executor = executor;
        return getThis();
    }
    
    public Executor getExecutor(){
        return executor;
    }
    
    public T then(ArgumentNode node){
        this.addChild(node);
        return getThis();
    }
    
    public List<Node> getChildren(){
        return this.children;
    }
    
    public void addChild(Node node){
        this.children.add(node);
    }
    
    public void removeChild(int i){
        this.children.remove(i);
    }
    
    public void removeChild(){
        this.children.remove(this.children.size()-1);
    }
}
