package com.quartzy.qapi.impl.v1_13_R1;

import com.quartzy.qapi.Particle;
import com.quartzy.qapi.QAPIProvider;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_13_R1.CraftEffect;
import org.bukkit.craftbukkit.v1_13_R1.CraftParticle;
import org.bukkit.entity.Player;

public class QAPIProviderImpl implements QAPIProvider{
    @Override
    public void sayHi(){
        System.out.println("Hi from 1.9!");
    }
    
    @Override
    public void sendMessage(CommandSender sender, BaseComponent... components){
        if(sender instanceof Player){
            ((Player) sender).spigot().sendMessage(components);
        }else{
            String finalString = "";
            for(int i = 0; i < components.length; i++){
                if(i==components.length-1) finalString+=components[i].toLegacyText();
                else finalString+=components[i].toLegacyText() + "\n";
            }
            sender.sendMessage(finalString);
        }
    }
    
    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, T data){
        location.getWorld().spawnParticle(toBukkitParticle(particle, org.bukkit.Particle.class), location, count, data);
    }
    
    @Override
    public <T> void spawnParticle(Player player, Particle particle, Location location, int count, T data){
        player.spawnParticle(toBukkitParticle(particle, org.bukkit.Particle.class), location, count, data);
    }
    
    
    public <T> T toBukkitParticle(Particle particle, Class<T> bukkitClass){
        try{
            return (T) org.bukkit.Particle.valueOf(particle.name());
        }catch(Exception e){
            return null;
        }
    }
}
