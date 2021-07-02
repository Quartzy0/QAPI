package com.quartzy.qapi;

import com.google.common.collect.Maps;

import java.util.Map;

public class Effect{
    private final Map<Attribute, AttributeModifier> attributeModifierMap = Maps.newHashMap();
    private final EffectType type;
    private final int liquidColor;
    private String name;
    
    public Effect(EffectType type, int liquidColor, String name){
        this.type = type;
        this.liquidColor = liquidColor;
        this.name = name;
    }
    
    public Map<Attribute, AttributeModifier> getAttributeModifierMap(){
        return attributeModifierMap;
    }
    
    public EffectType getType(){
        return type;
    }
    
    public int getLiquidColor(){
        return liquidColor;
    }
    
    public String getName(){
        return name;
    }
}
