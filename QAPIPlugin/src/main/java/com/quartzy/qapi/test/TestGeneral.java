package com.quartzy.qapi.test;

import com.quartzy.qapi.CommandTest;
import com.quartzy.qapi.TestExecutor;
import com.quartzy.qapi.command.Argument;
import com.quartzy.qapi.command.ArgumentExecutor;
import com.quartzy.qapi.command.ArgumentType;
import com.quartzy.qapi.command.CommandExecutor;

@CommandExecutor("command_generated-tests.general")
public class TestGeneral extends CommandTest{
    
    @ArgumentExecutor("intT.val")
    @TestExecutor("intT 23")
    public void testInt(@Argument(name = "val", type = ArgumentType.INTEGER) int i){
        assertEquals(i, 23);
        finishedTest();
    }
    
    @ArgumentExecutor("floatT.val")
    @TestExecutor("floatT 23.34")
    public void testFloat(@Argument(name = "val", type = ArgumentType.FLOAT) float f){
        assertEquals(f, 23.34f);
        finishedTest();
    }
    
    @ArgumentExecutor("doubleT.val")
    @TestExecutor("doubleT 346734.2344")
    public void testDouble(@Argument(name = "val", type = ArgumentType.DOUBLE) double d){
        assertEquals(d, 346734.2344d);
        finishedTest();
    }
    
    @ArgumentExecutor("boolT.val")
    @TestExecutor("boolT false")
    public void testBoolean(@Argument(name = "val", type = ArgumentType.BOOLEAN) boolean b){
        assertEquals(b, false);
        finishedTest();
    }
    
    @TestExecutor({"aliasT aliasTest1", "aliasT aliasTest2"})
    @ArgumentExecutor("aliasT.aliasTest1|aliasTest2")
    public void testAliases(){
        finishedTest();
    }
}
