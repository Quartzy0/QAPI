package com.quartzy.qapi.command.converter;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialToItemStack implements TypeConverter<ItemStack, Material>{
    
    @Override
    public ItemStack convert(Object obj){
        if(!(obj instanceof Material))return null;
        return new ItemStack((Material) obj, 1);
    }
    
    @Override
    public Class<Material> getTypeIn(){
        return Material.class;
    }
    
    @Override
    public Class<ItemStack> getTypeOut(){
        return ItemStack.class;
    }
}
