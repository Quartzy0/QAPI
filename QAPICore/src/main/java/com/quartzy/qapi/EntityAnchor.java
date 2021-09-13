package com.quartzy.qapi;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.function.BiFunction;

public enum EntityAnchor{
    FEET("feet", (vec3D, entity) -> vec3D),
    EYES("eyes", (vec3D, entity) -> {
        return new Location(vec3D.getWorld(), vec3D.getX(), vec3D.getY() + entity.getHeight(), vec3D.getZ(), vec3D.getYaw(), vec3D.getPitch());
    });
    
    public final String name;
    private final BiFunction<Location, Entity, Location> func;
    
    EntityAnchor(String name, BiFunction<Location, Entity, Location> func){
        this.name = name;
        this.func = func;
    }
    
    public Location getLocation(Entity entity) {
        return func.apply(entity.getLocation(), entity);
    }
}
