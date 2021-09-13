package com.quartzy.qapi;

import com.quartzy.qapi.command.CommandHandler;
import com.quartzy.qapi.test.TestBlock;
import com.quartzy.qapi.test.TestItem;
import com.quartzy.qapi.test.TestLocation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.*;

public class PerformTests implements CommandExecutor{
    public static final List<Class<? extends CommandTest>> testClasses = Collections.unmodifiableList(Arrays.asList(TestItem.class, TestBlock.class, TestLocation.class));
    private static final File resultsFile = new File("testResults.properties");
    
    private long lastMs = -1L;
    
    private static void msg(CommandSender sender, String message){
        sender.sendMessage(ChatColor.AQUA + "[" + ChatColor.GOLD + "QAPI" + ChatColor.AQUA + "] " + ChatColor.GOLD + message);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(args.length > 1) return false;
        boolean writeToFile = args.length == 1 && Boolean.parseBoolean(args[0]);
        msg(sender, "Adding test commands");
        start();
        List<CommandContainer> containers = new ArrayList<>(testClasses.size());
        for(Class<? extends CommandTest> testClass : testClasses){
            CommandContainer fromClass = CommandContainer.getFromClass(testClass);
            if(fromClass == null){
                sender.sendMessage(ChatColor.RED + "Test (" + testClass.getName() + ") could not be instantiated");
                continue;
            }
            containers.add(fromClass);
        }
        msg(sender, "Commands added " + end());
        msg(sender, "Starting testing");
        start();
        boolean allSuccess = true;
        Properties properties = new Properties();
        for(CommandContainer container : containers){
            for(String s : container.testCommands){
                msg(sender, "Test: " + s);
                Bukkit.dispatchCommand(sender, s);
            }
            if(!container.allTestsFinish()){
                allSuccess = false;
                properties.setProperty(container.name, "false");
                msg(sender, ChatColor.RED + "All tests have not been completed! Something has gone wrong");
            }else{
                properties.setProperty(container.name, "true");
            }
        }
        properties.setProperty("allSuccess", allSuccess ? "true" : "false");
        if(writeToFile){
            if(!resultsFile.exists()){
                try{
                    resultsFile.createNewFile();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
            try(PrintWriter writer = new PrintWriter(resultsFile)){
                properties.store(writer, "Results of testing QAPI commands");
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        if(allSuccess) msg(sender, "All tests finished successfully " + end());
        else msg(sender, "Some tests did not finish successfully " + end());
        msg(sender, "Removing test commands");
        start();
        for(CommandContainer container : containers){
            CommandHandler.removeCommand(container.name);
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
    
    private static class CommandContainer{
        public final CommandTest test;
        public final String name;
        public final String[] testCommands;
        
        public CommandContainer(CommandTest test, String name, String[] testCommands){
            this.test = test;
            this.name = name;
            this.testCommands = testCommands;
        }
        
        public static CommandContainer getFromClass(Class<? extends CommandTest> testClass){
            CommandTest commandTest = CommandHandler.addCommand(testClass);
            if(commandTest == null) return null;
            if(!testClass.isAnnotationPresent(com.quartzy.qapi.command.CommandExecutor.class)) return null;
            com.quartzy.qapi.command.CommandExecutor commandExecutor = testClass.getAnnotation(com.quartzy.qapi.command.CommandExecutor.class);
            String[] split = commandExecutor.value().split("\\.");
            StringBuilder fullPath = new StringBuilder();
            for(int i = 0; i < split.length; i++){
                fullPath.append(split[i]);
                if(i != split.length - 1) fullPath.append(" ");
            }
            String name = split[split.length - 1];
            
            List<String> test = new ArrayList<>();
            for(Method declaredMethod : testClass.getDeclaredMethods()){
                if(declaredMethod.isAnnotationPresent(TestExecutor.class)){
                    TestExecutor testExecutor = declaredMethod.getAnnotation(TestExecutor.class);
                    if(QAPI.version().lower(testExecutor.minVersion())) continue;
                    test.add(fullPath + " " + testExecutor.value());
                }
            }
            
            return new CommandContainer(commandTest, name, test.toArray(new String[test.size()]));
        }
        
        public boolean allTestsFinish(){
            return test.getTestsFinished() == testCommands.length;
        }
    }
}
