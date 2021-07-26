package com.quartzy.qapi;

import com.quartzy.qapi.command.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.function.Predicate;

@CommandExecutor("test-command")
public class TestCommand extends Command{
    
    @ArgumentExecutor(value = "target.item.amount.silent")
    public void giveItem(@Argument(name = "target", type = ArgumentTypeEnum.PLAYER) Player target,
                         @Argument(name = "item", type = ArgumentTypeEnum.ITEM_STACK) ItemStack item,
                         @Argument(name = "amount", type = ArgumentTypeEnum.INTEGER, defaultI = 23) int amount,
                         @Argument(name = "silent", type = ArgumentTypeEnum.BOOLEAN, defaultB = BoolUnset.TRUE) boolean silent) throws CommandException{
        target.getInventory().addItem(item);
        if(!silent){
            target.sendMessage("You got " + amount + " " + item.getType().name().toLowerCase() + (amount>1 ? "s" : "") + "!");
        }
        throw new CommandException("item", "Epic command exception! Test argument: %d", 55);
    }
    
    @ArgumentExecutor("target.location")
    public void teleport(@Argument(name = "target", type = ArgumentTypeEnum.PLAYER) Player target,
                         @Argument(name = "location", type = ArgumentTypeEnum.BLOCK_POS) Location location){
        target.teleport(location);
    }
    
//    @ArgumentExecutor(path = "message")
//    public void giveEntitiesEffect(@Argument(name = "message", type = ArgumentTypeEnum.MESSAGE_SELECTORS) String message,
//                                   @Sender Player sender) {
//        Bukkit.broadcastMessage(message + " (This message was brought to you by our sponsor, " + sender.getName() + ")");
//    }
    
    @ArgumentExecutor(value = "manytest.block.item.blockp.itemp.profile.bpos.cpos.vec3.vec2.color.resloc.angle.rot")
    public void multiArgumentTest(@Argument(name = "block", type = ArgumentTypeEnum.BLOCK_STATE)Material blockData,
                                  @Argument(name = "item", type = ArgumentTypeEnum.ITEM_STACK) Material material,
                                  @Argument(name = "blockp", type = ArgumentTypeEnum.BLOCK_PREDICATE)Predicate<BlockState> state,
                                  @Argument(name = "itemp", type = ArgumentTypeEnum.ITEM_PREDICATE)Predicate<ItemStack> itemStackPredicate,
                                  @Argument(name = "profile", type = ArgumentTypeEnum.GAME_PROFILE) GameProfile[] gameProfile,
                                  @Argument(name = "bpos", type = ArgumentTypeEnum.BLOCK_POS) Location location,
                                  @Argument(name = "cpos", type = ArgumentTypeEnum.COLUMN_POS) Location cLocation,
                                  @Argument(name = "vec3", type = ArgumentTypeEnum.VEC3) Location vec3,
                                  @Argument(name = "vec2", type = ArgumentTypeEnum.VEC2) Location vec2,
                                  @Argument(name = "color", type = ArgumentTypeEnum.COLOR) ChatColor color,
                                  @Argument(name = "resloc", type = ArgumentTypeEnum.RESOURCE_LOCATION)NamespacedKey key,
                                  @Argument(name = "angle", type = ArgumentTypeEnum.ANGLE) float angle,
                                  @Argument(name = "rot", type = ArgumentTypeEnum.ROTATION) Location rotation){
        if(blockData==null || material==null || state==null || itemStackPredicate==null || gameProfile == null || location == null || cLocation == null || vec3 == null || vec2 == null || color == null || key == null || angle<-180f || angle>180f || rotation == null)
            throw new NullPointerException("An argument in a test method was null. Something went wrong");
        System.out.println("BlockData: " + blockData.name());
        System.out.println("Material: " + material.name());
        System.out.println("GameProfile: " + Arrays.toString(gameProfile));
        System.out.println("BlockPos: " + location.toString());
        System.out.println("ColumnPos: " + cLocation.toString());
        System.out.println("Vec3: " + vec3.toString());
        System.out.println("Vec2: " + vec2.toString());
        System.out.println("Color: " + color.toString());
        System.out.println("NamespacedKey: " + key.toString());
        System.out.println("Angle: " + angle);
        System.out.println("Rotation: " + rotation.toString());
    }
}
