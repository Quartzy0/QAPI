package com.quartzy.qapi.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Argument{
    String name();
    ArgumentType type();
    
    int defaultI() default Integer.MIN_VALUE;
    long defaultL() default Long.MIN_VALUE;
    double defaultD() default Double.MIN_VALUE;
    float defaultF() default Float.MIN_VALUE;
    String defaultS() default "";
    BoolUnset defaultB() default BoolUnset.UNSET;
    
    long max() default Long.MAX_VALUE;
    long min() default Long.MIN_VALUE;
    double maxD() default Double.MAX_VALUE;
    double minD() default -Double.MAX_VALUE;
}
