package com.quartzy.qapi.impl.v1_16_R3;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.quartzy.qapi.*;
import com.quartzy.qapi.Axis;
import com.quartzy.qapi.command.ArgumentTypeEnum;
import com.quartzy.qapi.command.CommandExecutorInfo;
import com.quartzy.qapi.command.CommandSenderInfo;
import lombok.SneakyThrows;
import net.minecraft.server.v1_16_R3.*;
import net.minecraft.server.v1_16_R3.AttributeModifier;
import net.minecraft.server.v1_16_R3.Vec2F;
import net.minecraft.server.v1_16_R3.Vec3D;
import org.bukkit.*;
import org.bukkit.Particle;
import org.bukkit.block.*;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_16_R3.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

public class CommandExecutorInfo_v1_16_R3 implements CommandExecutorInfo{
    private CommandSenderInfo_v1_16_R3 senderInfo;
    private CommandContext<CommandListenerWrapper> commandContext;
    
    private static Field argumentsField;
    
    private static Field mobEffectField;
    private static Method mobEffectNameMethod;
    private static Field mobEffectAttributesField;
    
    public void setCommandContext(CommandContext<CommandListenerWrapper> commandContext){
        this.commandContext = commandContext;
        this.senderInfo = new CommandSenderInfo_v1_16_R3();
        this.senderInfo.setWrapper(commandContext.getSource());
    }
    
    @Override
    public CommandSenderInfo sender(){
        return senderInfo;
    }
    
    @SneakyThrows
    @Override
    public Object getArgument(String argumentName, ArgumentTypeEnum type){
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
                List<? extends Entity> entities = entitySelector2.getEntities(commandContext.getSource());
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
            case MATERIAL:
                ArgumentPredicateItemStack itemStack = (ArgumentPredicateItemStack) result;
                return CraftItemStack.asNewCraftStack(itemStack.a()).getType();
            case ITEM_PREDICATE:
                ArgumentItemPredicate.b itemPredicate = (ArgumentItemPredicate.b) result;
                Predicate<net.minecraft.server.v1_16_R3.ItemStack> itemStackPredicate = itemPredicate.create(commandContext);
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
                return CraftBlockData.fromData(argumentTileLocation.a());
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
            case ANGLE:
                ArgumentAngle.a argumentAngle = (ArgumentAngle.a) result;
                return argumentAngle.a(commandContext.getSource());
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
                Collection<CustomFunction> a = tagArgument.a(commandContext);
                NamespacedKey[] functions = new NamespacedKey[a.size()];
                int i = 0;
                for(CustomFunction customFunction : a){
                    functions[i++] = CraftNamespacedKey.fromMinecraft(customFunction.a());
                }
                return functions;
            case GAME_PROFILE:
                Collection<GameProfile> gameProfileL = ((ArgumentProfile.a) result).getNames(commandContext.getSource());
                com.quartzy.qapi.GameProfile[] gameprofiles = new com.quartzy.qapi.GameProfile[gameProfileL.size()];
                int i2 = 0;
                for(GameProfile gameProfile : gameProfileL){
                    com.quartzy.qapi.GameProfile gameprofile = new com.quartzy.qapi.GameProfile(gameProfile.getId(), gameProfile.getName());
                    for(Map.Entry<String, Property> entry : gameProfile.getProperties().entries()){
                        gameprofile.getProperties().put(entry.getKey(), new com.quartzy.qapi.GameProfile.Property(entry.getValue().getName(), entry.getValue().getValue(), entry.getValue().getSignature()));
                    }
                    gameprofiles[i2++] = gameprofile;
                }
                return gameprofiles;
            case ITEM_ENCHANTMENT:
                return new CraftEnchantment((Enchantment) result);
            case MESSAGE:
                return CraftChatMessage.fromComponent(((ArgumentChat.a) result).a(commandContext.getSource(), false));
            case MESSAGE_SELECTORS:
                return CraftChatMessage.fromComponent(((ArgumentChat.a) result).a(commandContext.getSource(), true));
            case MOB_EFFECT:
                if(mobEffectField==null){
                    mobEffectField = MobEffectList.class.getDeclaredField("b");
                    mobEffectField.setAccessible(true);
                }
                if(mobEffectAttributesField==null){
                    mobEffectAttributesField = MobEffectList.class.getDeclaredField("a");
                    mobEffectAttributesField.setAccessible(true);
                }
                
                MobEffectList mobEffectList = (MobEffectList) result;
                return new CraftPotionEffectType(mobEffectList);
                /*EffectType effectType = EffectType.NEUTRAL;
                MobEffectInfo o = (MobEffectInfo) mobEffectField.get(mobEffectList);
                switch(o){
                    case BENEFICIAL:
                        effectType = EffectType.BENEFICIAL;
                        break;
                    case HARMFUL:
                        effectType = EffectType.HARMFUL;
                        break;
                    case NEUTRAL:
                        effectType = EffectType.NEUTRAL;
                        break;
                }
                com.quartzy.qapi.Effect effect = new com.quartzy.qapi.Effect(effectType, mobEffectList.getColor(), getMobEffectName(mobEffectList));
                Map<AttributeBase, AttributeModifier> attributeMap = (Map<AttributeBase, AttributeModifier>) mobEffectAttributesField.get(mobEffectList);
                for(Map.Entry<AttributeBase, AttributeModifier> entry : attributeMap.entrySet()){
                    Attribute attribute = new Attribute(entry.getKey().getName(), entry.getKey().getDefault()).setShouldWatch(entry.getKey().b());
                    AttributeModifier.Operation operation = entry.getValue().getOperation();
                    com.quartzy.qapi.AttributeModifier.Operation operation1 = com.quartzy.qapi.AttributeModifier.Operation.ADDITION;
                    switch(operation){
                        case ADDITION:
                            operation1 = com.quartzy.qapi.AttributeModifier.Operation.ADDITION;
                            break;
                        case MULTIPLY_BASE:
                            operation1 = com.quartzy.qapi.AttributeModifier.Operation.MULTIPLY_BASE;
                            break;
                        case MULTIPLY_TOTAL:
                            operation1 = com.quartzy.qapi.AttributeModifier.Operation.MULTIPLY_TOTAL;
                            break;
                    }
                    com.quartzy.qapi.AttributeModifier attributeModifier = new com.quartzy.qapi.AttributeModifier(entry.getValue().getUniqueId(), entry.getValue().getName(), entry.getValue().getAmount(), operation1);
                    effect.getAttributeModifierMap().put(attribute, attributeModifier);
                }
                return effect;*/
            case NBT_COMPOUND_TAG:
                return ((NBTProviderImpl) QAPI.nbtProvider()).fromNMS(((NBTTagCompound) result));
            case NBT_TAG:
                return ((NBTProviderImpl) QAPI.nbtProvider()).fromNMS(((NBTBase) result));
            case NBT_PATH:
                return result.toString();
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
                return CraftNamespacedKey.fromMinecraft(((MinecraftKey) result));
            default:
                return result;
            
        }
    }
    
    private static String getMobEffectName(MobEffectList effect){
        if(mobEffectNameMethod==null){
            try{
                mobEffectNameMethod = MobEffectList.class.getDeclaredMethod("b");
                mobEffectNameMethod.setAccessible(true);
            } catch(NoSuchMethodException e){
                e.printStackTrace();
            }
        }
        try{
            return (String) mobEffectNameMethod.invoke(effect);
        } catch(IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        }
        return null;
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
