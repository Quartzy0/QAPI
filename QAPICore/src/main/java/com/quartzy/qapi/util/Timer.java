package com.quartzy.qapi.util;

public class Timer{
    private static long start;
    
    public static void start(){
        start = System.currentTimeMillis();
    }
    
    public static void end(){
        long l = System.currentTimeMillis() - start;
        System.out.println("Took " + (l/1000f) + " s (" + l + " ms)");
    }
}
