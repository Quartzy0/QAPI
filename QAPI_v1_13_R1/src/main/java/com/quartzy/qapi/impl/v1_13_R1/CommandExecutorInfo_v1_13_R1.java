package com.quartzy.qapi.impl.v1_13_R1;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.quartzy.qapi.*;
import com.quartzy.qapi.Axis;
import com.quartzy.qapi.NamespacedKey;
import com.quartzy.qapi.command.ArgumentType;
import com.quartzy.qapi.command.CommandExecutorInfo;
import com.quartzy.qapi.command.CommandSenderInfo;
import com.quartzy.qapi.nbt.NBTPath;
import lombok.SneakyThrows;
import net.minecraft.server.v1_13_R1.*;
import net.minecraft.server.v1_13_R1.Vec2F;
import net.minecraft.server.v1_13_R1.Vec3D;
import org.bukkit.*;
import org.bukkit.Particle;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_13_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_13_R1.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.v1_13_R1.util.CraftChatMessage;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;

@SuppressWarnings("ALL")
public class CommandExecutorInfo_v1_13_R1 implements CommandExecutorInfo{
    private CommandSenderInfo_v1_13_R1 senderInfo;
    private CommandContext<CommandListenerWrapper> commandContext;
    
    private static Field argumentsField;
    
    public void setCommandContext(CommandContext<CommandListenerWrapper> commandContext){
        this.commandContext = commandContext;
        this.senderInfo = new CommandSenderInfo_v1_13_R1();
        this.senderInfo.setWrapper(commandContext.getSource());
    }
    
    @Override
    public CommandSenderInfo sender(){
        return senderInfo;
    }
    
