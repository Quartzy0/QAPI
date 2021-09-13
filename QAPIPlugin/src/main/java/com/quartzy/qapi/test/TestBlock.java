package com.quartzy.qapi.test;

import com.cryptomorin.xseries.XMaterial;
import com.quartzy.qapi.CommandTest;
import com.quartzy.qapi.TestExecutor;
import com.quartzy.qapi.Version;
import com.quartzy.qapi.command.Argument;
import com.quartzy.qapi.command.ArgumentExecutor;
import com.quartzy.qapi.command.ArgumentType;
import com.quartzy.qapi.command.CommandExecutor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.function.Predicate;

@CommandExecutor("command_generated-tests.block")
public class TestBlock extends CommandTest{
    
    @ArgumentExecutor("blockMat.mat")
    @TestExecutor("blockMat minecraft:dirt")
    public void testBlock(@Argument(name="mat", type = ArgumentType.BLOCK_STATE) XMaterial material){
        assertEquals(material, XMaterial.DIRT);
        finishedTest();
    }
    
    @ArgumentExecutor("blockPred.mat")
    @TestExecutor(value = "blockPred #minecraft:planks", minVersion = Version.v1_13_R1)
    public void testBlockPredicate(@Argument(name = "mat", type = ArgumentType.BLOCK_PREDICATE)Predicate<BlockState> blockPred){
        World world = Bukkit.getWorlds().get(0);
        Block blockAt = world.getBlockAt(new Location(world, 0, 0, 0));
        Material type = Material.values()[blockAt.getType().ordinal()];
        blockAt.setType(XMaterial.OAK_PLANKS.parseMaterial());

        try{
            assertEquals(blockPred, blockAt.getState());
        }finally{
            blockAt.setType(type);
        }
        finishedTest();
    }
}
