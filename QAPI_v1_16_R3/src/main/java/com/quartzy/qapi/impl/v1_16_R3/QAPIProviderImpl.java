package com.quartzy.qapi.impl.v1_16_R3;

import com.quartzy.qapi.QAPIProvider;
import org.bukkit.Bukkit;

public class QAPIProviderImpl implements QAPIProvider{
    @Override
    public void sayHi(){
        System.out.println("Hi from 1.16!!!  " + Bukkit.getServer().getVersion());
    }
}
