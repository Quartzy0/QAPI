package com.quartzy.qapi.impl.v1_8_R2;

import com.quartzy.qapi.QAPIProvider;
import org.bukkit.Bukkit;

public class QAPIProviderImpl implements QAPIProvider{
    @Override
    public void sayHi(){
        System.out.println("Hi from 1.8.3!!!!!    " + Bukkit.getServer().getVersion());
    }
}