    @SneakyThrows
    @Override
    public Object getArgument(String argumentName, ArgumentType type){
        if(argumentsField==null){
            try{
                argumentsField = CommandContext.class.getDeclaredField("arguments");
                argumentsField.setAccessible(true);
            } catch(NoSuchFieldException e){
                e.printStackTrace();
            }
        }
        
        Map<String, ParsedArgument<CommandListenerWrapper, ?>> map = null;
        try{
            map = (Map<String, ParsedArgument<CommandListenerWrapper, ?>>) argumentsField.get(commandContext);
        } catch(IllegalAccessException e){
            e.printStackTrace();
        }
        if(map==null)return null;
        
        ParsedArgument<CommandListenerWrapper, ?> parsedArgument = map.get(argumentName);
        if(parsedArgument==null)return null;
        Object result = parsedArgument.getResult();
        
        switch(type){
            case ENTITY:
                EntitySelector entitySelector3 = (EntitySelector) result;
                Entity entity = entitySelector3.a(commandContext.getSource());
                return entity.getBukkitEntity();
            case ENTITIES:
                EntitySelector entitySelector2 = (EntitySelector) result;
                List<? extends Entity> entities = entitySelector2.b(commandContext.getSource());
                List<CraftEntity> entitiesCraft = new ArrayList<>();
                for(Entity entity1 : entities){
                    entitiesCraft.add(entity1.getBukkitEntity());
                }
                return entitiesCraft;
            case PLAYERS:
                EntitySelector entitySelector1 = (EntitySelector) result;
                List<EntityPlayer> entityPlayers = entitySelector1.d(commandContext.getSource());
                List<CraftPlayer> players = new ArrayList<>();
                for(EntityPlayer entityPlayer : entityPlayers){
                    players.add(entityPlayer.getBukkitEntity());
                }
                return players;
            case PLAYER:
                EntitySelector entitySelector = (EntitySelector) result;
                EntityPlayer entityPlayer = entitySelector.c(commandContext.getSource());
                return entityPlayer.getBukkitEntity();
            case ITEM_STACK:
                ArgumentPredicateItemStack itemStack = (ArgumentPredicateItemStack) result;
                return CraftItemStack.asNewCraftStack(itemStack.a()).getType();
            case ITEM_PREDICATE:
                ArgumentItemPredicate.b itemPredicate = (ArgumentItemPredicate.b) result;
                Predicate<net.minecraft.server.v1_13_R1.ItemStack> itemStackPredicate = itemPredicate.create(commandContext);
                return (Predicate<ItemStack>) itemStack1 -> itemStackPredicate.test(CraftItemStack.asNMSCopy(itemStack1));
            case BLOCK_POS:
            case COLUMN_POS:
            case ROTATION:
            case VEC2:
            case VEC3:
                IVectorPosition iVectorPosition = (IVectorPosition) result;
                Vec3D c = iVectorPosition.a(commandContext.getSource());
                Vec2F d = iVectorPosition.b(commandContext.getSource());
                return new Location(commandContext.getSource().getWorld().getWorld(), c.x, c.y, c.z, d.i, d.j);
            case BLOCK_STATE:
                ArgumentTileLocation argumentTileLocation = (ArgumentTileLocation) result;
                return CraftBlockData.fromData(argumentTileLocation.a()).getMaterial();
            case BLOCK_PREDICATE:
                ArgumentBlockPredicate.b argumentBlockPredicate = (ArgumentBlockPredicate.b) result;
                Predicate<ShapeDetectorBlock> shapeDetectorBlockPredicate = argumentBlockPredicate.create(commandContext.getSource().getServer().getTagRegistry());
                return (Predicate<BlockState>) blockState -> {
                    Location location = blockState.getLocation();
                    BlockPosition pos = new BlockPosition(location.getX(), location.getY(), location.getZ());
                    ShapeDetectorBlock shapeDetectorBlock = new ShapeDetectorBlock(((CraftWorld) blockState.getWorld()).getHandle(), pos, false);
                    IBlockData state = ((CraftBlockData) blockState.getBlockData()).getState();
                    try{
                        Field d1 = ShapeDetectorBlock.class.getDeclaredField("d");
                        d1.setAccessible(true);
                        d1.set(shapeDetectorBlock, state);
                    } catch(NoSuchFieldException | IllegalAccessException e){
                        e.printStackTrace();
                    }
                    return shapeDetectorBlockPredicate.test(shapeDetectorBlock);
                };
            case COLOR:
                return ChatColor.getByChar(((EnumChatFormat) result).character);
            case COMPONENT:
                IChatBaseComponent iChatBaseComponent = (IChatBaseComponent) result;
                return CraftChatMessage.fromComponent(iChatBaseComponent);
            case ENTITY_SUMMON:
                return EntityType.fromName(((MinecraftKey) result).getKey());
            case ENTITY_ANCHOR:
                ArgumentAnchor.Anchor argumentAnchor = (ArgumentAnchor.Anchor) result;
                switch(argumentAnchor){
                    case EYES:
                        return EntityAnchor.EYES;
                    case FEET:
                        return EntityAnchor.FEET;
                }
            case FUNCTION:
                ArgumentTag.a tagArgument = (ArgumentTag.a) result;
                Collection<CustomFunction> a = tagArgument.create(commandContext);
                NamespacedKey[] functions = new NamespacedKey[a.size()];
                int i = 0;
                for(CustomFunction customFunction : a){
                    functions[i++] = new NamespacedKey(customFunction.a().b(), customFunction.a().getKey());
                }
                return functions;
            case GAME_PROFILE:
                Collection<GameProfile> gameProfileL = ((ArgumentProfile.a) result).getNames(commandContext.getSource());
                com.quartzy.qapi.GameProfile[] profiles1 = new com.quartzy.qapi.GameProfile[gameProfileL.size()];
                int i6 = 0;
                for(GameProfile profile : gameProfileL){
                    profiles1[i6] = new com.quartzy.qapi.GameProfile(profile.getId(), profile.getName());
                    for(Map.Entry<String, Property> entry : profile.getProperties().entries()){
                        profiles1[i6++].getProperties().put(entry.getKey(), new com.quartzy.qapi.GameProfile.Property(entry.getValue().getName(), entry.getValue().getValue(), entry.getValue().getSignature()));
                    }
                }
                return profiles1;
            case ITEM_ENCHANTMENT:
                return new CraftEnchantment((Enchantment) result);
            case MESSAGE:
                return CraftChatMessage.fromComponent(((ArgumentChat.a) result).a(commandContext.getSource(), false));
            case MESSAGE_SELECTORS:
                return CraftChatMessage.fromComponent(((ArgumentChat.a) result).a(commandContext.getSource(), true));
            case MOB_EFFECT:
                MobEffectList mobEffectList = (MobEffectList) result;
                return new CraftPotionEffectType(mobEffectList);
            case NBT_COMPOUND_TAG:
                return ((NBTProviderImpl) QAPI.nbtProvider()).fromNMS(((NBTTagCompound) result));
            case NBT_TAG:
                return ((NBTProviderImpl) QAPI.nbtProvider()).fromNMS(((NBTBase) result));
            case NBT_PATH:
                return new NBTPath(result.toString());
            case OBJECTIVE_CRITERIA:
                IScoreboardCriteria scoreboardCriteria = (IScoreboardCriteria) result;
                return ScoreCriteria.INSTANCES.get(scoreboardCriteria.getName());
            case OPERATION:
                ArgumentMathOperation.a mathOperation = (ArgumentMathOperation.a) result;
                return (IOperation) (scoreIn1, scoreIn2) -> {
                    ScoreContainer score1 = new ScoreContainer(scoreIn1);
                    ScoreContainer score2 = new ScoreContainer(scoreIn2);
                    try{
                        mathOperation.apply(score1, score2);
                    } catch(CommandSyntaxException e){
                        e.printStackTrace();
                    }
                    return score1.getScore();
                };
            case PARTICLE:
                try{
                    return Particle.valueOf(((ParticleType) result).a());
                }catch(Exception e){
                    e.printStackTrace();
                }
            case SCORE_HOLDER:
                ArgumentScoreholder.a result1 = (ArgumentScoreholder.a) result;
                Collection<String> names = result1.getNames(commandContext.getSource(), () -> {
                    Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
                    List<String> playerNames = new ArrayList<>();
                    for(Player onlinePlayer : onlinePlayers){
                        playerNames.add(onlinePlayer.getName());
                    }
                    return playerNames;
                });
                return names.toArray(new String[names.size()]);
            case SWIZZLE:
                EnumSet<EnumDirection.EnumAxis> enumAxes = (EnumSet<EnumDirection.EnumAxis>) result;
                Axis[] axis = new Axis[enumAxes.size()];
                int i1 = 0;
                for(EnumDirection.EnumAxis enumAx : enumAxes){
                    switch(enumAx){
                        case X:
                            axis[i1++] = Axis.X;
                            break;
                        case Y:
                            axis[i1++] = Axis.Y;
                            break;
                        case Z:
                            axis[i1++] = Axis.Z;
                            break;
                    }
                }
                return axis;
            case RESOURCE_LOCATION:
                return new NamespacedKey(((MinecraftKey) result).b(), ((MinecraftKey) result).getKey());
            case UUID:
                return UUID.fromString(((String) result));
            default:
                return result;
            
        }
    }
    
