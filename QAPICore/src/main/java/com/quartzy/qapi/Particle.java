package com.quartzy.qapi;

import com.google.common.base.Preconditions;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public enum Particle{
    EXPLOSION_NORMAL,
    EXPLOSION_LARGE,
    EXPLOSION_HUGE,
    FIREWORKS_SPARK,
    WATER_BUBBLE,
    WATER_SPLASH,
    WATER_WAKE,
    SUSPENDED,
    SUSPENDED_DEPTH,
    CRIT,
    CRIT_MAGIC,
    SMOKE_NORMAL,
    SMOKE_LARGE,
    SPELL,
    SPELL_INSTANT,
    SPELL_MOB,
    SPELL_MOB_AMBIENT,
    SPELL_WITCH,
    DRIP_WATER,
    DRIP_LAVA,
    VILLAGER_ANGRY,
    VILLAGER_HAPPY,
    TOWN_AURA,
    NOTE,
    PORTAL,
    ENCHANTMENT_TABLE,
    FLAME,
    LAVA,
    CLOUD,
    REDSTONE(org.bukkit.Particle.DustOptions.class),
    SNOWBALL,
    SNOW_SHOVEL,
    SLIME,
    HEART,
    BARRIER,
    ITEM_CRACK(ItemStack.class),
    BLOCK_CRACK(BlockData.class),
    BLOCK_DUST(BlockData.class),
    WATER_DROP,
    MOB_APPEARANCE,
    DRAGON_BREATH,
    END_ROD,
    DAMAGE_INDICATOR,
    SWEEP_ATTACK,
    FALLING_DUST(BlockData.class),
    TOTEM,
    SPIT,
    SQUID_INK,
    BUBBLE_POP,
    CURRENT_DOWN,
    BUBBLE_COLUMN_UP,
    NAUTILUS,
    DOLPHIN,
    SNEEZE,
    CAMPFIRE_COSY_SMOKE,
    CAMPFIRE_SIGNAL_SMOKE,
    COMPOSTER,
    FLASH,
    FALLING_LAVA,
    LANDING_LAVA,
    FALLING_WATER,
    DRIPPING_HONEY,
    FALLING_HONEY,
    LANDING_HONEY,
    FALLING_NECTAR,
    SOUL_FIRE_FLAME,
    ASH,
    CRIMSON_SPORE,
    WARPED_SPORE,
    SOUL,
    DRIPPING_OBSIDIAN_TEAR,
    FALLING_OBSIDIAN_TEAR,
    LANDING_OBSIDIAN_TEAR,
    REVERSE_PORTAL,
    WHITE_ASH,
    FOOTSTEP,
    LEGACY_BLOCK_CRACK(MaterialData.class),
    LEGACY_BLOCK_DUST(MaterialData.class),
    LEGACY_FALLING_DUST(MaterialData.class);
    
    private final Class<?> dataType;
    
    Particle() {
        this.dataType = Void.class;
    }
    
    Particle(Class<?> data) {
        this.dataType = data;
    }
    
    public Class<?> getDataType() {
        return this.dataType;
    }
    
    public <T> T getBukkitParticle(){
        Class<T> dataType = null;
        try{
            if(QAPI.version().higher(Version.v1_8_R3)){
                    dataType = (Class<T>) Class.forName("org.bukkit.Particle");
            }else{
                dataType = (Class<T>) Class.forName("org.bukkit.Effect");
            }
        } catch(ClassNotFoundException e){
            System.err.println("Can't find particle class?!");
            e.printStackTrace();
        }
        return QAPI.qapi().toBukkitParticle(this, dataType);
    }
    
    public void spawnParticle(Location location, int count, int data){
        QAPI.qapi().spawnParticle(this, location, count, data);
    }
    
    public <T> void spawnParticle(Location location, int count, T data){
        QAPI.qapi().spawnParticle(this, location, count, data);
    }
    
    public void spawnParticle(Player player, Location location, int count, int data){
        QAPI.qapi().spawnParticle(player, this, location, count, data);
    }
    
    public <T> void spawnParticle(Player player, Location location, int count, T data){
        QAPI.qapi().spawnParticle(player, this, location, count, data);
    }
    
    public void spawnParticle(Player player, int count, int data){
        QAPI.qapi().spawnParticle(player, this, player.getLocation(), count, data);
    }
    
    public <T> void spawnParticle(Player player, int count, T data){
        QAPI.qapi().spawnParticle(player, this, player.getLocation(), count, data);
    }
    
    public static class DustOptions {
        private final Color color;
        private final float size;
        
        public DustOptions( Color color, float size) {
            Preconditions.checkArgument(color != null, "color");
            this.color = color;
            this.size = size;
        }
        
        public Color getColor() {
            return this.color;
        }
        
        public float getSize() {
            return this.size;
        }
    }
    
    public static Particle fromLegacy(String in){
        try{
            return Particle.valueOf(in.toLowerCase());
        }catch(Exception e){
            return null;
        }
    }
}
