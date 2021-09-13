package com.quartzy.qapi.test;

import com.cryptomorin.xseries.XMaterial;
import com.quartzy.qapi.CommandTest;
import com.quartzy.qapi.TestExecutor;
import com.quartzy.qapi.Version;
import com.quartzy.qapi.command.Argument;
import com.quartzy.qapi.command.ArgumentExecutor;
import com.quartzy.qapi.command.ArgumentType;
import com.quartzy.qapi.command.CommandExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

@CommandExecutor("command_generated-tests.item")
public class TestItem extends CommandTest{
    
    @ArgumentExecutor("material.item.count")
    @TestExecutor("material minecraft:diamond 33")
    public void testItemA(@Argument(name = "item", type = ArgumentType.ITEM_STACK) XMaterial material,
                         @Argument(name = "count", type = ArgumentType.INTEGER)int count){
        assertEquals(material, XMaterial.DIAMOND);
        assertEquals(count, 33);
        finishedTest();
    }
    
    @ArgumentExecutor("predicate.item.count")
    @TestExecutor(value = "predicate #minecraft:doors 72", minVersion = Version.v1_13_R1)
    public void testItemB(@Argument(name = "item", type = ArgumentType.ITEM_PREDICATE) Predicate<ItemStack> itemPredicate,
                         @Argument(name = "count", type = ArgumentType.INTEGER)int count){
        assertEquals(itemPredicate, new ItemStack(XMaterial.ACACIA_DOOR.parseMaterial()));
        assertEquals(count, 72);
        finishedTest();
    }
    
    @ArgumentExecutor("slot.itemSlot")
    @TestExecutor("slot enderchest.24")
    public void testItemC(@Argument(name = "itemSlot", type = ArgumentType.ITEM_SLOT) int itemSlot){
        assertEquals(itemSlot, 200+24);
        finishedTest();
    }
    
    @ArgumentExecutor("enchantment.itemEnch")
    @TestExecutor("enchantment minecraft:power")
    public void testItemD(@Argument(name = "itemEnch", type = ArgumentType.ITEM_ENCHANTMENT) Enchantment ench){
        assertEquals(ench, Enchantment.ARROW_DAMAGE);
        finishedTest();
    }
}
