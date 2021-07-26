package com.quartzy.qapi;

import com.quartzy.qapi.command.CommandHandler;
import com.quartzy.qapi.nbt.NBTCompound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class QAPIPlugin extends JavaPlugin implements Listener{
    @Override
    public void onLoad(){
        System.out.println("Loading QAPI");
    }
    
    @Override
    public void onDisable(){
        System.out.println("Disabled QAPI");
    }
    
    @Override
    public void onEnable(){
        System.out.println("Enabled QAPI");
        QAPIProvider qapiProvider = QAPI.qapi();
        qapiProvider.sayHi();
        getServer().getPluginManager().registerEvents(this, this);
        
        getCommand("perform_tests").setExecutor(new PerformTests());
    
        CommandHandler.addCommand(TestCommand.class);
        CommandHandler.addCommand(SimpleCommand.class);
    }
    
    @EventHandler
    public void playerJoin(PlayerJoinEvent event){
        NBTCompound compound = new NBTCompound();
        compound.setString("testValue", "ahoy");
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        itemStack = QAPI.nbtProvider().applyNBTToItem(itemStack, compound);
        event.getPlayer().getInventory().addItem(itemStack);
        NBTCompound nbtFromItem = QAPI.nbtProvider().getNBTFromItem(itemStack);
        System.out.println(nbtFromItem.asString());
    }
}
