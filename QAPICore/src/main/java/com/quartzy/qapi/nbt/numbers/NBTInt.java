package com.quartzy.qapi.nbt.numbers;

import java.util.Objects;

public class NBTInt extends NBTNumber{
    private final int data;
    
    public NBTInt(int data){
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
        return (short) (data & 0xffff);
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
    public String asString(){
        return this.data + "i";
    }
    
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        NBTInt nbtInt = (NBTInt) o;
        return data == nbtInt.data;
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(data);
    }
    
    @Override
    public String toString(){
        return "NBTInt{" +
                "data=" + data +
                '}';
    }
}
