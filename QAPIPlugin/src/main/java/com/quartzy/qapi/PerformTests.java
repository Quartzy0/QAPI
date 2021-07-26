package com.quartzy.qapi;

import com.quartzy.qapi.command.CommandHandler;
import com.quartzy.qapi.test.TestItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PerformTests implements CommandExecutor{
    public static final List<Class<? extends CommandTest>> testClasses = Collections.unmodifiableList(Arrays.asList(TestItem.class));
    public static final List<String> testNames = Collections.unmodifiableList(Arrays.asList("test-testitem"));
    
    private long lastMs = -1L;
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(args.length!=0)return false;
        msg(sender, "Adding test commands");
        start();
        List<CommandTest> instances = new ArrayList<>();
        for(Class<? extends CommandTest> testClass : testClasses){
            instances.add(CommandHandler.addCommand(testClass));
        }
        msg(sender, "Commands added " + end());
        msg(sender, "Starting testing");
        start();
        for(int i = 0; i < instances.size(); i++){
            for(String s : instances.get(i).testCases()){
                msg(sender, "Test: " + s);
                Bukkit.dispatchCommand(sender, s);
            }
            if(!instances.get(i).allTestsFinish()){
                msg(sender, ChatColor.RED + "All tests have not been completed!");
            }
        }
        msg(sender, "All tests finished successfully " + end());
        msg(sender, "Removing test commands");
        start();
        for(String testName : testNames){
            CommandHandler.removeCommand(testName);
        }
        msg(sender, "Test commands removed successfully " + end());
        return true;
    }
    
    private void start(){
        lastMs = System.currentTimeMillis();
    }
    
    private String end(){
        return "(took: " + (System.currentTimeMillis() - lastMs) + " ms)";
    }
    
    private static void msg(CommandSender sender, String message){
        sender.sendMessage(ChatColor.AQUA + "[" + ChatColor.GOLD + "QAPI" + ChatColor.AQUA + "] " + ChatColor.GOLD + message);
    }
}
