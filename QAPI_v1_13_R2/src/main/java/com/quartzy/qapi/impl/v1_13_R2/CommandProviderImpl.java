package com.quartzy.qapi.impl.v1_13_R2;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.quartzy.qapi.command.*;
import net.minecraft.server.v1_13_R2.*;
import net.minecraft.server.v1_13_R2.ArgumentBlockPredicate;
import net.minecraft.server.v1_13_R2.ArgumentItemPredicate;
import net.minecraft.server.v1_13_R2.ArgumentItemStack;
import net.minecraft.server.v1_13_R2.ArgumentVec2I;
import net.minecraft.server.v1_13_R2.ArgumentVec3;
import net.minecraft.server.v1_13_R2.EntitySelector;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;

import java.util.EnumSet;
import java.util.UUID;

public class CommandProviderImpl implements CommandProvider{
    @Override
    public void registerCommand(LiteralNode node){
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        CommandDispatcher commandDispatcher = server.getCommandDispatcher();
        com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> a = commandDispatcher.a();
        a.register((LiteralArgumentBuilder<CommandListenerWrapper>) addBranch(null, node));
    }
    
    @Override
    public void unregisterCommand(String commandName){
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        CommandDispatcher commandDispatcher = server.getCommandDispatcher();
        com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> a = commandDispatcher.a();
        a.getRoot().removeCommand(commandName);
    }
    
    @Override
    public CommandSenderInfo createSenderInstance(){
        return new CommandSenderInfo_v1_13_R2();
    }
    
    @Override
    public Class<?> returnClassFromType(ArgumentTypeEnum type){
        switch(type){
            case STRING_WORD:
            case STRING_GREEDY:
            case STRING_STRING:
            case OBJECTIVE:
            case TEAM:
                return String.class;
            case INTEGER:
            case ITEM_SLOT:
            case TIME:
            case SCOREBOARD_SLOT:
            case ANGLE:
                return Integer.class;
            case LONG:
                return Long.class;
            case DOUBLE:
                return Double.class;
            case FLOAT:
                return Float.class;
            case ENTITY:
            case ENTITIES:
            case PLAYER:
            case PLAYERS:
                return EntitySelector.class;
            case ITEM_STACK:
                return ArgumentPredicateItemStack.class;
            case ITEM_PREDICATE:
                return ArgumentItemPredicate.b.class;
            case BLOCK_POS:
            case COLUMN_POS:
            case ROTATION:
            case VEC2:
            case VEC3:
                return IVectorPosition.class;
            case BLOCK_STATE:
                return ArgumentTileLocation.class;
            case BLOCK_PREDICATE:
                return ArgumentBlockPredicate.b.class;
            case COLOR:
                return EnumChatFormat.class;
            case BOOLEAN:
                return Boolean.class;
            case COMPONENT:
                return IChatBaseComponent.class;
            case DIMENSION:
            case ENTITY_SUMMON:
            case RESOURCE_LOCATION:
                return MinecraftKey.class;
            case ENTITY_ANCHOR:
                return ArgumentAnchor.Anchor.class;
            case FUNCTION:
                return ArgumentTag.a.class;
            case GAME_PROFILE:
                return ArgumentProfile.a.class;
            case ITEM_ENCHANTMENT:
                return Enchantment.class;
            case MESSAGE:
            case MESSAGE_SELECTORS:
                return ArgumentChat.a.class;
            case MOB_EFFECT:
                return MobEffectList.class;
            case NBT_COMPOUND_TAG:
                return NBTTagCompound.class;
            case NBT_PATH:
                return ArgumentNBTKey.c.class;
            case NBT_TAG:
                return NBTBase.class;
            case OBJECTIVE_CRITERIA:
                return IScoreboardCriteria.class;
            case OPERATION:
                return ArgumentMathOperation.a.class;
            case PARTICLE:
                return ParticleParam.class;
            case SCORE_HOLDER:
                return ArgumentScoreholder.a.class;
            case SWIZZLE:
                return EnumSet.class;
            case UUID:
                return UUID.class;
        }
        return null;
    }
    
    private ArgumentBuilder<CommandListenerWrapper, ?> addBranch(ArgumentBuilder<CommandListenerWrapper, ?> commandBuilder, Node nodeArg){
        if(nodeArg==null || nodeArg.getName()==null || nodeArg.getName().isEmpty()){
            throw new NullPointerException("Node cannot be null or have an empty name");
        }
        ArgumentBuilder<CommandListenerWrapper, ?> argumentBuilder;
        if(nodeArg instanceof ArgumentNode){
            argumentBuilder = CommandDispatcher.a(nodeArg.getName(), toNMSArgument(((ArgumentNode) nodeArg)));
        }else{
            argumentBuilder = CommandDispatcher.a(nodeArg.getName());
        }
        if(nodeArg.getRequirement()!=null){
            argumentBuilder.requires(commandListenerWrapper -> {
                CommandSenderInfo_v1_13_R2 t = new CommandSenderInfo_v1_13_R2();
                t.setWrapper(commandListenerWrapper);
                return nodeArg.getRequirement().test(t);
            });
        }
        if(nodeArg.getExecutor()!=null){
            argumentBuilder.executes(commandContext -> {
                CommandExecutorInfo_v1_13_R2 t = new CommandExecutorInfo_v1_13_R2();
                t.setCommandContext(commandContext);
                return nodeArg.getExecutor().run(t);
            });
        }
        for(int i = 0; i < nodeArg.getChildren().size(); i++){
            addBranch(argumentBuilder, ((Node) nodeArg.getChildren().get(i)));
        }
        if(commandBuilder==null) return argumentBuilder;
        else {
            commandBuilder.then(argumentBuilder);
            return commandBuilder;
        }
    }
    
