package com.quartzy.qapi;

import com.google.common.collect.Maps;
import org.apache.commons.lang.SystemUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public enum EntityAnchor{
    FEET("feet", (vec3D, entity) -> vec3D),
    EYES("eyes", (vec3D, entity) -> {
        return new Location(vec3D.getWorld(), vec3D.getX(), vec3D.getY() + entity.getHeight(), vec3D.getZ(), vec3D.getYaw(), vec3D.getPitch());
    });
    private static final Map<String, EntityAnchor> BY_NAME = new HashMap<>();
    
    private final String name;
    private final BiFunction<Location, Entity, Location> func;
    
    EntityAnchor(String name, BiFunction<Location, Entity, Location> func){
        this.name = name;
        this.func = func;
    }
    
    public Location getLocation(Entity entity) {
        return func.apply(entity.getLocation(), entity);
    }
    
    static {
        for(EntityAnchor value : EntityAnchor.values()){
            BY_NAME.put(value.name, value);
        }
    }
}
