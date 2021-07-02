package com.quartzy.qapi;

import java.util.Objects;

public class Vec3I{
    
    public int x, y, z;
    
    @Override
    public String toString(){
        return "Vec3{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
    
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Vec3I vec3I = (Vec3I) o;
        return x == vec3I.x && y == vec3I.y && z == vec3I.z;
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(x, y, z);
    }
    
    public Vec3I setX(int x){
        this.x = x;
        return this;
    }
    
    public Vec3I setY(int y){
        this.y = y;
        return this;
    }
    
    public Vec3I setZ(int z){
        this.z = z;
        return this;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public int getZ(){
        return z;
    }
    
    public Vec3I(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
