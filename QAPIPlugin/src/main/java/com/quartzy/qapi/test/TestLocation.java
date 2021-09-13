package com.quartzy.qapi.test;

import com.quartzy.qapi.CommandTest;
import com.quartzy.qapi.TestExecutor;
import com.quartzy.qapi.command.Argument;
import com.quartzy.qapi.command.ArgumentExecutor;
import com.quartzy.qapi.command.ArgumentType;
import com.quartzy.qapi.command.CommandExecutor;
import org.bukkit.Location;

@CommandExecutor("command_generated-tests.location")
public class TestLocation extends CommandTest{
    
    @ArgumentExecutor("blockLoc.loc")
    @TestExecutor("blockLoc 383 200 2344")
    public void testBlockPos(@Argument(name="loc", type = ArgumentType.BLOCK_POS) Location loc){
        assertEquals(loc.getBlockX(), 383);
        assertEquals(loc.getBlockY(), 200);
        assertEquals(loc.getBlockZ(), 2344);
        finishedTest();
    }
    
    @ArgumentExecutor("columnLoc.loc")
    @TestExecutor("columnLoc 383 200")
    public void testColumnPos(@Argument(name="loc", type = ArgumentType.COLUMN_POS) Location loc){
        assertEquals(loc.getBlockX(), 383);
        assertEquals(loc.getBlockZ(), 200);
        finishedTest();
    }
    
    @ArgumentExecutor("vec2.loc")
    @TestExecutor("vec2 383.32 200.45")
    public void testVec2(@Argument(name="loc", type = ArgumentType.VEC2) Location loc){
        assertEquals(loc.getX(), 383.32);
        assertEquals(loc.getZ(), 200.45);
        finishedTest();
    }
    
    @ArgumentExecutor("vec3.loc")
    @TestExecutor("vec3 383.32 200.45 3732.5677")
    public void testVec3(@Argument(name="loc", type = ArgumentType.VEC3) Location loc){
        assertEquals(loc.getX(), 383.32);
        assertEquals(loc.getY(), 200.45);
        assertEquals(loc.getZ(), 3732.5677);
        finishedTest();
    }
}
