package com.quartzy.qapi.test;

import com.quartzy.qapi.CommandTest;
import com.quartzy.qapi.TestExecutor;
import com.quartzy.qapi.command.Argument;
import com.quartzy.qapi.command.ArgumentExecutor;
import com.quartzy.qapi.command.ArgumentType;
import com.quartzy.qapi.command.CommandExecutor;
import org.bukkit.ChatColor;

@CommandExecutor("command_generated-tests.chat")
public class TestChat extends CommandTest{
    
    @ArgumentExecutor("colorT.val")
    @TestExecutor("colorT c")
    public void testColor(@Argument(name = "val", type = ArgumentType.COLOR) ChatColor c){
        assertEquals(c, ChatColor.RED);
        finishedTest();
    }
    
    @ArgumentExecutor("messageT.val")
    @TestExecutor("messageT this is an epic test")
    public void testMessage(@Argument(name = "val", type = ArgumentType.MESSAGE) String s){
        assertEquals(s, "this is an epic test");
        finishedTest();
    }
    
    @ArgumentExecutor("componentT.val")
    @TestExecutor("componentT [\"\",{\"text\":\"White text! \",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Haha hover\"}},{\"text\":\"Black text! \",\"color\":\"black\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Haha hover\"}}]")
    public void testComponent(@Argument(name = "val", type = ArgumentType.COMPONENT) String s){
        assertEquals(s, "this is an epic test");
        finishedTest();
    }
}
