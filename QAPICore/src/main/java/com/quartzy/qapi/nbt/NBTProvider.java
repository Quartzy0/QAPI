package com.quartzy.qapi.nbt;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface NBTProvider{
    
    byte getTagId(Class<? extends NBTBase> aCass);
    
    Class<? extends NBTBase> getNBTClass(byte type);
    
    NBTBase serializeUUID(UUID uuid);
    
    UUID deserializeUUID(NBTBase uuid);
    
    ItemStack applyNBTToItem(ItemStack itemStack, NBTCompound tag);
    
    void applyNBTToEntity(Entity entity, NBTCompound tag);
    
    NBTCompound getNBTFromItem(ItemStack itemStack);
    
    NBTCompound getNBTFromEntity(Entity entity);
}
