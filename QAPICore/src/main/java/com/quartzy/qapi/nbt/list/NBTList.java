package com.quartzy.qapi.nbt.list;

import com.quartzy.qapi.QAPI;
import com.quartzy.qapi.nbt.NBTBase;

import java.util.AbstractList;

public abstract class NBTList<T extends NBTBase> extends AbstractList<T> implements NBTBase{
    public NBTList() {
    }
    
    public abstract T set(int index, T val);
    
    public abstract T remove(int index);
    
    public abstract void insert(int index, T val);
    
    public abstract boolean add(T val);
    
    protected boolean checkTypes(T val, byte type){
        if(val==null)return false;
        return QAPI.nbtProvider().getTagId(val.getClass())==type;
    }
}
