package com.quartzy.qapi;

import com.quartzy.qapi.command.*;
import com.quartzy.qapi.nbt.NBTBase;
import com.quartzy.qapi.nbt.NBTPath;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandExecutor("simplecmd|smpl.supercool.epic.victory.royale")
public class SimpleCommand extends Command{
    
    @ArgumentExecutor("str.target")
    public void reply(@Argument(name = "str", type = ArgumentType.STRING_WORD) String i,
                      @Argument(name = "target", type = ArgumentType.PLAYER) Player player,
                      @Sender CommandSender sender){
        player.sendMessage("Message from " + sender.getName() + ": " + i);
    }
    
    @ArgumentExecutor("block.location")
    public void setBlock(@Argument(name = "block", type = ArgumentType.BLOCK_STATE) Material block,
                         @Argument(name = "location", type = ArgumentType.BLOCK_POS) Location location){
        location.getWorld().getBlockAt(location).setType(block);
    }
    
    @ArgumentExecutor("msg.message")
    public void message(@Argument(name = "message", type = ArgumentType.MESSAGE_SELECTORS) String msg){
        Bukkit.broadcastMessage(msg);
    }
    
    @ArgumentExecutor("nbtC.nbtVal")
    public void nbtCTest(@Argument(name = "nbtVal", type = ArgumentType.NBT_COMPOUND_TAG) NBTBase nbt){
        Bukkit.broadcastMessage(nbt.toString());
    }
    
    @ArgumentExecutor("nbt.nbtVal")
    public void nbtTest(@Argument(name = "nbtVal", type = ArgumentType.NBT_TAG) NBTBase nbt){
        Bukkit.broadcastMessage(nbt.toString());
    }
    
    @ArgumentExecutor("nbtP.nbtVal.nbtPath")
    public void nbtPath(@Argument(name = "nbtVal", type = ArgumentType.NBT_TAG) NBTBase nbt,
                        @Argument(name = "nbtPath", type = ArgumentType.NBT_PATH) NBTPath path){
        Bukkit.broadcastMessage(String.valueOf(path.getMatch(nbt)));
    }
    
    @CommandExecutor("simplecmd.crack")
    public static class SimpleCommandP2 extends Command{
        
        @ArgumentExecutor("item")
        public void sendMsg(@Argument(name = "item", type = ArgumentType.ITEM_STACK) ItemStack s,
                            @Sender Player sender){
            sender.getInventory().addItem(s);
        }
        
        @ArgumentExecutor("alias.num|number.int")
        public void aliasTest(@Argument(name = "int", type = ArgumentType.INTEGER) int i){
            System.out.println(i);
        }
    }
}
