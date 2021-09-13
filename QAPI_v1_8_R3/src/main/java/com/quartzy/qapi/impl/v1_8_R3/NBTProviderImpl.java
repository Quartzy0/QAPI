package com.quartzy.qapi.impl.v1_8_R3;

import com.quartzy.qapi.nbt.NBTBase;
import com.quartzy.qapi.nbt.*;
import com.quartzy.qapi.nbt.list.NBTListByte;
import com.quartzy.qapi.nbt.list.NBTListInt;
import com.quartzy.qapi.nbt.list.NBTListLong;
import com.quartzy.qapi.nbt.list.NBTListTag;
import com.quartzy.qapi.nbt.numbers.*;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.UUID;

public class NBTProviderImpl implements NBTProvider{
    private static Constructor<NBTTagEnd> nbtEndConstructor;
    
    @Override
    public byte getTagId(Class<? extends NBTBase> aCass){
        if(NBTInt.class.isAssignableFrom(aCass)){
            return 3;
        }else if(NBTByte.class.isAssignableFrom(aCass)){
            return 1;
        }else if(NBTLong.class.isAssignableFrom(aCass)){
            return 4;
        }else if(NBTShort.class.isAssignableFrom(aCass)){
            return 2;
        }else if(NBTDouble.class.isAssignableFrom(aCass)){
            return 6;
        }else if(NBTFloat.class.isAssignableFrom(aCass)){
            return 5;
        }else if(NBTCompound.class.isAssignableFrom(aCass)){
            return 10;
        }else if(NBTString.class.isAssignableFrom(aCass)){
            return 8;
        }else if(NBTListByte.class.isAssignableFrom(aCass)){
            return 7;
        }else if(NBTListInt.class.isAssignableFrom(aCass)){
            return 11;
        }else if(NBTListTag.class.isAssignableFrom(aCass)){
            return 9;
        }else if(NBTListLong.class.isAssignableFrom(aCass)){ //Long list did not exist in this version
            return 12;
        }else if(NBTEnd.class.isAssignableFrom(aCass)){
            return 0;
        }
        return -1;
    }
    
    @Override
    public Class<? extends NBTBase> getNBTClass(byte type){
        switch(type){
            case 3:
                return NBTInt.class;
            case 1:
                return NBTByte.class;
            case 2:
                return NBTShort.class;
            case 8:
                return NBTString.class;
            case 4:
                return NBTLong.class;
            case 5:
                return NBTFloat.class;
            case 6:
                return NBTDouble.class;
            case 10:
                return NBTCompound.class;
            case 7:
                return NBTListByte.class;
            case 11:
                return NBTListInt.class;
            case 12: //Long list did not exist in this version
                return NBTListLong.class;
            case 0:
                return NBTEnd.class;
            case 9:
                return NBTListTag.class;
        }
        return NBTBase.class;
    }
    
    @Override
    public NBTBase serializeUUID(UUID uuid){
        long var1 = uuid.getMostSignificantBits();
        long var3 = uuid.getLeastSignificantBits();
        int[] ints = {(int) (var1 >> 32), (int) var1, (int) (var3 >> 32), (int) var3};
        return new NBTListInt(ints);
    }
    
    @Override
    public UUID deserializeUUID(NBTBase uuid){
        if(!(uuid instanceof NBTListInt)) return null;
        int[] var1 = ((NBTListInt) uuid).getValues();
        if (var1.length != 4) {
            throw new IllegalArgumentException("Expected UUID-Array to be of length 4, but found " + var1.length + ".");
        } else {
            return new UUID((long)var1[0] << 32 | (long)var1[1] & 4294967295L, (long)var1[2] << 32 | (long)var1[3] & 4294967295L);
        }
    }
    
    @Override
    public NBTCompound getNBTFromEntity(Entity entity){
        net.minecraft.server.v1_8_R3.Entity handle = ((CraftEntity) entity).getHandle();
        NBTTagCompound compound = new NBTTagCompound();
        handle.e(compound);
        return (NBTCompound) fromNMS(compound);
    }
    
    @Override
    @Nonnull
    public NBTCompound getNBTFromItem(ItemStack itemStack){
        net.minecraft.server.v1_8_R3.ItemStack itemStack1 = CraftItemStack.asNMSCopy(itemStack);
        return itemStack1.getTag()==null ? new NBTCompound() : (NBTCompound) fromNMS(itemStack1.getTag());
    }
    
    @Override
    public void applyNBTToEntity(Entity entity, NBTCompound tag){
        net.minecraft.server.v1_8_R3.Entity handle = ((CraftEntity) entity).getHandle();
        NBTTagCompound compound = (NBTTagCompound) toNMSTag(tag);
        handle.f(compound);
    }
    
    @Override
    public org.bukkit.inventory.ItemStack applyNBTToItem(ItemStack itemStack, NBTCompound tag){
        net.minecraft.server.v1_8_R3.ItemStack itemStack1 = CraftItemStack.asNMSCopy(itemStack);
        itemStack1.setTag(((NBTTagCompound) toNMSTag(tag)));
        return CraftItemStack.asBukkitCopy(itemStack1);
    }
    
