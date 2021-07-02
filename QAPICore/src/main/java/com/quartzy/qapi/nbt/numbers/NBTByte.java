package com.quartzy.qapi.nbt.numbers;

import java.util.Objects;

public class NBTByte extends NBTNumber{
    private final byte data;
    
    public NBTByte(byte data){
        this.data = data;
    }
    
    @Override
    public long asLong(){
        return data;
    }
    
    @Override
    public int asInt(){
        return data;
    }
    
    @Override
    public short asShort(){
        return data;
    }
    
    @Override
    public byte asByte(){
        return data;
    }
    
    @Override
    public double asDouble(){
        return data;
    }
    
    @Override
    public float asFloat(){
        return data;
    }
    
    @Override
    public Number asNumber(){
        return data;
    }
    
    @Override
    public String toString(){
        return "NBTByte{" +
                "data=" + data +
                '}';
    }
    
    @Override
    public String asString(){
        return this.data + "b";
    }
    
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        NBTByte nbtByte = (NBTByte) o;
        return data == nbtByte.data;
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(data);
    }
}
