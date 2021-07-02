package com.quartzy.qapi.nbt.list;

import com.quartzy.qapi.nbt.numbers.NBTByte;
import org.apache.commons.lang.ArrayUtils;

public class NBTListByte extends NBTList<NBTByte>{
    private byte[] values;
    
    public NBTListByte(byte[] values){
        this.values = values;
    }
    
    public byte[] getValues(){
        return values;
    }
    
    @Override
    public NBTByte set(int index, NBTByte val){
        checkBounds(index);
        byte prev = values[index];
        values[index] = val.asByte();
        return new NBTByte(prev);
    }
    
    @Override
    public NBTByte remove(int index){
        checkBounds(index);
        byte prev = values[index];
        values = ArrayUtils.remove(values, index);
        return new NBTByte(prev);
    }
    
    @Override
    public void insert(int index, NBTByte val){
        checkBounds(index);
        values = ArrayUtils.add(values, index, val.asByte());
    }
    
    @Override
    public int size(){
        return values.length;
    }
    
    @Override
    public boolean add(NBTByte val){
        values = ArrayUtils.add(values, val.asByte());
        return true;
    }
    
    @Override
    public NBTByte get(int index){
        checkBounds(index);
        return new NBTByte(values[index]);
    }
    
    private void checkBounds(int i){
        if(i<0 || i>=this.size())
            throw new IndexOutOfBoundsException("i (" + i + ") is out of bounds! i<0 || i>size(" + this.size() + ")");
    }
}
