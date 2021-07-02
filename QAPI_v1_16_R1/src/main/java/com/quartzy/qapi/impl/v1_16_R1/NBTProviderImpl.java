package com.quartzy.qapi.impl.v1_16_R1;

import com.quartzy.qapi.nbt.*;
import com.quartzy.qapi.nbt.NBTBase;
import com.quartzy.qapi.nbt.list.NBTListByte;
import com.quartzy.qapi.nbt.list.NBTListInt;
import com.quartzy.qapi.nbt.list.NBTListLong;
import com.quartzy.qapi.nbt.list.NBTListTag;
import com.quartzy.qapi.nbt.numbers.*;
import com.quartzy.qapi.nbt.numbers.NBTNumber;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.UUID;

public class NBTProviderImpl implements NBTProvider{
    @Override
    public byte getTagId(Class<? extends NBTBase> aCass){
        if(NBTInt.class.isAssignableFrom(aCass)){
            return CraftMagicNumbers.NBT.TAG_INT;
        }else if(NBTByte.class.isAssignableFrom(aCass)){
            return CraftMagicNumbers.NBT.TAG_BYTE;
        }else if(NBTLong.class.isAssignableFrom(aCass)){
            return CraftMagicNumbers.NBT.TAG_LONG;
        }else if(NBTShort.class.isAssignableFrom(aCass)){
            return CraftMagicNumbers.NBT.TAG_SHORT;
        }else if(NBTDouble.class.isAssignableFrom(aCass)){
            return CraftMagicNumbers.NBT.TAG_DOUBLE;
        }else if(NBTFloat.class.isAssignableFrom(aCass)){
            return CraftMagicNumbers.NBT.TAG_FLOAT;
        }else if(NBTCompound.class.isAssignableFrom(aCass)){
            return CraftMagicNumbers.NBT.TAG_COMPOUND;
        }else if(NBTString.class.isAssignableFrom(aCass)){
            return CraftMagicNumbers.NBT.TAG_STRING;
        }else if(NBTListByte.class.isAssignableFrom(aCass)){
            return CraftMagicNumbers.NBT.TAG_BYTE_ARRAY;
        }else if(NBTListInt.class.isAssignableFrom(aCass)){
            return CraftMagicNumbers.NBT.TAG_INT_ARRAY;
        }else if(NBTListTag.class.isAssignableFrom(aCass)){
            return CraftMagicNumbers.NBT.TAG_LIST;
        }else if(NBTListLong.class.isAssignableFrom(aCass)){
            return 12; //For some reason not listed in CraftMagicNumbers.NBT
        }else if(NBTEnd.class.isAssignableFrom(aCass)){
            return CraftMagicNumbers.NBT.TAG_END;
        }else if(NBTNumber.class.isAssignableFrom(aCass)){
            return CraftMagicNumbers.NBT.TAG_ANY_NUMBER;
        }
        return -1;
    }
    
    @Override
    public Class<? extends NBTBase> getNBTClass(byte type){
        switch(type){
            case CraftMagicNumbers.NBT.TAG_INT:
                return NBTInt.class;
            case CraftMagicNumbers.NBT.TAG_BYTE:
                return NBTByte.class;
            case CraftMagicNumbers.NBT.TAG_SHORT:
                return NBTShort.class;
            case CraftMagicNumbers.NBT.TAG_STRING:
                return NBTString.class;
            case CraftMagicNumbers.NBT.TAG_LONG:
                return NBTLong.class;
            case CraftMagicNumbers.NBT.TAG_FLOAT:
                return NBTFloat.class;
            case CraftMagicNumbers.NBT.TAG_DOUBLE:
                return NBTDouble.class;
            case CraftMagicNumbers.NBT.TAG_COMPOUND:
                return NBTCompound.class;
            case CraftMagicNumbers.NBT.TAG_BYTE_ARRAY:
                return NBTListByte.class;
            case CraftMagicNumbers.NBT.TAG_INT_ARRAY:
                return NBTListInt.class;
            case 12: //For some reason not listed in CraftMagicNumbers.NBT
                return NBTListLong.class;
            case CraftMagicNumbers.NBT.TAG_END:
                return NBTEnd.class;
            case CraftMagicNumbers.NBT.TAG_LIST:
                return NBTListTag.class;
            case CraftMagicNumbers.NBT.TAG_ANY_NUMBER:
                return NBTNumber.class;
        }
        return NBTBase.class;
    }
    
    @Override
    public NBTBase serializeUUID(UUID uuid){
        NBTTagIntArray a = GameProfileSerializer.a(uuid);
        return new NBTListInt(a.getInts());
    }
    
    @Override
    public UUID deserializeUUID(NBTBase uuid){
        if(!(uuid instanceof NBTListInt))return null;
        NBTTagIntArray a = new NBTTagIntArray(((NBTListInt) uuid).getValues());
        return GameProfileSerializer.a(a);
    }
    
    @Override
    public ItemStack applyNBTToItem(ItemStack itemStack, NBTCompound tag){
        net.minecraft.server.v1_16_R1.ItemStack itemStack1 = CraftItemStack.asNMSCopy(itemStack);
        itemStack1.setTag(((NBTTagCompound) toNMSTag(tag)));
        return CraftItemStack.asBukkitCopy(itemStack1);
    }
    
    @Override
    public void applyNBTToEntity(Entity entity, NBTCompound tag){
        net.minecraft.server.v1_16_R1.Entity handle = ((CraftEntity) entity).getHandle();
        NBTTagCompound compound = (NBTTagCompound) toNMSTag(tag);
        handle.load(compound);
    }
    
