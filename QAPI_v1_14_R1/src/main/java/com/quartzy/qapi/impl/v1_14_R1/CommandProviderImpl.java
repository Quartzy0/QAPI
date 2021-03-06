package com.quartzy.qapi.impl.v1_14_R1;

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import com.quartzy.qapi.command.*;
import com.quartzy.qapi.command.ArgumentType;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.CraftServer;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CommandProviderImpl implements CommandProvider{
    private final HashMap<String, LiteralCommandNode<CommandListenerWrapper>> nodes = new HashMap<>();
    private com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> dispatcher;
    private CommandDispatcher dispatcherNMS;
    
    private static Method vanillaCommandsMethod;
    private static Field argumentsField;
    
    private void setDispathcer(){
        if(dispatcher == null || dispatcherNMS==null){
            DedicatedServer server = ((CraftServer) Bukkit.getServer()).getServer();
            dispatcherNMS = server.getCommandDispatcher();
            dispatcher = dispatcherNMS.a();
        }
    }
    
    static {
        try{
            vanillaCommandsMethod = CraftServer.class.getDeclaredMethod("setVanillaCommands", boolean.class);
            vanillaCommandsMethod.setAccessible(true);
            
            argumentsField = ArgumentBuilder.class.getDeclaredField("arguments");
            argumentsField.setAccessible(true);
        } catch(NoSuchMethodException | NoSuchFieldException e){
            e.printStackTrace();
        }
    }
    
    @Override
    public void reloadCommands(){
        setDispathcer();
        try{
            vanillaCommandsMethod.invoke(Bukkit.getServer(), false);
        } catch(IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        }
        for(Player onlinePlayer : Bukkit.getOnlinePlayers()){
            EntityPlayer handle = ((CraftPlayer) onlinePlayer).getHandle();
            dispatcherNMS.a(handle);
        }
    }
    
    @Override
    public void unregisterCommand(String commandName){
        setDispathcer();
        dispatcher.getRoot().removeCommand(commandName);
        reloadCommands();
    }
    
    @Override
    public void registerCommand(LiteralNode node, String path){
        setDispathcer();
        if(path.isEmpty()){
            nodes.put(node.getName(), dispatcher.register((LiteralArgumentBuilder<CommandListenerWrapper>) addBranch(null, node)));
            return;
        }
        int endIndex = path.indexOf('.');
        endIndex = endIndex==-1 ? path.length() : endIndex;
        String[] commandNames = path.substring(0, endIndex).split("\\|");
        String commandName = commandNames[0];
        commandNames = Arrays.copyOfRange(commandNames, 1, commandNames.length);
        String[] splitPath = endIndex==path.length() ? new String[0] : path.substring(endIndex+1).split("\\.");
        LiteralCommandNode<CommandListenerWrapper> commandNode = nodes.get(commandName);
        if(commandNode!=null){
            CommandNode<CommandListenerWrapper> currentNode = commandNode;
            pathLoop:
            for(String s : splitPath){
                List<CommandNode<CommandListenerWrapper>> children = new ArrayList<>(currentNode.getChildren());
                for(CommandNode<CommandListenerWrapper> child : children){
                    if(child.getName().equals(s)){
                        currentNode = child;
                        continue pathLoop;
                    }
                }
                LiteralArgumentBuilder<CommandListenerWrapper> builder = LiteralArgumentBuilder.literal(s);
                LiteralCommandNode<CommandListenerWrapper> build = builder.build();
                currentNode.addChild(build);
                currentNode = build;
            }
            currentNode.addChild(addBranch(null, node).build());
        }else{
            LiteralNode masterNode = new LiteralNode(commandName);
            Node<?> currentNode = masterNode;
            for(String s : splitPath){
                LiteralNode node1 = new LiteralNode(s);
                currentNode.addChild(node1);
                currentNode = node1;
            }
            currentNode.addChild(node);
    
            LiteralCommandNode<CommandListenerWrapper> literalNode = dispatcher.register((LiteralArgumentBuilder<CommandListenerWrapper>) addBranch(null, masterNode));
            for(String alias : commandNames){
                LiteralArgumentBuilder<CommandListenerWrapper> aliasLiteral = LiteralArgumentBuilder.literal(alias);
                aliasLiteral.requires(literalNode.getRequirement()).redirect(literalNode);
                dispatcher.register(aliasLiteral);
            }
            nodes.put(masterNode.getName(), literalNode);
        }
        reloadCommands();
    }
    
    @Override
    public CommandSenderInfo createSenderInstance(){
        return new CommandSenderInfo_v1_14_R1();
    }
    
    @Override
    public Class<?> returnClassFromType(ArgumentType type){
        switch(type){
            case STRING_WORD:
            case STRING_GREEDY:
            case STRING:
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
                return ArgumentNBTKey.h.class;
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
    
    private ArgumentBuilder<CommandListenerWrapper, ?> addBranch(ArgumentBuilder<CommandListenerWrapper, ?> commandBuilder, Node<?> nodeArg){
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
                CommandSenderInfo_v1_14_R1 t = new CommandSenderInfo_v1_14_R1();
                t.setWrapper(commandListenerWrapper);
                return nodeArg.getRequirement().test(t);
            });
        }
        if(nodeArg.getExecutor()!=null){
            argumentBuilder.executes(commandContext -> {
                CommandExecutorInfo_v1_14_R1 t = new CommandExecutorInfo_v1_14_R1();
                t.setCommandContext(commandContext);
                return nodeArg.getExecutor().run(t);
            });
        }
        for(int i = 0; i < nodeArg.getChildren().size(); i++){
            addBranch(argumentBuilder, nodeArg.getChildren().get(i));
        }
        if(commandBuilder==null)
            return argumentBuilder;
        else {
            if(nodeArg instanceof LiteralNode && ((LiteralNode) nodeArg).getAliases()!=null){
                LiteralNode nodeArgLit = (LiteralNode) nodeArg;
            
                CommandNode<CommandListenerWrapper> build = argumentBuilder.build();
                List<CommandNode<CommandListenerWrapper>> commandNodes = new ArrayList<>(nodeArgLit.getAliases().length+1);
                commandNodes.add(build);
            
                for(String alias : nodeArgLit.getAliases()){
                    commandNodes.add(new LiteralCommandNode<>(alias, build.getCommand(), build.getRequirement(), build, null, false));
                }
                try{
                    for(CommandNode<CommandListenerWrapper> commandNode : commandNodes){
                        ((RootCommandNode<CommandListenerWrapper>) argumentsField.get(commandBuilder)).addChild(commandNode);
                    }
                } catch(IllegalAccessException e){
                    e.printStackTrace();
                }
            }else{
                commandBuilder.then(argumentBuilder);
            }
            return commandBuilder;
        }
    }
    
    public com.mojang.brigadier.arguments.ArgumentType<?> toNMSArgument(ArgumentNode argumentType){
        switch(argumentType.getType()){
            case STRING_WORD:
            case UUID:
                return StringArgumentType.word();
            case STRING_GREEDY:
                return StringArgumentType.greedyString();
            case STRING:
                return StringArgumentType.string();
            case INTEGER:
                return IntegerArgumentType.integer(argumentType.getMinI(), argumentType.getMaxI());
            case LONG:
                return LongArgumentType.longArg(argumentType.getMinL(), argumentType.getMaxL());
            case FLOAT:
                return FloatArgumentType.floatArg(argumentType.getMinF(), argumentType.getMaxF());
            case DOUBLE:
                return DoubleArgumentType.doubleArg(argumentType.getMinD(), argumentType.getMaxD());
            case BOOLEAN:
                return BoolArgumentType.bool();
            case ENTITY:
                return net.minecraft.server.v1_14_R1.ArgumentEntity.a();
            case ENTITIES:
                return net.minecraft.server.v1_14_R1.ArgumentEntity.multipleEntities();
            case PLAYER:
                return net.minecraft.server.v1_14_R1.ArgumentEntity.c();
            case PLAYERS:
                return net.minecraft.server.v1_14_R1.ArgumentEntity.d();
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
                return ArgumentNBTTag.a();
            case NBT_TAG:
                return ArgumentNBTBase.a();
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
                return ArgumentTime.a();
            case GAME_PROFILE:
                return ArgumentProfile.a();
        }
        throw new IllegalArgumentException("Unable to find argument NMS type for argument type " + argumentType.getType());
    }
}