    private net.minecraft.server.v1_8_R3.NBTBase toNMSTag(NBTBase tag){
        if(tag==null)return null;
        switch(getTagId(tag.getClass())){
            case 3:
                return new NBTTagInt(((NBTNumber) tag).asInt());
            case 1:
                return new NBTTagByte(((NBTNumber) tag).asByte());
            case 2:
                return new NBTTagShort(((NBTNumber) tag).asShort());
            case 8:
                return new NBTTagString(((NBTString) tag).getString());
            case 4:
                return new NBTTagLong(((NBTNumber) tag).asLong());
            case 5:
                return new NBTTagFloat(((NBTNumber) tag).asFloat());
            case 6:
                return new NBTTagDouble(((NBTNumber) tag).asDouble());
            case 10:
                NBTTagCompound compound = new NBTTagCompound();
                NBTCompound nbtCompound = (NBTCompound) tag;
                for(String key : nbtCompound.getKeys()){
                    compound.set(key, toNMSTag(nbtCompound.get(key)));
                }
                return compound;
            case 7:
                return new NBTTagByteArray(((NBTListByte) tag).getValues());
            case 11:
                return new NBTTagIntArray(((NBTListInt) tag).getValues());
            case 12: //Long list did not exist in this version
                long[] values = ((NBTListLong) tag).getValues();
                int[] valuesInt = new int[values.length*2];
                for(int i = 0; i < valuesInt.length; i++){
                    valuesInt[i++] = (int) (values[i/2] >> 32);
                    valuesInt[i++] = (int) values[i/2];
                }
                return new NBTTagIntArray(valuesInt);
            case 0:
                return createEndTag();
            case 9:
                NBTTagList listTag = new NBTTagList();
                NBTListTag list = (NBTListTag) tag;
                for(NBTBase key : list){
                    listTag.add(toNMSTag(key));
                }
                return listTag;
        }
        return createEndTag();
    }
    
    public NBTBase fromNMS(net.minecraft.server.v1_8_R3.NBTBase base){
        if(base==null)return null;
        switch(base.getTypeId()){
            case 1:
                return new NBTByte(((net.minecraft.server.v1_8_R3.NBTBase.NBTNumber) base).f());
            case 2:
                return new NBTShort(((net.minecraft.server.v1_8_R3.NBTBase.NBTNumber) base).e());
            case 3:
                return new NBTInt(((net.minecraft.server.v1_8_R3.NBTBase.NBTNumber) base).d());
            case 4:
                return new NBTLong(((net.minecraft.server.v1_8_R3.NBTBase.NBTNumber) base).c());
            case 5:
                return new NBTFloat(((net.minecraft.server.v1_8_R3.NBTBase.NBTNumber) base).h());
            case 6:
                return new NBTDouble(((net.minecraft.server.v1_8_R3.NBTBase.NBTNumber) base).g());
            case 8:
                return new NBTString(((NBTTagString) base).a_());
            case 7:
                return new NBTListByte(((NBTTagByteArray) base).c());
            case 11:
                return new NBTListInt(((NBTTagIntArray) base).c());
            case 12: //Long list did not exist in this version
                int[] valuesInt = ((NBTTagIntArray) base).c();
                long[] values = new long[(int) Math.ceil(valuesInt.length/2f)];
                for(int i = 0; i < values.length; i++){
                    long l = 0;
                    if(i*2+1<values.length){
                        l = valuesInt[i * 2 + 1] & 0xFFFFFFFFL;
                    }
                    long l1 = 0;
                    if(i*2<values.length){
                        l1 = (long) valuesInt[i * 2] << 32;
                    }
                    values[i] = l1 | l;
                }
                return new NBTListLong(values);
            case 9:
                NBTTagList nbtList = (NBTTagList) base;
                NBTListTag nbtListTag = new NBTListTag(new ArrayList<>(), (byte) nbtList.f());
                for(int i = 0; i < nbtList.size(); i++){
                    nbtListTag.add(fromNMS(nbtList.g(i)));
                }
                return nbtListTag;
            case 10:
                NBTTagCompound nbtTagCompound = (NBTTagCompound) base;
                NBTCompound nbtCompound = new NBTCompound();
                for(String key : nbtTagCompound.c()){
                    nbtCompound.set(key, fromNMS(nbtTagCompound.get(key)));
                }
                return nbtCompound;
            case 0:
                return new NBTEnd();
        }
        return new NBTEnd();
    }
    
    private NBTTagEnd createEndTag(){
        try{
            if(nbtEndConstructor==null){
                nbtEndConstructor = NBTTagEnd.class.getDeclaredConstructor();
                nbtEndConstructor.setAccessible(true);
            }
            return nbtEndConstructor.newInstance();
        } catch(NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e){
            e.printStackTrace();
        }
        return null;
    }
}
