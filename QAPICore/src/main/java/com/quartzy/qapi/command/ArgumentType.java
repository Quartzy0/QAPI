package com.quartzy.qapi.command;

import com.quartzy.qapi.Version;
import org.jetbrains.annotations.Nullable;

public enum ArgumentType{
        BOOLEAN, FLOAT, DOUBLE, INTEGER, LONG, GAME_PROFILE, BLOCK_POS, COLUMN_POS, VEC3, VEC2, BLOCK_STATE, BLOCK_PREDICATE(Version.v1_13_R1),
        ITEM_STACK, ITEM_PREDICATE(Version.v1_13_R1), COLOR, COMPONENT, MESSAGE, MESSAGE_SELECTORS, NBT_COMPOUND_TAG, NBT_TAG, NBT_PATH, OBJECTIVE, OBJECTIVE_CRITERIA, OPERATION,
        PARTICLE, ANGLE, ROTATION, SCOREBOARD_SLOT, SCORE_HOLDER, SWIZZLE, TEAM, ITEM_SLOT, RESOURCE_LOCATION, MOB_EFFECT, FUNCTION(Version.v1_13_R1), ENTITY_ANCHOR,
        ITEM_ENCHANTMENT, ENTITY_SUMMON, DIMENSION, TIME, UUID, STRING_WORD, STRING_GREEDY, PLAYER, PLAYERS, ENTITIES, ENTITY,
        STRING;
        
        @Nullable
        public final Version minVersion;
        
        ArgumentType(@Nullable Version minVersion){
                this.minVersion = minVersion;
        }
        
        ArgumentType(){
                this.minVersion = null;
        }
}