    public ArgumentType toNMSArgument(ArgumentNode argumentType){
        switch(argumentType.getType()){
            case STRING_WORD:
            case UUID:
                return StringArgumentType.word();
            case STRING_GREEDY:
                return StringArgumentType.greedyString();
            case STRING_STRING:
                return StringArgumentType.string();
            case INTEGER:
                return IntegerArgumentType.integer(argumentType.getMinI(), argumentType.getMaxI());
            case LONG:
                Bukkit.getLogger().warning("WARNING: Long argument type is not supported on this version! It will be automatically converted to an integer which will not allow numbers larger than an integer to be input by the user. An error might also be generated if the max or the min values are larger than Integer.MAX_VALUE (" + Integer.MAX_VALUE + ") or smaller than Integer.MIN_VALUE (" + Integer.MIN_VALUE + ") (argument '" + argumentType.getName() + "')");
                return IntegerArgumentType.integer((int) (argumentType.getMinL()==Long.MIN_VALUE ? Integer.MIN_VALUE : argumentType.getMinL()), (int) (argumentType.getMaxL()==Long.MAX_VALUE ? Integer.MAX_VALUE : argumentType.getMaxL()));
            case FLOAT:
                return FloatArgumentType.floatArg(argumentType.getMinF(), argumentType.getMaxF());
            case DOUBLE:
                return DoubleArgumentType.doubleArg(argumentType.getMinD(), argumentType.getMaxD());
            case BOOLEAN:
                return BoolArgumentType.bool();
            case ENTITY:
                return net.minecraft.server.v1_13_R2.ArgumentEntity.a();
            case ENTITIES:
                return net.minecraft.server.v1_13_R2.ArgumentEntity.b();
            case PLAYER:
                return net.minecraft.server.v1_13_R2.ArgumentEntity.c();
            case PLAYERS:
                return net.minecraft.server.v1_13_R2.ArgumentEntity.d();
            case BLOCK_POS:
                return ArgumentPosition.a();
            case COLUMN_POS:
                return ArgumentVec2I.a();
            case VEC3:
                return ArgumentVec3.a();
            case VEC2:
                return ArgumentVec2.a();
            case BLOCK_STATE:
                return ArgumentTile.a();
            case BLOCK_PREDICATE:
                return ArgumentBlockPredicate.a();
            case ITEM_STACK:
                return ArgumentItemStack.a();
            case ITEM_PREDICATE:
                return ArgumentItemPredicate.a();
            case COLOR:
                return ArgumentChatFormat.a();
            case COMPONENT:
                return ArgumentChatComponent.a();
            case MESSAGE:
            case MESSAGE_SELECTORS:
                return ArgumentChat.a();
            case NBT_COMPOUND_TAG:
            case NBT_TAG:
                return ArgumentNBTTag.a();
            case NBT_PATH:
                return ArgumentNBTKey.a();
            case OBJECTIVE:
                return ArgumentScoreboardObjective.a();
            case OBJECTIVE_CRITERIA:
                return ArgumentScoreboardCriteria.a();
            case OPERATION:
                return ArgumentMathOperation.a();
            case PARTICLE:
                return ArgumentParticle.a();
            case ANGLE:
                return FloatArgumentType.floatArg(-180, 180);
            case ROTATION:
                return ArgumentRotation.a();
            case SCOREBOARD_SLOT:
                return ArgumentScoreboardSlot.a();
            case SCORE_HOLDER:
                return ArgumentScoreholder.a();
            case SWIZZLE:
                return ArgumentRotationAxis.a();
            case TEAM:
                return ArgumentScoreboardTeam.a();
            case ITEM_SLOT:
                return ArgumentInventorySlot.a();
            case RESOURCE_LOCATION:
                return ArgumentMinecraftKeyRegistered.a();
            case MOB_EFFECT:
                return ArgumentMobEffect.a();
            case FUNCTION:
                return ArgumentTag.a();
            case ENTITY_ANCHOR:
                return ArgumentAnchor.a();
            case ITEM_ENCHANTMENT:
                return ArgumentEnchantment.a();
            case ENTITY_SUMMON:
                return ArgumentEntitySummon.a();
            case DIMENSION:
                return ArgumentDimension.a();
            case TIME:
                return IntegerArgumentType.integer(0);
            case GAME_PROFILE:
                return ArgumentProfile.a();
        }
        throw new IllegalArgumentException("Unable to find argument NMS type for argument type " + argumentType.getType());
    }
}
