package com.quartzy.qapi;

import com.quartzy.qapi.command.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class TestCommand extends Command{
    @Override
    public String getName(){
        return "test-command";
    }
    
    @ArgumentExecutor(path = "target.item.amount.silent")
    public void giveItem(@Argument(name = "target", type = ArgumentTypeEnum.PLAYER) Player target,
                         @Argument(name = "item", type = ArgumentTypeEnum.MATERIAL) Material item,
                         @Argument(name = "amount", type = ArgumentTypeEnum.INTEGER, defaultI = 23) int amount,
                         @Argument(name = "silent", type = ArgumentTypeEnum.BOOLEAN, defaultB = BoolUnset.TRUE) boolean silent){
        target.getInventory().addItem(new ItemStack(item, amount));
        if(!silent){
            target.sendMessage("You got " + amount + " " + item.name().toLowerCase() + (amount>1 ? "s" : "") + "!");
        }
    }
    
    @ArgumentExecutor(path = "target.location")
    public void teleport(@Argument(name = "target", type = ArgumentTypeEnum.PLAYER) Player target,
                         @Argument(name = "location", type = ArgumentTypeEnum.BLOCK_POS) Location location){
        target.teleport(location);
    }
    
    @ArgumentExecutor(path = "message")
    public void giveEntitiesEffect(@Argument(name = "message", type = ArgumentTypeEnum.MESSAGE_SELECTORS) String message,
                                   @Sender Player sender){
        Bukkit.broadcastMessage(message + " (This message was brought to you by our sponsor, " + sender.getName() + ")");
    }
}