    @Override
    public NBTCompound getNBTFromItem(ItemStack itemStack){
        net.minecraft.server.v1_16_R1.ItemStack itemStack1 = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = itemStack1.getOrCreateTag();
        return (NBTCompound) fromNMS(tag);
    }
    
    @Override
    public NBTCompound getNBTFromEntity(Entity entity){
        net.minecraft.server.v1_16_R1.Entity handle = ((CraftEntity) entity).getHandle();
        NBTTagCompound tagNMS = new NBTTagCompound();
        handle.save(tagNMS);
        return (NBTCompound) fromNMS(tagNMS);
    }
    
    private net.minecraft.server.v1_16_R1.NBTBase toNMSTag(NBTBase tag){
        if(tag==null)return null;
        switch(getTagId(tag.getClass())){
            case CraftMagicNumbers.NBT.TAG_INT:
            case CraftMagicNumbers.NBT.TAG_ANY_NUMBER:
                return NBTTagInt.a(((NBTNumber) tag).asInt());
            case CraftMagicNumbers.NBT.TAG_BYTE:
                return NBTTagByte.a(((NBTNumber) tag).asByte());
            case CraftMagicNumbers.NBT.TAG_SHORT:
                return NBTTagShort.a(((NBTNumber) tag).asShort());
            case CraftMagicNumbers.NBT.TAG_STRING:
                return NBTTagString.a(((NBTString) tag).getString());
            case CraftMagicNumbers.NBT.TAG_LONG:
                return NBTTagLong.a(((NBTNumber) tag).asLong());
            case CraftMagicNumbers.NBT.TAG_FLOAT:
                return NBTTagFloat.a(((NBTNumber) tag).asFloat());
            case CraftMagicNumbers.NBT.TAG_DOUBLE:
                return NBTTagDouble.a(((NBTNumber) tag).asDouble());
            case CraftMagicNumbers.NBT.TAG_COMPOUND:
                NBTTagCompound compound = new NBTTagCompound();
                NBTCompound nbtCompound = (NBTCompound) tag;
                for(String key : nbtCompound.getKeys()){
                    compound.set(key, toNMSTag(nbtCompound.get(key)));
                }
                return compound;
            case CraftMagicNumbers.NBT.TAG_BYTE_ARRAY:
                return new NBTTagByteArray(((NBTListByte) tag).getValues());
            case CraftMagicNumbers.NBT.TAG_INT_ARRAY:
                return new NBTTagIntArray(((NBTListInt) tag).getValues());
            case 12: //For some reason not listed in CraftMagicNumbers.NBT
                return new NBTTagLongArray(((NBTListLong) tag).getValues());
            case CraftMagicNumbers.NBT.TAG_END:
                return NBTTagEnd.b;
            case CraftMagicNumbers.NBT.TAG_LIST:
                NBTTagList listTag = new NBTTagList();
                NBTListTag list = (NBTListTag) tag;
                for(NBTBase key : list){
                    listTag.add(toNMSTag(key));
                }
                return listTag;
        }
        return NBTTagEnd.b;
    }
    
    private NBTBase fromNMS(net.minecraft.server.v1_16_R1.NBTBase base){
        if(base==null)return null;
        switch(base.getTypeId()){
            case CraftMagicNumbers.NBT.TAG_BYTE:
                return new NBTByte(((net.minecraft.server.v1_16_R1.NBTNumber) base).asByte());
            case CraftMagicNumbers.NBT.TAG_SHORT:
                return new NBTShort(((net.minecraft.server.v1_16_R1.NBTNumber) base).asShort());
            case CraftMagicNumbers.NBT.TAG_INT:
                return new NBTInt(((net.minecraft.server.v1_16_R1.NBTNumber) base).asInt());
            case CraftMagicNumbers.NBT.TAG_LONG:
                return new NBTLong(((net.minecraft.server.v1_16_R1.NBTNumber) base).asLong());
            case CraftMagicNumbers.NBT.TAG_FLOAT:
                return new NBTFloat(((net.minecraft.server.v1_16_R1.NBTNumber) base).asFloat());
            case CraftMagicNumbers.NBT.TAG_DOUBLE:
                return new NBTDouble(((net.minecraft.server.v1_16_R1.NBTNumber) base).asDouble());
            case CraftMagicNumbers.NBT.TAG_STRING:
                return new NBTString(base.asString());
            case CraftMagicNumbers.NBT.TAG_BYTE_ARRAY:
                return new NBTListByte(((NBTTagByteArray) base).getBytes());
            case CraftMagicNumbers.NBT.TAG_INT_ARRAY:
                return new NBTListInt(((NBTTagIntArray) base).getInts());
            case 12: //For some reason not listed in CraftMagicNumbers.NBT
                return new NBTListLong(((NBTTagLongArray) base).getLongs());
            case CraftMagicNumbers.NBT.TAG_LIST:
                NBTTagList nbtList = (NBTTagList) base;
                NBTListTag nbtListTag = new NBTListTag(Collections.emptyList(), nbtList.d_());
                for(net.minecraft.server.v1_16_R1.NBTBase nbtBase : nbtList){
                    nbtListTag.add(fromNMS(nbtBase));
                }
                return nbtListTag;
            case CraftMagicNumbers.NBT.TAG_COMPOUND:
                NBTTagCompound nbtTagCompound = (NBTTagCompound) base;
                NBTCompound nbtCompound = new NBTCompound();
                for(String key : nbtTagCompound.getKeys()){
                    nbtCompound.set(key, fromNMS(nbtTagCompound.get(key)));
                }
                return nbtCompound;
            case CraftMagicNumbers.NBT.TAG_END:
                return new NBTEnd();
        }
        return new NBTEnd();
    }
}
