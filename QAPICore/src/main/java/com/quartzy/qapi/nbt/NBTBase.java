package com.quartzy.qapi.nbt;

public interface NBTBase{
    
    default String asString(){
        return this.toString();
    }
}
