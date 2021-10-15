package com.quartzy.qapi;

import com.google.common.collect.Maps;
import org.bukkit.ChatColor;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

/**
 * The possible criteria a scoreboard objective may have
 */
public class ScoreCriteria {
   public static final Map<String, ScoreCriteria> INSTANCES = Maps.newHashMap();
   public static final ScoreCriteria DUMMY = new ScoreCriteria("dummy");
   public static final ScoreCriteria TRIGGER = new ScoreCriteria("trigger");
   public static final ScoreCriteria DEATH_COUNT = new ScoreCriteria("deathCount");
   public static final ScoreCriteria PLAYER_KILL_COUNT = new ScoreCriteria("playerKillCount");
   public static final ScoreCriteria TOTAL_KILL_COUNT = new ScoreCriteria("totalKillCount");
   public static final ScoreCriteria HEALTH = new ScoreCriteria("health", true, ScoreCriteria.RenderType.HEARTS);
   public static final ScoreCriteria FOOD = new ScoreCriteria("food", true, ScoreCriteria.RenderType.INTEGER);
   public static final ScoreCriteria AIR = new ScoreCriteria("air", true, ScoreCriteria.RenderType.INTEGER);
   public static final ScoreCriteria ARMOR = new ScoreCriteria("armor", true, ScoreCriteria.RenderType.INTEGER);
   public static final ScoreCriteria XP = new ScoreCriteria("xp", true, ScoreCriteria.RenderType.INTEGER);
   public static final ScoreCriteria LEVEL = new ScoreCriteria("level", true, ScoreCriteria.RenderType.INTEGER);
   public static final ScoreCriteria[] TEAM_KILL = new ScoreCriteria[]{new ScoreCriteria("teamkill." + ChatColor.BLACK.name().toLowerCase()), new ScoreCriteria("teamkill." + ChatColor.DARK_BLUE.name().toLowerCase()), new ScoreCriteria("teamkill." + ChatColor.DARK_GREEN.name().toLowerCase()), new ScoreCriteria("teamkill." + ChatColor.DARK_AQUA.name().toLowerCase()), new ScoreCriteria("teamkill." + ChatColor.DARK_RED.name().toLowerCase()), new ScoreCriteria("teamkill." + ChatColor.DARK_PURPLE.name().toLowerCase()), new ScoreCriteria("teamkill." + ChatColor.GOLD.name().toLowerCase()), new ScoreCriteria("teamkill." + ChatColor.GRAY.name().toLowerCase()), new ScoreCriteria("teamkill." + ChatColor.DARK_GRAY.name().toLowerCase()), new ScoreCriteria("teamkill." + ChatColor.BLUE.name().toLowerCase()), new ScoreCriteria("teamkill." + ChatColor.GREEN.name().toLowerCase()), new ScoreCriteria("teamkill." + ChatColor.AQUA.name().toLowerCase()), new ScoreCriteria("teamkill." + ChatColor.RED.name().toLowerCase()), new ScoreCriteria("teamkill." + ChatColor.LIGHT_PURPLE.name().toLowerCase()), new ScoreCriteria("teamkill." + ChatColor.YELLOW.name().toLowerCase()), new ScoreCriteria("teamkill." + ChatColor.WHITE.name().toLowerCase())};
   public static final ScoreCriteria[] KILLED_BY_TEAM = new ScoreCriteria[]{new ScoreCriteria("killedByTeam." + ChatColor.BLACK.name().toLowerCase()), new ScoreCriteria("killedByTeam." + ChatColor.DARK_BLUE.name().toLowerCase()), new ScoreCriteria("killedByTeam." + ChatColor.DARK_GREEN.name().toLowerCase()), new ScoreCriteria("killedByTeam." + ChatColor.DARK_AQUA.name().toLowerCase()), new ScoreCriteria("killedByTeam." + ChatColor.DARK_RED.name().toLowerCase()), new ScoreCriteria("killedByTeam." + ChatColor.DARK_PURPLE.name().toLowerCase()), new ScoreCriteria("killedByTeam." + ChatColor.GOLD.name().toLowerCase()), new ScoreCriteria("killedByTeam." + ChatColor.GRAY.name().toLowerCase()), new ScoreCriteria("killedByTeam." + ChatColor.DARK_GRAY.name().toLowerCase()), new ScoreCriteria("killedByTeam." + ChatColor.BLUE.name().toLowerCase()), new ScoreCriteria("killedByTeam." + ChatColor.GREEN.name().toLowerCase()), new ScoreCriteria("killedByTeam." + ChatColor.AQUA.name().toLowerCase()), new ScoreCriteria("killedByTeam." + ChatColor.RED.name().toLowerCase()), new ScoreCriteria("killedByTeam." + ChatColor.LIGHT_PURPLE.name().toLowerCase()), new ScoreCriteria("killedByTeam." + ChatColor.YELLOW.name().toLowerCase()), new ScoreCriteria("killedByTeam." + ChatColor.WHITE.name().toLowerCase())};
   private final String name;
   private final boolean readOnly;
   private final ScoreCriteria.RenderType renderType;

   public ScoreCriteria(String name) {
      this(name, false, ScoreCriteria.RenderType.INTEGER);
   }

   protected ScoreCriteria(String name, boolean readOnly, ScoreCriteria.RenderType renderType) {
      this.name = name;
      this.readOnly = readOnly;
      this.renderType = renderType;
      INSTANCES.put(name, this);
   }

   public String getName() {
      return this.name;
   }

   public boolean isReadOnly() {
      return this.readOnly;
   }

   public ScoreCriteria.RenderType getRenderType() {
      return this.renderType;
   }

   public enum RenderType {
      INTEGER("integer"),
      HEARTS("hearts");

      private final String id;
      private static final Map<String, ScoreCriteria.RenderType> renderTypeById;

      RenderType(String id) {
         this.id = id;
      }

      public String getId() {
         return this.id;
      }

      public static ScoreCriteria.RenderType byId(String id) {
         return renderTypeById.getOrDefault(id, INTEGER);
      }

      static {
         Builder<String, ScoreCriteria.RenderType> builder = ImmutableMap.builder();

         for(ScoreCriteria.RenderType renderType : values()) {
            builder.put(renderType.id, renderType);
         }

         renderTypeById = builder.build();
      }
   }
}