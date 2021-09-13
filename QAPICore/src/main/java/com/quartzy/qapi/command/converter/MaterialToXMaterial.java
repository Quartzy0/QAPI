package com.quartzy.qapi.command.converter;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;

public class MaterialToXMaterial implements TypeConverter<XMaterial, Material>{
    @Override
    public XMaterial convert(Object obj){
        if(!(obj instanceof Material))return null;
        return XMaterial.matchXMaterial((Material) obj);
    }
    
    @Override
    public Class<Material> getTypeIn(){
        return Material.class;
    }
    
    @Override
    public Class<XMaterial> getTypeOut(){
        return XMaterial.class;
    }
}
