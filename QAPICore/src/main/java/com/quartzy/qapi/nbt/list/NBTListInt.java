package com.quartzy.qapi.nbt.list;

import com.quartzy.qapi.nbt.numbers.NBTInt;
import org.apache.commons.lang.ArrayUtils;

public class NBTListInt extends NBTList<NBTInt>{
    private int[] values;
    
    public NBTListInt(int[] values){
        this.values = values;
    }
    
    public int[] getValues(){
        return values;
    }
    
    @Override
    public NBTInt set(int index, NBTInt val){
        checkBounds(index);
        int prev = values[index];
        values[index] = val.asInt();
        return new NBTInt(prev);
    }
    
    @Override
    public NBTInt remove(int index){
        checkBounds(index);
        int prev = values[index];
        values = ArrayUtils.remove(values, index);
        return new NBTInt(prev);
    }
    
    @Override
    public void insert(int index, NBTInt val){
        checkBounds(index);
        values = ArrayUtils.add(values, index, val.asInt());
    }
    
    @Override
    public int size(){
        return values.length;
    }
    
    @Override
    public boolean add(NBTInt val){
        values = ArrayUtils.add(values, val.asInt());
        return true;
    }
    
    @Override
    public NBTInt get(int index){
        checkBounds(index);
        return new NBTInt(values[index]);
    }
    
    private void checkBounds(int i){
        if(i<0 || i>=this.size())
            throw new IndexOutOfBoundsException("i (" + i + ") is out of bounds! i<0 || i>size(" + this.size() + ")");
    }
}
