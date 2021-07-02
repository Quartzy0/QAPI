package com.quartzy.qapi.impl.v1_16_R2;

import com.quartzy.qapi.QAPIProvider;

public class QAPIProviderImpl implements QAPIProvider{
    @Override
    public void sayHi(){
        System.out.println("Hi from 1.16.2");
    }
}
