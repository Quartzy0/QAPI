package com.quartzy.qapi.nbt.numbers;

import java.util.Objects;

public class NBTFloat extends NBTNumber{
    private final float data;
    
    public NBTFloat(float data){
        this.data = data;
    }
    
    @Override
    public long asLong(){
        return (long) data;
    }
    
    @Override
    public int asInt(){
        return (int) data;
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
    public String toString(){
        return "NBTFloat{" +
                "data=" + data +
                '}';
    }
    
    @Override
    public String asString(){
        return this.data + "f";
    }
    
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        NBTFloat nbtFloat = (NBTFloat) o;
        return Float.compare(nbtFloat.data, data) == 0;
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(data);
    }
    
    @Override
    public Number asNumber(){
        return data;
    }
}
