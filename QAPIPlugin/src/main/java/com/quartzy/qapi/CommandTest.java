package com.quartzy.qapi;

import com.quartzy.qapi.command.Command;

import java.util.Objects;
import java.util.function.Predicate;

public abstract class CommandTest extends Command{
    private int testsFinished = 0;
    
    protected final void finishedTest(){
        testsFinished++;
    }
    
    public int getTestsFinished(){
        return testsFinished;
    }
    
    protected final <T> void assertEquals(Predicate<T> a, T b, String message){
        if(a!=null && a.test(b))return;
        throw new AssertionError(message);
    }
    
    protected final <T> void assertEquals(Predicate<T> a, T b){
        if(a!=null && a.test(b))return;
        throw new AssertionError("Assertion failed. Predicate did not test positively with object " + b);
    }
    
    protected final void assertEquals(Object a, Object b, String message){
        if(Objects.deepEquals(a, b))return;
        throw new AssertionError(message);
    }
    
    protected final void assertEquals(Object a, Object b){
        if(Objects.deepEquals(a, b))return;
        throw new AssertionError("Assertion failed. Objects " + a + " and " + b + " are not equal");
    }
    
    protected final void assertRange(int i, int min, int max){
        if(i > min && i < max)return;
        throw new AssertionError("Integer " + i + " is out of range (min: " + min + ", max: " + max + ")");
    }
    
    protected final void assertRange(int i, int min, int max, String message){
        if(i > min && i < max)return;
        throw new AssertionError(message);
    }
}
