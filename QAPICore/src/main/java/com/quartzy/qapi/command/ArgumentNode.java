package com.quartzy.qapi.command;

public class ArgumentNode extends Node<ArgumentNode>{
    private ArgumentType type;
    private long minL = Long.MIN_VALUE, maxL = Long.MAX_VALUE;
    private int minI = Integer.MIN_VALUE, maxI = Integer.MAX_VALUE;
    private float minF = -Float.MAX_VALUE, maxF = Float.MAX_VALUE;
    private double minD = -Double.MAX_VALUE, maxD = Double.MAX_VALUE;
    
    public ArgumentNode(String name){
        super(name);
    }
    
    public void setType(ArgumentType type){
        this.type = type;
    }
    
    public ArgumentType getType(){
        return type;
    }
    
    @Override
    public ArgumentNode getThis(){
        return this;
    }
    
    public void setMinL(long minL){
        this.minL = minL;
    }
    
    public void setMaxL(long maxL){
        this.maxL = maxL;
    }
    
    public void setMinI(int minI){
        this.minI = minI;
    }
    
    public void setMaxI(int maxI){
        this.maxI = maxI;
    }
    
    public void setMinF(float minF){
        this.minF = minF;
    }
    
    public void setMaxF(float maxF){
        this.maxF = maxF;
    }
    
    public void setMinD(double minD){
        this.minD = minD;
    }
    
    public void setMaxD(double maxD){
        this.maxD = maxD;
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
