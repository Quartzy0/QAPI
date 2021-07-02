package com.quartzy.qapi.nbt.numbers;

import java.util.Objects;

public class NBTLong extends NBTNumber{
    private final long data;
    
    public NBTLong(long data){
        this.data = data;
    }
    
    @Override
    public long asLong(){
        return data;
    }
    
    @Override
    public int asInt(){
        return (int) (data);
    }
    
    @Override
    public short asShort(){
        return (short) data;
    }
    
    @Override
    public byte asByte(){
        return (byte) data;
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
        return "NBTLong{" +
                "data=" + data +
                '}';
    }
    
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        NBTLong nbtLong = (NBTLong) o;
        return data == nbtLong.data;
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(data);
    }
    
    @Override
    public String asString(){
        return this.data + "l";
    }
}
