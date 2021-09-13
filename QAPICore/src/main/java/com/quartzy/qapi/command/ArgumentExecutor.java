package com.quartzy.qapi.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArgumentExecutor{
    String value();
    
    int permissionLevel() default 4;
    
    String permission() default "";
}
