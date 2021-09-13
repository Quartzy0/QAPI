package com.quartzy.qapi.command.converter;

public interface TypeConverter<T, S>{
    T convert(Object obj);
    
    Class<S> getTypeIn();
    Class<T> getTypeOut();
}
