package com.quartzy.qapi.nbt.list;

import com.quartzy.qapi.nbt.numbers.NBTLong;
import org.apache.commons.lang.ArrayUtils;

public class NBTListLong extends NBTList<NBTLong>{
    private long[] values;
    
    public NBTListLong(long[] values){
        this.values = values;
    }
    
    public long[] getValues(){
        return values;
    }
    
    @Override
    public NBTLong set(int index, NBTLong val){
        checkBounds(index);
        long prev = values[index];
        values[index] = val.asLong();
        return new NBTLong(prev);
    }
    
    @Override
    public NBTLong remove(int index){
        checkBounds(index);
        long prev = values[index];
        values = ArrayUtils.remove(values, index);
        return new NBTLong(prev);
    }
    
    @Override
    public void insert(int index, NBTLong val){
        checkBounds(index);
        values = ArrayUtils.add(values, index, val.asLong());
    }
    
    @Override
    public int size(){
        return values.length;
    }
    
    @Override
    public boolean add(NBTLong val){
        values = ArrayUtils.add(values, val.asLong());
        return true;
    }
    
    @Override
    public NBTLong get(int index){
        checkBounds(index);
        return new NBTLong(values[index]);
    }
    
    private void checkBounds(int i){
        if(i<0 || i>=this.size())
            throw new IndexOutOfBoundsException("i (" + i + ") is out of bounds! i<0 || i>size(" + this.size() + ")");
    }
}
