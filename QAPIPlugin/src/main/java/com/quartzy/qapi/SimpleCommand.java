package com.quartzy.qapi;

import com.quartzy.qapi.command.*;
import com.quartzy.qapi.nbt.NBTBase;
import com.quartzy.qapi.nbt.NBTPath;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandExecutor("simplecmd")
public class SimpleCommand extends Command{
    
    @ArgumentExecutor("str.target")
    public void reply(@Argument(name = "str", type = ArgumentTypeEnum.STRING_WORD) String i,
                      @Argument(name = "target", type = ArgumentTypeEnum.PLAYER) Player player,
                      @Sender CommandSender sender){
        player.sendMessage("Message from " + sender.getName() + ": " + i);
    }
    
    @ArgumentExecutor("block.location")
    public void setBlock(@Argument(name = "block", type = ArgumentTypeEnum.BLOCK_STATE)Material block,
                         @Argument(name = "location", type = ArgumentTypeEnum.BLOCK_POS) Location location){
        location.getWorld().getBlockAt(location).setType(block);
    }
    
    @ArgumentExecutor("msg.message")
    public void message(@Argument(name = "message", type = ArgumentTypeEnum.MESSAGE_SELECTORS) String msg){
        Bukkit.broadcastMessage(msg);
    }
    
    @ArgumentExecutor("nbtC.nbtVal")
    public void nbtCTest(@Argument(name = "nbtVal", type = ArgumentTypeEnum.NBT_COMPOUND_TAG) NBTBase nbt){
        Bukkit.broadcastMessage(nbt.toString());
    }
    
    @ArgumentExecutor("nbt.nbtVal")
    public void nbtTest(@Argument(name = "nbtVal", type = ArgumentTypeEnum.NBT_TAG) NBTBase nbt){
        Bukkit.broadcastMessage(nbt.toString());
    }
    
    @ArgumentExecutor("nbtP.nbtVal.nbtPath")
    public void nbtPath(@Argument(name = "nbtVal", type = ArgumentTypeEnum.NBT_TAG) NBTBase nbt,
                        @Argument(name = "nbtPath", type = ArgumentTypeEnum.NBT_PATH) NBTPath path){
        Bukkit.broadcastMessage(String.valueOf(path.getMatch(nbt)));
    }
}
