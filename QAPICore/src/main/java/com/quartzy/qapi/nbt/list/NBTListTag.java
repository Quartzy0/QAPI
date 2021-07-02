package com.quartzy.qapi.nbt.list;

import com.quartzy.qapi.nbt.NBTBase;

import java.util.List;
import java.util.Objects;

public class NBTListTag extends NBTList<NBTBase>{
    private final List<NBTBase> tags;
    private final byte type;
    
    public NBTListTag(List<NBTBase> tags, byte type){
        this.tags = tags;
        this.type = type;
    }
    
    public List<NBTBase> getTags(){
        return tags;
    }
    
    @Override
    public NBTBase get(int index){
        return this.tags.get(index);
    }
    
    @Override
    public NBTBase set(int index, NBTBase val){
        if(val==null)return this.remove(index);
        NBTBase previousVal = this.get(index);
        if(checkTypes(val, type)){
            this.tags.set(index, val);
        }else{
            throw new UnsupportedOperationException("Trying to set a tag of a different type than the one of the list (" + type + ")");
        }
        return previousVal;
    }
    
    @Override
    public NBTBase remove(int index){
        return this.tags.remove(index);
    }
    
    @Override
    public void insert(int index, NBTBase val){
        if(checkTypes(val, type)){
            this.tags.add(index, val);
        }else{
            throw new UnsupportedOperationException("Trying to set a tag of a different type than the one of the list (" + type + ")");
        }
    }
    
    @Override
    public boolean add(NBTBase val){
        if(checkTypes(val, type)){
            this.tags.add(val);
        }else{
            throw new UnsupportedOperationException("Trying to set a tag of a different type than the one of the list (" + type + ")");
        }
        return true;
    }
    
    @Override
    public int size(){
        return this.tags.size();
    }
    
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        if(!super.equals(o)) return false;
        NBTListTag nbtBases = (NBTListTag) o;
        return type == nbtBases.type && Objects.equals(tags, nbtBases.tags);
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(super.hashCode(), tags, type);
    }
    
    @Override
    public String asString(){
        StringBuilder builder = new StringBuilder();
        builder.append("[");
    
        for(int i = 0; i < this.tags.size(); i++){
            builder.append(this.tags.get(i).asString());
            if(i!=this.tags.size()-1){
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }
    
    @Override
    public String toString(){
        return "NBTListTag{" +
                "tags=" + tags +
                ", type=" + type +
                '}';
    }
}
