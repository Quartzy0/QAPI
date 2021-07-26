package com.quartzy.qapi;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface QAPIProvider{
    void sayHi();
    
    void sendMessage(CommandSender sender, BaseComponent... components);
    
    <T> void spawnParticle(Particle particle, Location location, int count, T data);
    
    <T> void spawnParticle(Player player, Particle particle, Location location, int count, T data);
    
    <T> T toBukkitParticle(Particle particle, Class<T> bukkitClass);
}
