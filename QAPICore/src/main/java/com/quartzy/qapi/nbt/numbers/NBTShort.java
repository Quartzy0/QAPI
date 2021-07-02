package com.quartzy.qapi.nbt.numbers;

import java.util.Objects;

public class NBTShort extends NBTNumber{
    private final short data;
    
    public NBTShort(short data){
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
        return (byte) (data & 0xff);
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
        return "NBTShort{" +
                "data=" + data +
                '}';
    }
    
    @Override
    public String asString(){
        return this.data + "s";
    }
    
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        NBTShort nbtShort = (NBTShort) o;
        return data == nbtShort.data;
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(data);
    }
}
