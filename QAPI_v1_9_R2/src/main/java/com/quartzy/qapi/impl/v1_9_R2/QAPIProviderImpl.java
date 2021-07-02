package com.quartzy.qapi.impl.v1_9_R2;

import com.quartzy.qapi.QAPIProvider;

public class QAPIProviderImpl implements QAPIProvider{
    @Override
    public void sayHi(){
        System.out.println("Hi from 1.9.4!");
    }
}
