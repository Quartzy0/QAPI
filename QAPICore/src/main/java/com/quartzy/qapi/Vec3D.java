package com.quartzy.qapi;

import java.util.Objects;

public class Vec3D{
    private double x, y, z;
    
    @Override
    public String toString(){
        return "Vec3D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
    
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Vec3D vec3D = (Vec3D) o;
        return Double.compare(vec3D.x, x) == 0 && Double.compare(vec3D.y, y) == 0 && Double.compare(vec3D.z, z) == 0;
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(x, y, z);
    }
    
    public Vec3D setX(double x){
        this.x = x;
        return this;
    }
    
    public Vec3D setY(double y){
        this.y = y;
        return this;
    }
    
    public Vec3D setZ(double z){
        this.z = z;
        return this;
    }
    
    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
    
    public double getZ(){
        return z;
    }
    
    public Vec3D(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
