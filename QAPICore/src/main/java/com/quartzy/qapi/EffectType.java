package com.quartzy.qapi;

import org.bukkit.ChatColor;

public enum EffectType {
   BENEFICIAL(ChatColor.BLUE),
   HARMFUL(ChatColor.RED),
   NEUTRAL(ChatColor.BLUE);

   private final ChatColor color;

   EffectType(ChatColor color) {
      this.color = color;
   }

   public ChatColor getColor() {
      return this.color;
   }
}