package com.quartzy.qapi.nbt;

import com.google.common.collect.Maps;
import com.quartzy.qapi.QAPI;
import com.quartzy.qapi.nbt.list.*;
import com.quartzy.qapi.nbt.numbers.*;

import java.util.*;

public class NBTCompound implements NBTBase{
    private final Map<String, NBTBase> tags;
    
    public NBTCompound(Map<String, NBTBase> tags){
        this.tags = tags;
    }
    
    public NBTCompound(){
        this(Maps.newHashMap());
    }
    
    public Set<String> getKeys(){
        return this.tags.keySet();
    }
    
    public void set(String name, NBTBase tag){
        this.tags.put(name, tag);
    }
    
    public void setInt(String name, int val){
        this.set(name, new NBTInt(val));
    }
    
    public void setLong(String name, long val){
        this.set(name, new NBTLong(val));
    }
    
    public void setShort(String name, short val){
        this.set(name, new NBTShort(val));
    }
    
    public void setByte(String name, byte val){
        this.set(name, new NBTByte(val));
    }
    
    public void setFloat(String name, float val){
        this.set(name, new NBTFloat(val));
    }
    
    public void setDouble(String name, double val){
        this.set(name, new NBTDouble(val));
    }
    
    public void setString(String name, String val){
        this.set(name, new NBTString(val));
    }
    
    public void setUUID(String name, UUID uuid){
        this.set(name, QAPI.nbtProvider().serializeUUID(uuid));
    }
    
    public void setByteArray(String name, byte... val){
        this.set(name, new NBTListByte(val));
    }
    
    public void setLongArray(String name, long... val){
        this.set(name, new NBTListLong(val));
    }
    
    public void setIntArray(String name, int... val){
        this.set(name, new NBTListInt(val));
    }
    
    public void setTagArray(String name, NBTBase... val){
        if(val!=null && val.length!=0)
            this.set(name, new NBTListTag(Arrays.asList(val), QAPI.nbtProvider().getTagId(val[0].getClass())));
    }
    
    public NBTBase get(String name){
        return this.tags.get(name);
    }
    
    public int getInt(String name){
        try{
            if(hasKeyOfType(name, NBTInt.class))
                return ((NBTNumber)this.tags.get(name)).asInt();
        }catch(ClassCastException e){
            //Ignore
        }
        return 0;
    }
    
    public long getLong(String name){
        try{
            if(hasKeyOfType(name, NBTNumber.class))
                return ((NBTNumber)this.tags.get(name)).asLong();
        }catch(ClassCastException e){
            //Ignore
        }
        return 0L;
    }
    
    public short getShort(String name){
        try{
            if(hasKeyOfType(name, NBTNumber.class))
                return ((NBTNumber)this.tags.get(name)).asShort();
        }catch(ClassCastException e){
            //Ignore
        }
        return 0;
    }
    
    public byte getByte(String name){
        try{
            if(hasKeyOfType(name, NBTNumber.class))
                return ((NBTNumber)this.tags.get(name)).asByte();
        }catch(ClassCastException e){
            //Ignore
        }
        return 0x0b;
    }
    
    public float getFloat(String name){
        try{
            if(hasKeyOfType(name, NBTNumber.class))
                return ((NBTNumber)this.tags.get(name)).asFloat();
        }catch(ClassCastException e){
            //Ignore
        }
        return 0.0f;
    }
    
    public double getDouble(String name){
        try{
            if(hasKeyOfType(name, NBTNumber.class))
                return ((NBTNumber)this.tags.get(name)).asDouble();
        }catch(ClassCastException e){
            //Ignore
        }
        return 0.0D;
    }
    
    public String getString(String name){
        try{
            if(hasKeyOfType(name, NBTString.class))
                return ((NBTString)this.tags.get(name)).getString();
        }catch(ClassCastException e){
            //Ignore
        }
        return "";
    }
    
    public UUID getUUID(String name){
        try{
            if(hasKeyOfType(name, NBTListInt.class))
                return QAPI.nbtProvider().deserializeUUID((NBTListInt) this.tags.get(name));
        } catch(ClassCastException e){
            //Ignore
        }
        return new UUID(0, 0);
    }
    
    public NBTListByte getByteArray(String name){
        try{
            if(hasKeyOfType(name, NBTListByte.class))
                return ((NBTListByte) this.tags.get(name));
        } catch(ClassCastException e){
            //Ignore
        }
        return new NBTListByte(new byte[]{0});
    }
    
    public NBTListInt getIntArray(String name){
        try{
            if(hasKeyOfType(name, NBTListInt.class))
                return ((NBTListInt) this.tags.get(name));
        } catch(ClassCastException e){
            //Ignore
        }
        return new NBTListInt(new int[]{0});
    }
    
    public NBTListLong getLongArray(String name){
        try{
            if(hasKeyOfType(name, NBTListLong.class))
                return ((NBTListLong) this.tags.get(name));
        } catch(ClassCastException e){
            //Ignore
        }
        return new NBTListLong(new long[]{0});
    }
    
    public NBTListTag getTagArray(String name, byte type){
        try{
            if(hasKeyOfType(name, NBTListTag.class))
                return ((NBTListTag) this.tags.get(name));
        } catch(ClassCastException e){
            //Ignore
        }
        return new NBTListTag(new ArrayList<>(), type);
    }
    
    public NBTListTag getTagArray(String name, Class<? extends NBTBase> type){
        return this.getTagArray(name, QAPI.nbtProvider().getTagId(type));
    }
    
    public boolean hasKey(String name){
        return this.tags.containsKey(name);
    }
    
    public boolean hasKeyOfType(String name, byte type){
        NBTBase nbtBase = this.tags.get(name);
        if(nbtBase==null)return false;
        return QAPI.nbtProvider().getTagId(nbtBase.getClass())==type;
    }
    
    public boolean hasKeyOfType(String name, Class<? extends NBTBase> type){
        NBTBase nbtBase = this.tags.get(name);
        if(nbtBase==null)return false;
        return type.isAssignableFrom(nbtBase.getClass());
    }
    
    Map<String, NBTBase> getTags(){
        return tags;
    }
    
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        NBTCompound that = (NBTCompound) o;
        return Objects.equals(tags, that.tags);
    }
    
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("NBTCompound{");
        boolean first = true;
        for(Map.Entry<String, NBTBase> entry : this.tags.entrySet()){
            if(!first){
                builder.append(", ");
            }else{
                first = false;
            }
            builder.append(entry.getKey());
            builder.append(": ");
            builder.append(entry.getValue().toString());
        }
        builder.append('}');
        return builder.toString();
    }
    
    @Override
    public String asString(){
        StringBuilder builder = new StringBuilder();
        builder.append('{');
        boolean first = true;
        for(Map.Entry<String, NBTBase> entry : this.tags.entrySet()){
            if(!first){
                builder.append(", ");
            }else{
                first = false;
            }
            builder.append(entry.getKey());
            builder.append(": ");
            builder.append(entry.getValue().asString());
        }
        builder.append('}');
        return builder.toString();
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(tags);
    }
}