    @Override
    public String getCommandString(){
        return commandContext.getInput();
    }
    
    @Override
    public String getArgumentString(String argumentName){
        return this.getArgumentRange(argumentName).getTrim(this.getCommandString());
    }
    
    @Override
    public StringRange getArgumentRange(String argumentName){
        CommandContext<CommandListenerWrapper> ctx = commandContext;
        ParsedArgument<CommandListenerWrapper, ?> parsedArgument = null;
        while(ctx!=null){
            parsedArgument = getArg(argumentName, ctx);
            
            if(parsedArgument!=null) break;
            
            ctx = ctx.getChild();
        }
        if(parsedArgument==null)return null;
        
        com.mojang.brigadier.context.StringRange range = parsedArgument.getRange();
        return new StringRange(range.getStart(), range.getEnd());
    }
    
    private ParsedArgument<CommandListenerWrapper, ?> getArg(String argumentName, CommandContext<CommandListenerWrapper> context){
        if(argumentsField==null){
            try{
                argumentsField = CommandContext.class.getDeclaredField("arguments");
                argumentsField.setAccessible(true);
            } catch(NoSuchFieldException e){
                e.printStackTrace();
            }
        }
        
        Map<String, ParsedArgument<CommandListenerWrapper, ?>> map = null;
        try{
            map = (Map<String, ParsedArgument<CommandListenerWrapper, ?>>) argumentsField.get(context);
        } catch(IllegalAccessException e){
            e.printStackTrace();
        }
        if(map==null)return null;
        return map.get(argumentName);
    }
    
    private static class ScoreContainer extends ScoreboardScore{
        
        private int score;
        
        public ScoreContainer(int score){
            super(null, null, null);
            this.score = score;
        }
        
        @Override
        public void addScore(int var0){
            this.score+=var0;
        }
        
        @Override
        public void incrementScore(){
            this.score++;
        }
        
        @Override
        public int getScore(){
            return this.score;
        }
        
        @Override
        public void c(){
            this.score = 0;
        }
        
        @Override
        public void setScore(int var0){
            this.score = var0;
        }
    }
}
