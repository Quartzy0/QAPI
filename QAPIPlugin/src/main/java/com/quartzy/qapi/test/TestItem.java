package com.quartzy.qapi.test;

import com.quartzy.qapi.CommandTest;
import com.quartzy.qapi.command.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

@CommandExecutor("test-testitem")
public class TestItem extends CommandTest{
    
    @ArgumentExecutor("material.item.count")
    public void testItemA(@Argument(name = "item", type = ArgumentTypeEnum.ITEM_STACK)Material material,
                         @Argument(name = "count", type = ArgumentTypeEnum.INTEGER)int count){
        assertEquals(material, Material.DIAMOND);
        assertEquals(count, 33);
        finishedTest();
    }
    
    @ArgumentExecutor("predicate.item.count")
    public void testItemB(@Argument(name = "item", type = ArgumentTypeEnum.ITEM_PREDICATE) Predicate<ItemStack> itemPredicate,
                         @Argument(name = "count", type = ArgumentTypeEnum.INTEGER)int count){
        assertEquals(itemPredicate, new ItemStack(Material.BOAT));
        assertEquals(count, 72);
        finishedTest();
    }
    
    @ArgumentExecutor("slot.itemSlot")
    public void testItemC(@Argument(name = "itemSlot", type = ArgumentTypeEnum.ITEM_SLOT) int itemSlot){
        assertEquals(itemSlot, 200+24);
        finishedTest();
    }
    
    @ArgumentExecutor("enchantment.itemEnch")
    public void testItemD(@Argument(name = "itemSlot", type = ArgumentTypeEnum.ITEM_ENCHANTMENT) Enchantment ench){
        assertEquals(ench, Enchantment.ARROW_DAMAGE);
        finishedTest();
    }
    
    @Override
    public String[] testCases(){
        return new String[]{"test-testitem material minecraft:diamond 33", "test-testitem predicate minecraft:boat 72", "test-testitem slot enderchest.24", "test-testitem minecraft:power"};
    }
    
    @Override
    public int testCount(){
        return 3;
    }
}
