package com.quartzy.qapi.nbt;

import java.util.Objects;

public class NBTString implements NBTBase{
    private final String data;
    
    public NBTString(String data){
        this.data = data;
    }
    
    public String getString(){
        return data;
    }
    
    @Override
    public String toString(){
        return "NBTString{" +
                "data='" + data + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        NBTString nbtString = (NBTString) o;
        return Objects.equals(data, nbtString.data);
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(data);
    }
    
    @Override
    public String asString(){
        return this.data;
    }
}
