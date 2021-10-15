package com.quartzy.qapi;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class ColorUtil{
    private static final HashMap<String, ChatColor> colorsByName = new HashMap<>();
    
    static {
        for(ChatColor value : ChatColor.values()){
            colorsByName.put(value.name().toUpperCase(), value);
        }
    }
    
    @NotNull
    public static ChatColor parseColor(@Nullable String s){
        if(s==null)return ChatColor.RESET;
        String str = s.trim();
        ChatColor byName = colorsByName.get(str.toUpperCase());
        if(byName!=null)return byName;
        ChatColor byChar = ChatColor.getByChar(str.toLowerCase());
        if(byChar!=null)return byChar;
        return ChatColor.RESET;
    }
    
    @Nullable
    public static ChatColor parseColorWithNull(@Nullable String s){
        if(s==null)return null;
        return colorsByName.get(s.trim().toUpperCase());
    }
}
