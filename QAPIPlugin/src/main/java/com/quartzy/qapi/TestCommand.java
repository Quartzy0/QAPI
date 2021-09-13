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
    public void giveItem(@Argument(name = "target", type = ArgumentType.PLAYER) Player target,
                         @Argument(name = "item", type = ArgumentType.ITEM_STACK) Material item,
                         @Argument(name = "amount", type = ArgumentType.INTEGER, defaultI = 23) int amount,
                         @Argument(name = "silent", type = ArgumentType.BOOLEAN, defaultB = BoolUnset.TRUE) boolean silent) throws CommandException{
        target.getInventory().addItem(new ItemStack(item, amount));
        if(!silent){
            target.sendMessage("You got " + amount + " " + item.name().toLowerCase() + (amount>1 ? "s" : "") + "!");
        }
        throw new CommandException("item", "Epic command exception! Test argument: %d", 55);
    }
    
    @ArgumentExecutor("target.location")
    public void teleport(@Argument(name = "target", type = ArgumentType.PLAYER) Player target,
                         @Argument(name = "location", type = ArgumentType.BLOCK_POS) Location location){
        target.teleport(location);
    }
    
    @ArgumentExecutor("itemP.pred")
    public void predItem(@Argument(name = "pred", type = ArgumentType.ITEM_PREDICATE) Predicate<ItemStack> predItem){
        System.out.println(predItem.test(new ItemStack(Material.STONE_BUTTON, 32)));
    }
    
    @ArgumentExecutor("blockP.pred")
    public void predBlock(@Argument(name = "pred", type = ArgumentType.BLOCK_PREDICATE) Predicate<BlockState> predBlock,
                          @Sender Player sender){
        System.out.println(predBlock.test(sender.getWorld().getBlockAt(sender.getLocation()).getState()));
    }
    
    @ArgumentExecutor(value = "manytest.block.item.blockp.itemp.profile.bpos.cpos.vec3.vec2.color.resloc.angle.rot")
    public void multiArgumentTest(@Argument(name = "block", type = ArgumentType.BLOCK_STATE)Material blockData,
                                  @Argument(name = "item", type = ArgumentType.ITEM_STACK) Material material,
                                  @Argument(name = "blockp", type = ArgumentType.BLOCK_PREDICATE)Predicate<BlockState> state,
                                  @Argument(name = "itemp", type = ArgumentType.ITEM_PREDICATE)Predicate<ItemStack> itemStackPredicate,
                                  @Argument(name = "profile", type = ArgumentType.GAME_PROFILE) GameProfile[] gameProfile,
                                  @Argument(name = "bpos", type = ArgumentType.BLOCK_POS) Location location,
                                  @Argument(name = "cpos", type = ArgumentType.COLUMN_POS) Location cLocation,
                                  @Argument(name = "vec3", type = ArgumentType.VEC3) Location vec3,
                                  @Argument(name = "vec2", type = ArgumentType.VEC2) Location vec2,
                                  @Argument(name = "color", type = ArgumentType.COLOR) ChatColor color,
                                  @Argument(name = "resloc", type = ArgumentType.RESOURCE_LOCATION)NamespacedKey key,
                                  @Argument(name = "angle", type = ArgumentType.ANGLE) float angle,
                                  @Argument(name = "rot", type = ArgumentType.ROTATION) Location rotation){
        if(blockData==null || material==null || state==null || itemStackPredicate==null || gameProfile == null || location == null || cLocation == null || vec3 == null || vec2 == null || color == null || key == null || angle<-180f || angle>180f || rotation == null)
            throw new NullPointerException("An argument in a test method was null. Something went wrong");
        System.out.println("BlockData: " + blockData.name());
        System.out.println("Material: " + material.name());
        System.out.println("GameProfile: " + Arrays.toString(gameProfile));
        System.out.println("BlockPos: " + location);
        System.out.println("ColumnPos: " + cLocation);
        System.out.println("Vec3: " + vec3);
        System.out.println("Vec2: " + vec2);
        System.out.println("Color: " + color);
        System.out.println("NamespacedKey: " + key);
        System.out.println("Angle: " + angle);
        System.out.println("Rotation: " + rotation);
    }
}
