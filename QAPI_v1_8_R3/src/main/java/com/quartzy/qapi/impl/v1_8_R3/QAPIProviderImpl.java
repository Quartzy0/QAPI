package com.quartzy.qapi.impl.v1_8_R3;

import com.quartzy.qapi.Particle;
import com.quartzy.qapi.QAPIProvider;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftEffect;
import org.bukkit.entity.Player;

public class QAPIProviderImpl implements QAPIProvider{
    @Override
    public void sayHi(){
        System.out.println("Hi from 1.8!!!!!    " + Bukkit.getServer().getVersion());
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
        Effect effect = toBukkitParticle(particle, Effect.class);
        location.getWorld().spigot().playEffect(location, effect, 0, CraftEffect.getDataValue(effect, data), 0f, 0f, 0f, 1f, count, 64);
    }
    
    @Override
    public <T> void spawnParticle(Player player, Particle particle, Location location, int count, T data){
        Effect effect = toBukkitParticle(particle, Effect.class);
        player.spigot().playEffect(location, effect, 0, CraftEffect.getDataValue(effect, data), 0f, 0f, 0f, 1f, count, 64);
    }
    
    public <T> T toBukkitParticle(Particle particle, Class<T> bukkitClass){
        switch(particle){
            case FIREWORKS_SPARK:
                return (T) Effect.FIREWORKS_SPARK;
            case CRIT:
                return (T) Effect.CRIT;
            case CRIT_MAGIC:
                return (T) Effect.MAGIC_CRIT;
            case SPELL_MOB:
                return (T) Effect.POTION_SWIRL;
            case SPELL_MOB_AMBIENT:
                return (T) Effect.POTION_SWIRL_TRANSPARENT;
            case SPELL:
                return (T) Effect.SPELL;
            case SPELL_INSTANT:
                return (T) Effect.INSTANT_SPELL;
            case SPELL_WITCH:
                return (T) Effect.WITCH_MAGIC;
            case NOTE:
                return (T) Effect.NOTE;
            case PORTAL:
                return (T) Effect.PORTAL;
            case ENCHANTMENT_TABLE:
                return (T) Effect.FLYING_GLYPH;
            case FLAME:
                return (T) Effect.FLAME;
            case LAVA:
                return (T) Effect.LAVA_POP;
            case WATER_SPLASH:
                return (T) Effect.SPLASH;
            case SMOKE_NORMAL:
                return (T) Effect.SMOKE;
            case EXPLOSION_NORMAL:
                return (T) Effect.EXPLOSION;
            case EXPLOSION_LARGE:
                return (T) Effect.EXPLOSION_LARGE;
            case EXPLOSION_HUGE:
                return (T) Effect.EXPLOSION_HUGE;
            case SUSPENDED_DEPTH:
                return (T) Effect.VOID_FOG;
            case TOWN_AURA:
                return (T) Effect.SMALL_SMOKE;
            case CLOUD:
                return (T) Effect.CLOUD;
            case REDSTONE:
                return (T) Effect.COLOURED_DUST;
            case SNOWBALL:
                return (T) Effect.SNOWBALL_BREAK;
            case DRIP_WATER:
                return (T) Effect.WATERDRIP;
            case DRIP_LAVA:
                return (T) Effect.LAVADRIP;
            case SNOW_SHOVEL:
                return (T) Effect.SNOW_SHOVEL;
            case SLIME:
                return (T) Effect.SLIME;
            case HEART:
                return (T) Effect.HEART;
            case VILLAGER_ANGRY:
                return (T) Effect.VILLAGER_THUNDERCLOUD;
            case VILLAGER_HAPPY:
                return (T) Effect.HAPPY_VILLAGER;
            case SMOKE_LARGE:
                return (T) Effect.LARGE_SMOKE;
            case ITEM_CRACK:
                return (T) Effect.ITEM_BREAK;
            case LEGACY_BLOCK_CRACK:
            case BLOCK_CRACK:
                return (T) Effect.TILE_BREAK;
            case LEGACY_BLOCK_DUST:
            case LEGACY_FALLING_DUST:
            case BLOCK_DUST:
                return (T) Effect.TILE_DUST;
            case FOOTSTEP:
                return (T) Effect.FOOTSTEP;
        }
        return null;
    }
}
