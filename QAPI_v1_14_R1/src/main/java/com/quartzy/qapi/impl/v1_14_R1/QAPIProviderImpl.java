package com.quartzy.qapi.impl.v1_14_R1;

import com.quartzy.qapi.QAPIProvider;

public class QAPIProviderImpl implements QAPIProvider{
    @Override
    public void sayHi(){
        System.out.println("Hi from 1.14!!");
    }
}