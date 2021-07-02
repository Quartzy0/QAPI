package com.quartzy.qapi.command;

public enum BoolUnset{
    TRUE(true), FALSE(false), UNSET(false);
    
    public final boolean value;
    
    BoolUnset(boolean value){
        this.value = value;
    }
}
