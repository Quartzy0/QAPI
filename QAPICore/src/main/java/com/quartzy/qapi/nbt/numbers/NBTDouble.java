package com.quartzy.qapi.nbt.numbers;

import java.util.Objects;

public class NBTDouble extends NBTNumber{
    private final double data;
    
    public NBTDouble(double data){
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
        return (float) data;
    }
    
    @Override
    public String toString(){
        return "NBTDouble{" +
                "data=" + data +
                '}';
    }
    
    @Override
    public String asString(){
        return this.data + "d";
    }
    
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        NBTDouble nbtDouble = (NBTDouble) o;
        return Double.compare(nbtDouble.data, data) == 0;
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
