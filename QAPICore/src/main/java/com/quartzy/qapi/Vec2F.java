package com.quartzy.qapi;

import java.util.Objects;

public class Vec2F{
    
    public float x, y;
    
    @Override
    public String toString(){
        return "Vec2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
    
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Vec2F vec2F = (Vec2F) o;
        return x == vec2F.x && y == vec2F.y;
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(x, y);
    }
    
    public float getX(){
        return x;
    }
    
    public Vec2F setX(int x){
        this.x = x;
        return this;
    }
    
    public float getY(){
        return y;
    }
    
    public Vec2F setY(int y){
        this.y = y;
        return this;
    }
    
    public Vec2F(int x, int y){
        this.x = x;
        this.y = y;
    }
}
