package com.quartzy.qapi;

import com.quartzy.qapi.command.CommandProvider;
import com.quartzy.qapi.nbt.NBTProvider;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class QAPI{
    private static QAPIProvider instance;
    private static Version version;
    private static NBTProvider nbtProvider;
    private static CommandProvider commandProvider;
    
    public static Version version(){
        if(version!=null)return version;
    
        String name = Bukkit.getServer().getClass().getPackage().getName();
        String versionString = name.substring(name.lastIndexOf('.') + 1);
        
        try{
            version = Version.valueOf(versionString);
        }catch(NullPointerException | IllegalArgumentException e){
            throw new RuntimeException("Unable to find minecraft version of server (" + versionString + ")");
        }
        
        return version;
    }
    
    public static QAPIProvider qapi(){
        if(instance!=null)return instance;
        try{
            Class<?> aClass = Class.forName("com.quartzy.qapi.impl." + version().name() + ".QAPIProviderImpl");
            Constructor<?> constructor = aClass.getConstructor();
            instance = (QAPIProvider) constructor.newInstance();
        } catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e){
            System.err.println("Error when trying to find QAPIProvider implementation for version " + version().name);
            e.printStackTrace();
        }
        return instance;
    }
    
    public static NBTProvider nbtProvider(){
        if(nbtProvider!=null)return nbtProvider;
        try{
            Class<?> aClass = Class.forName("com.quartzy.qapi.impl." + version().name() + ".NBTProviderImpl");
            Constructor<?> constructor = aClass.getConstructor();
            nbtProvider = (NBTProvider) constructor.newInstance();
        } catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e){
            System.err.println("Error when trying to find NBTProvider implementation for version " + version().name);
            e.printStackTrace();
        }
        return nbtProvider;
    }
    
    public static CommandProvider commandProvider(){
        if(commandProvider!=null)return commandProvider;
        try{
            Class<?> aClass = Class.forName("com.quartzy.qapi.impl." + version().name() + ".CommandProviderImpl");
            Constructor<?> constructor = aClass.getConstructor();
            commandProvider = (CommandProvider) constructor.newInstance();
        } catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e){
            System.err.println("Error when trying to find CommandProvider implementation for version " + version().name);
            e.printStackTrace();
        }
        return commandProvider;
    }
}
