package com.quartzy.qapi.command;

public class ArgumentNode extends Node<ArgumentNode>{
    private ArgumentTypeEnum type;
    private long minL = Long.MIN_VALUE, maxL = Long.MAX_VALUE;
    private int minI = Integer.MIN_VALUE, maxI = Integer.MAX_VALUE;
    private float minF = -Float.MAX_VALUE, maxF = Float.MAX_VALUE;
    private double minD = -Double.MAX_VALUE, maxD = Double.MAX_VALUE;
    
    public ArgumentNode(String name, ArgumentTypeEnum type){
        super(name);
        this.type = type;
    }
    
    public ArgumentNode(String name){
        super(name);
    }
    
    public void setType(ArgumentTypeEnum type){
        this.type = type;
    }
    
    public ArgumentTypeEnum getType(){
        return type;
    }
    
    @Override
    public ArgumentNode getThis(){
        return this;
    }
    
    public ArgumentNode setMinL(long minL){
        this.minL = minL;
        return this;
    }
    
    public ArgumentNode setMaxL(long maxL){
        this.maxL = maxL;
        return this;
    }
    
    public ArgumentNode setMinI(int minI){
        this.minI = minI;
        return this;
    }
    
    public ArgumentNode setMaxI(int maxI){
        this.maxI = maxI;
        return this;
    }
    
    public ArgumentNode setMinF(float minF){
        this.minF = minF;
        return this;
    }
    
    public ArgumentNode setMaxF(float maxF){
        this.maxF = maxF;
        return this;
    }
    
    public ArgumentNode setMinD(double minD){
        this.minD = minD;
        return this;
    }
    
    public ArgumentNode setMaxD(double maxD){
        this.maxD = maxD;
        return this;
    }
    
    public long getMinL(){
        return minL;
    }
    
    public long getMaxL(){
        return maxL;
    }
    
    public int getMinI(){
        return minI;
    }
    
    public int getMaxI(){
        return maxI;
    }
    
    public float getMinF(){
        return minF;
    }
    
    public float getMaxF(){
        return maxF;
    }
    
    public double getMinD(){
        return minD;
    }
    
    public double getMaxD(){
        return maxD;
    }
}
