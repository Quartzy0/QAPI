package com.quartzy.qapi;

import java.util.Objects;

public class Vec2I{
    private int x, y;
    
    public Vec2I(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    @Override
    public String toString(){
        return "Vec2I{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
    
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Vec2I vec2I = (Vec2I) o;
        return x == vec2I.x && y == vec2I.y;
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(x, y);
    }
    
    public int getX(){
        return x;
    }
    
    public Vec2I setX(int x){
        this.x = x;
        return this;
    }
    
    public int getY(){
        return y;
    }
    
    public Vec2I setY(int y){
        this.y = y;
        return this;
    }
}
