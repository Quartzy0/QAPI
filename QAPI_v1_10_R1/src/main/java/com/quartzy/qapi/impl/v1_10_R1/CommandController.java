package com.quartzy.qapi.impl.v1_10_R1;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.quartzy.qapi.*;
import com.quartzy.qapi.command.ArgumentNode;
import com.quartzy.qapi.command.ArgumentType;
import com.quartzy.qapi.command.LiteralNode;
import com.quartzy.qapi.command.Node;
import com.quartzy.qapi.nbt.NBTPath;
import net.minecraft.server.v1_10_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.craftbukkit.v1_10_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_10_R1.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.v1_10_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings("ALL")
public class CommandController extends org.bukkit.command.Command{
    private static Method mojangsonParse;
    private static Method mojangsonTypeParse;
    private static final char[] nbtNestingChars = new char[]{'{', '}', '[', ']'};
    private static final char[] nbtStringChars = new char[]{'"', '\''};
    
    static{
        try{
            mojangsonParse = MojangsonParser.class.getDeclaredMethod("a", String.class, String.class);
            mojangsonParse.setAccessible(true);
            mojangsonTypeParse = Class.forName("net.minecraft.server.v1_10_R1.MojangsonParser$MojangsonTypeParser").getDeclaredMethod("a");
            mojangsonTypeParse.setAccessible(true);
        } catch(NoSuchMethodException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    
    private final LiteralNode commandNode;
    private Node<?> finalNode;
    private Node<?> lastNode;
    private int lastOffset;
    private List<String> suggestions = new ArrayList<>();
    private final HashMap<String, ParsedArgument> arguments = new HashMap<>();
    
    public CommandController(LiteralNode commandNode){
        super(commandNode.getName());
        this.commandNode = commandNode;
    }
    
    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args){
        StringBuilder argsStr = new StringBuilder();
        for(int i = 0; i < args.length; i++){
            if(i==args.length-1) argsStr.append(args[i]);
            else argsStr.append(args[i]).append(" ");
        }
        String fullString = "/" + s + " " + argsStr;
        CommandExecutorInfo_v1_10_R1 executorInfo = new CommandExecutorInfo_v1_10_R1(new CommandSenderInfo_v1_10_R1(commandSender, commandNode), null, fullString, argsStr.toString());
        finalNode = commandNode;
        try{
            parse(commandNode, argsStr.toString(), 0, executorInfo, true);
        }catch(com.quartzy.qapi.command.CommandException e){
            executorInfo.setArguments(arguments);
            e.send(executorInfo);
            return true;
        }
        executorInfo.setArguments(arguments);
        
        if(finalNode==null || finalNode.getExecutor()==null){
            new com.quartzy.qapi.command.CommandException(fullString.length(), "Expected argument").send(executorInfo);
            return true;
        }
    
        if(!finalNode.getRequirement().test(executorInfo.getSender())){
            new com.quartzy.qapi.command.CommandException(-1, "You do not have permission to run this command").send(executorInfo);
            return true;
        }
    
        finalNode.getExecutor().run(executorInfo);
        return true;
    }
    
    public void parse(Node<?> node, String args, int offset, CommandExecutorInfo_v1_10_R1 executorInfo, boolean rethrow) throws com.quartzy.qapi.command.CommandException{
        lastNode = node;
        lastOffset = offset;
        if(args.length()<=offset)return;
        ICommandListener listener = executorInfo.getSender().getListener();
        for(int j = 0; j < node.getChildren().size() ; j++){
            finalNode = node.getChildren().get(j);
            if(finalNode instanceof ArgumentNode){
                ArgumentNode argumentNode = (ArgumentNode) finalNode;
                ArgumentType type = argumentNode.getType();
                StringRange range = new StringRange(offset, -1);
                ParsedArgument argument = new ParsedArgument(null, range, argumentNode.getName());
                try{
                    switch(type){
                        case LONG:
                            range.setEnd(indexOf(args, offset));
                            argument.value = CommandAbstract.a(range.getTrim(args), argumentNode.getMinL(), argumentNode.getMaxL());
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case DIMENSION:
                        case INTEGER:
                            range.setEnd(indexOf(args, offset));
                            argument.value = CommandAbstract.a(range.getTrim(args), argumentNode.getMinI(), argumentNode.getMaxI());
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case FLOAT:
                        case ANGLE:
                            range.setEnd(indexOf(args, offset));
                            argument.value = (float) CommandAbstract.a(range.getTrim(args), argumentNode.getMinF(), argumentNode.getMaxF());
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case DOUBLE:
                            range.setEnd(indexOf(args, offset));
                            argument.value = CommandAbstract.a(range.getTrim(args), argumentNode.getMinD(), argumentNode.getMaxD());
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case COLOR:
                            range.setEnd(indexOf(args, offset));
                            ChatColor byChar = ChatColor.getByChar(range.getTrim(args));
                            if(byChar == null){
                                throw new CommandException("The color input is not valid");
                            }
                            argument.value = byChar;
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case BOOLEAN:
                            range.setEnd(indexOf(args, offset));
                            argument.value = CommandAbstract.d(range.getTrim(args));
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case OPERATION:
                            range.setEnd(indexOf(args, offset));
                            IOperation operation = null;
                            String s = range.getTrim(args);
                            switch(s){
                                case "=":
                                    operation = (i1, i2) -> i2;
                                    break;
                                case "<":
                                    operation = Math::min;
                                    break;
                                case ">":
                                    operation = Math::max;
                                    break;
                                case "%=":
                                    operation = Math::floorMod;
                                    break;
                                case "*=":
                                    operation = (i1, i2) -> i1 * i2;
                                    break;
                                case "/=":
                                    operation = Math::floorDiv;
                                    break;
                                case "+=":
                                    operation = Integer::sum;
                                    break;
                                case "-=":
                                    operation = (i1, i2) -> i1 - i2;
                                    break;
                            }
                            argument.value = operation;
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case BLOCK_POS:
                        case VEC3:
                            range.setEnd(indexOf(args, offset, 3));
                            String[] splitPos = split(range.getTrim(args), 3);
                            BlockPosition senderPos = listener.getChunkCoordinates();
                            argument.value = new Location(executorInfo.getSender().getWorld(), CommandAbstract.b(senderPos.getX(), splitPos[0], -30000000, 30000000, false), CommandAbstract.b(senderPos.getX(), splitPos[1], -30000000, 30000000, false), CommandAbstract.b(senderPos.getZ(), splitPos[2], -30000000, 30000000, false));
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case VEC2:
                        case COLUMN_POS:
                            range.setEnd(indexOf(args, offset, 2));
                            String[] split = split(range.getTrim(args), 2);
                            BlockPosition var4 = listener.getChunkCoordinates();
                            argument.value = new Location(executorInfo.getSender().getWorld(), CommandAbstract.b(var4.getX(), split[0], -30000000, 30000000, false), 0, CommandAbstract.b(var4.getZ(), split[1], -30000000, 30000000, false));
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case ITEM_SLOT:
                            range.setEnd(indexOf(args, offset, 1));
                            argument.value = parseItemSlot(range.getTrim(args));
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case RESOURCE_LOCATION:
                            range.setEnd(indexOf(args, offset, 1));
                            argument.value = new NamespacedKey(range.getTrim(args));
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case STRING_WORD:
                        case TEAM:
                            range.setEnd(indexOf(args, offset, 1));
                            argument.value = range.getTrim(args);
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case MESSAGE:
                        case STRING_GREEDY:
                            range.setEnd(args.length());
                            argument.value = range.getTrim(args);
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case STRING:
                            range.setEnd(args.indexOf('"', offset));
                            argument.value = range.getTrim(args);
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case ITEM_STACK:
                            range.setEnd(indexOf(args, offset, 1));
                            Item item = CommandAbstract.a(listener, range.getTrim(args));
                            argument.value = CraftItemStack.asNewCraftStack(item, 1).getType();
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case UUID:
                            range.setEnd(indexOf(args, offset, 1));
                            argument.value = UUID.fromString(range.getTrim(args));
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case ROTATION:
                            range.setEnd(indexOf(args, offset, 2));
                            String[] s1 = range.getTrim(args).split(" ");
                            float pitch = ((Entity) listener).pitch;
                            float yaw = ((Entity) listener).yaw;
                            BlockPosition chunkCoordinates = listener.getChunkCoordinates();
                            pitch = (float) CommandAbstract.b(pitch, s1[1], Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                            yaw = (float) CommandAbstract.b(yaw, s1[0], Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                            argument.value = new Location(executorInfo.sender().getWorld(), chunkCoordinates.getX(), chunkCoordinates.getY(), chunkCoordinates.getZ(), yaw, pitch);
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case ENTITY:
                            range.setEnd(indexOf(args, offset, 1));
                            Entity b = CommandAbstract.b(((CraftServer) Bukkit.getServer()).getServer(), listener, range.getTrim(args));
                            argument.value = b.getBukkitEntity();
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case ENTITIES:
                            range.setEnd(indexOf(args, offset, 1));
                            List<?> entities = CommandAbstract.c(((CraftServer) Bukkit.getServer()).getServer(), listener, range.getTrim(args));
                            List<CraftEntity> craftEntities = new ArrayList<>(entities.size());
                            for(Object entity : entities){
                                craftEntities.add(((Entity) entity).getBukkitEntity());
                            }
                            argument.value = craftEntities;
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case PLAYER:
                            range.setEnd(indexOf(args, offset, 1));
                            EntityPlayer player = CommandAbstract.a(((CraftServer) Bukkit.getServer()).getServer(), listener, range.getTrim(args));
                            argument.value = player.getBukkitEntity();
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case PLAYERS:
                            range.setEnd(indexOf(args, offset, 1));
                            List<EntityPlayer> players = getPlayers(listener, range.getTrim(args));
                            List<CraftPlayer> craftPlayers = new ArrayList<>(players.size());
                            for(EntityPlayer entityPlayer : players){
                                craftPlayers.add(entityPlayer.getBukkitEntity());
                            }
                            argument.value = craftPlayers;
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case TIME:
                            range.setEnd(indexOf(args, offset, 1));
                            String s2 = range.getTrim(args);
                            char modifier = s2.charAt(s2.length() - 1);
                            int i1;
                            if(!Character.isDigit(modifier)){
                                i1 = CommandAbstract.a(s2.substring(0, s2.length() - 1), 0);
                                switch(modifier){
                                    case 's':
                                        i1 *= 20;
                                        break;
                                    case 'd':
                                        i1 *= 24000;
                                        break;
                                }
                            } else{
                                i1 = CommandAbstract.a(s2, 0);
                            }
                            argument.value = i1;
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case SWIZZLE:
                            range.setEnd(indexOf(args, offset, 1));
                            List<Axis> axes = new ArrayList<>(3);
                            String s3 = range.getTrim(args);
                            for(int i2 = 0; i2 < s3.length(); i2++){
                                switch(s3.charAt(i2)){
                                    case 'x':
                                        axes.add(Axis.X);
                                        break;
                                    case 'y':
                                        axes.add(Axis.Y);
                                        break;
                                    case 'z':
                                        axes.add(Axis.Z);
                                        break;
                                }
                            }
                            argument.value = axes.toArray(new Axis[axes.size()]);
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case SCOREBOARD_SLOT:
                            range.setEnd(indexOf(args, offset, 1));
                            argument.value = Scoreboard.getSlotForName(range.getTrim(args));
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case BLOCK_STATE:
                            range.setEnd(indexOf(args, offset, 1));
                            Block g = CommandAbstract.b(listener, range.getTrim(args));
                            argument.value = Material.getMaterial(Block.getId(g));
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case ITEM_ENCHANTMENT:
                            range.setEnd(indexOf(args, offset, 1));
                            Enchantment ench;
                            try {
                                ench = Enchantment.c(CommandAbstract.a(range.getTrim(args), 0));
                            } catch (Exception e) {
                                ench = Enchantment.b(range.getTrim(args));
                                if (ench == null) {
                                    throw e;
                                }
                            }
                            argument.value = new CraftEnchantment(ench);
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case PARTICLE:
                            range.setEnd(indexOf(args, offset, 1));
                            EnumParticle particle = null;
                            EnumParticle[] particles = EnumParticle.values();
                            String particleS = range.getTrim(args);
    
                            for(EnumParticle enumParticle : particles){
                                if(enumParticle.e()){
                                    if(particleS.startsWith(enumParticle.b())){
                                        particle = enumParticle;
                                        break;
                                    }
                                } else{
                                    if(particleS.equals(enumParticle.b())){
                                        particle = enumParticle;
                                        break;
                                    }
                                }
                            }
                            if(particle==null){
                                throw new com.quartzy.qapi.command.CommandException(argumentNode.getName(), true, "commands.particle.notFound", particleS, new Object[0]);
                            }
                            argument.value = Particle.fromLegacy(particle.name());
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case MESSAGE_SELECTORS:
                            range.setEnd(args.length());
                            String message = range.getTrim(args);
                            int i = 0;
                            while((i = message.indexOf('@', i))!=-1){
                                String selector = message.substring(i, indexOf(message, i));
                                String playerNames = getPlayerNames(listener, selector);
                                message = message.replace(selector, playerNames == null ? "nobody" : playerNames);
                                i+=selector.length();
                            }
                            argument.value = message;
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case SCORE_HOLDER:
                            range.setEnd(indexOf(args, offset, 1));
                            ArrayList<String> names = new ArrayList<>();
                            if(range.getTrim(args).equals("*")){
                                for(Player onlinePlayer : Bukkit.getOnlinePlayers()){
                                    names.add(onlinePlayer.getDisplayName());
                                }
                            }else{
                                String str = range.getTrim(args);
                                List<?> playersSc = PlayerSelector.getPlayers(listener, str, EntityPlayer.class);
                                if (playersSc == null) {
                                    try {
                                        playersSc = Collections.singletonList(MinecraftServer.getServer().getPlayerList().a(UUID.fromString(str)));
                                    } catch (IllegalArgumentException ignored) {
                                    }
                                }
    
                                if (playersSc == null) {
                                    playersSc = Collections.singletonList(MinecraftServer.getServer().getPlayerList().getPlayer(str));
                                }
    
                                if (playersSc.isEmpty()) {
                                    throw new ExceptionPlayerNotFound();
                                } else {
                                    for(Object entityPlayer : playersSc){
                                        names.add(((EntityPlayer) entityPlayer).getName());
                                    }
                                }
                            }
                            argument.value = names;
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case NBT_COMPOUND_TAG:
                            range.setEnd(findClosingIndex(args, offset));
                            argument.value = ((NBTProviderImpl) QAPI.nbtProvider()).fromNMS(MojangsonParser.parse(range.getTrim(args)));
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case NBT_TAG:
                            range.setEnd(findClosingIndex(args, offset));
                            Object tag = mojangsonParse.invoke(null, "tag", range.getTrim(args));
                            argument.value = ((NBTProviderImpl) QAPI.nbtProvider()).fromNMS(((NBTBase) mojangsonTypeParse.invoke(tag)));
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case FUNCTION:
                            range.setEnd(indexOf(args, offset));
                            throw new com.quartzy.qapi.command.CommandException(finalNode.getName(), false, "Functions are not supported in this version of minecraft");
                        case GAME_PROFILE:
                            range.setEnd(indexOf(args, offset));
                            String str = range.getTrim(args);
                            List<GameProfile> profiles = new ArrayList<>();
                            if(PlayerSelector.isPattern(str)){
                                List<?> players1 = PlayerSelector.getPlayers(listener, str, EntityPlayer.class);
                                for(Object entityPlayer : players1){
                                    profiles.add(((EntityPlayer) entityPlayer).getProfile());
                                }
                            }else{
                                profiles.add(MinecraftServer.getServer().getUserCache().getProfile(str));
                            }
                            com.quartzy.qapi.GameProfile[] profiles1 = new com.quartzy.qapi.GameProfile[profiles.size()];
                            for(int i2 = 0; i2 < profiles.size(); i2++){
                                profiles1[i2] = new com.quartzy.qapi.GameProfile(profiles.get(i2).getId(), profiles.get(i2).getName());
                                for(Map.Entry<String, Property> entry : profiles.get(i2).getProperties().entries()){
                                    profiles1[i2].getProperties().put(entry.getKey(), new com.quartzy.qapi.GameProfile.Property(entry.getValue().getName(), entry.getValue().getValue(), entry.getValue().getSignature()));
                                }
                            }
                            argument.value = profiles1;
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case OBJECTIVE:
                            range.setEnd(indexOf(args, offset));
                            Scoreboard scoreboard = MinecraftServer.getServer().getWorldServer(0).getScoreboard();
                            String objStr = range.getTrim(args);
                            if(scoreboard==null || scoreboard.getObjective(objStr)==null)
                                throw new com.quartzy.qapi.command.CommandException(finalNode.getName(), true, "commands.scoreboard.objectiveNotFound", objStr);
                            argument.value = objStr;
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case OBJECTIVE_CRITERIA:
                            range.setEnd(indexOf(args, offset));
                            IScoreboardCriteria criteria = IScoreboardCriteria.criteria.get(range.getTrim(args));
                            if (criteria == null)
                                throw new com.quartzy.qapi.command.CommandException(finalNode.getName(), true, "commands.scoreboard.objectives.add.wrongType", range.getTrim(args));
                            argument.value = ScoreCriteria.INSTANCES.get(criteria.getName());
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case COMPONENT:
                            range.setEnd(findClosingIndex(args, offset));
                            argument.value = CraftChatMessage.fromComponent(IChatBaseComponent.ChatSerializer.a(range.getTrim(args)));
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case ENTITY_ANCHOR:
                            range.setEnd(indexOf(args, offset));
                            try{
                                argument.value = EntityAnchor.valueOf(range.getTrim(args));
                            }catch(Exception e){
                                throw new com.quartzy.qapi.command.CommandException(finalNode.getName(), false, "Invalid entity anchor (head or feet)");
                            }
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case ENTITY_SUMMON:
                            range.setEnd(indexOf(args, offset));
                            NBTTagCompound compound = new NBTTagCompound();
                            compound.setString("id", range.getTrim(args));
                            Entity a1 = EntityTypes.a(compound, listener.getWorld());
                            if(a1==null){
                                throw new com.quartzy.qapi.command.CommandException(finalNode.getName(), false, "Invalid entity");
                            }
                            argument.value = a1.getBukkitEntity();
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case MOB_EFFECT:
                            range.setEnd(indexOf(args, offset));
                            String effectStr = range.getTrim(args);
                            MobEffectList effect;
                            try {
                                effect = MobEffectList.fromId(CommandAbstract.a(effectStr, 1));
                            } catch (Exception e) {
                                effect = MobEffectList.getByName(effectStr);
                                if (effect == null) {
                                    throw e;
                                }
                            }
                            argument.value = new CraftPotionEffectType(effect);
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                        case NBT_PATH:
                            range.setEnd(findClosingIndex(args, offset));
                            argument.value = new NBTPath(range.getTrim(args));
                            parse(argumentNode, args, range.getEnd()+1, executorInfo, rethrow);
                            break;
                    }
                    arguments.put(argumentNode.getName(), argument);
                    return;
                } catch(CommandException e){
                    arguments.put(argumentNode.getName(), argument);
                    if(!rethrow) continue;
                    if(j == node.getChildren().size() - 1){
                        throw new com.quartzy.qapi.command.CommandException(finalNode.getName(), true, e.getMessage(), e.getArgs());
                    }
                } catch(com.quartzy.qapi.command.CommandException e){
                    arguments.put(argumentNode.getName(), argument);
                    if(!rethrow) continue;
                    if(j == node.getChildren().size() - 1){
                        throw e;
                    }
                } catch(Exception e){
                    arguments.put(argumentNode.getName(), argument);
                    if(!rethrow) continue;
                    if(j == node.getChildren().size() - 1){
                        if(e instanceof MojangsonParseException){
                            throw new com.quartzy.qapi.command.CommandException(finalNode.getName(), false, e.getMessage());
                        }
                        e.printStackTrace();
                        throw new com.quartzy.qapi.command.CommandException(finalNode.getName(), false, "Internal exception occurred: " + e.getMessage());
                    }
                }
            }else if(finalNode instanceof LiteralNode){
                StringRange range = new StringRange(offset, indexOf(args, offset));
                ParsedArgument argument = new ParsedArgument(null, range, finalNode.getName());
                if(finalNode.getName().equalsIgnoreCase(range.getTrim(args))){
                    argument.value = range.getTrim(args);
                    arguments.put(finalNode.getName(), argument);
                    parse(finalNode, args, range.getEnd()+1, executorInfo, true);
                    return;
                }else{
                    arguments.put(finalNode.getName(), argument);
                    if(rethrow){
                        if(j == node.getChildren().size() - 1){
                            throw new com.quartzy.qapi.command.CommandException(finalNode.getName(), "Unrecognized argument");
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException{
        System.out.println(alias + "  " + Arrays.toString(args));
        StringBuilder argsStr = new StringBuilder();
        for(int i = 0; i < args.length-1; i++){
            if(i==args.length-2) argsStr.append(args[i]);
            else argsStr.append(args[i]).append(" ");
        }
        String fullString = "/" + alias + " " + argsStr;
        CommandExecutorInfo_v1_10_R1 executorInfo = new CommandExecutorInfo_v1_10_R1(new CommandSenderInfo_v1_10_R1(sender, commandNode), null, fullString, argsStr.toString());
        try{
            parse(commandNode, argsStr.toString(), 0, executorInfo, false);
        } catch(Throwable ignored){
        
        }
        List<String> suggestions = new ArrayList<>();
        int argumentCount = argsStr.substring(Math.max(lastOffset-1, 0)).split(" ").length;
        for(int i = 0; i < lastNode.getChildren().size(); i++){
            if(lastNode.getChildren().get(i) instanceof ArgumentNode){
                suggestions.addAll(generateSuggestions(((ArgumentNode) lastNode.getChildren().get(i)).getType(), argumentCount, sender));
            }else{
                suggestions.add(lastNode.getChildren().get(i).getName());
            }
        }
        this.suggestions = suggestions;
        return sort(args[args.length-1]);
    }
    
    public List<String> generateSuggestions(ArgumentType type, int argumentCount, CommandSender sender){
        switch(type){
            default:
                return new ArrayList<>();
            case PLAYER:
            case PLAYERS:
            case ENTITY:
            case ENTITIES:
            case SCORE_HOLDER:
                List<String> names = new ArrayList<>();
                for(Player onlinePlayer : Bukkit.getOnlinePlayers()){
                    names.add(onlinePlayer.getName());
                }
                names.add("@p");
                names.add("@r");
                names.add("@e");
                names.add("@a");
                return names;
            case ENTITY_ANCHOR:
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add("head");
                arrayList.add("feet");
                return arrayList;
            case COLOR:
                List<String> colors = new ArrayList<>();
                for(ChatColor value : ChatColor.values()){
                    colors.add(value.getChar() + "");
                }
                return colors;
            case BOOLEAN:
                ArrayList<String> bool = new ArrayList<>();
                bool.add("true");
                bool.add("false");
                return bool;
            case GAME_PROFILE:
                List<String> profiles = new ArrayList<>();
                for(Player onlinePlayer : Bukkit.getOnlinePlayers()){
                    profiles.add(onlinePlayer.getName());
                    profiles.add(onlinePlayer.getUniqueId().toString());
                }
                profiles.add("@p");
                profiles.add("@r");
                profiles.add("@e");
                profiles.add("@a");
                return profiles;
            case OBJECTIVE_CRITERIA:
                return new ArrayList<>(ScoreCriteria.INSTANCES.keySet());
            case TEAM:
                return new ArrayList<>(MinecraftServer.getServer().getWorldServer(0).getScoreboard().getTeamNames());
            case BLOCK_STATE:
                return toStringList(Block.REGISTRY.keySet());
            case ITEM_STACK:
                return toStringList(Item.REGISTRY.keySet());
            case VEC3:
                if(!(sender instanceof org.bukkit.entity.Entity))return new ArrayList<>();
                Location locationVec3 = ((org.bukkit.entity.Entity) sender).getLocation();
                switch(argumentCount){
                    case 1:
                        return Collections.singletonList(Double.toString(locationVec3.getX()));
                    case 2:
                        return Collections.singletonList(Double.toString(locationVec3.getY()));
                    case 3:
                        return Collections.singletonList(Double.toString(locationVec3.getZ()));
                }
                return new ArrayList<>();
            case BLOCK_POS:
                if(!(sender instanceof org.bukkit.entity.Entity))return new ArrayList<>();
                Location locationBlock = ((org.bukkit.entity.Entity) sender).getLocation();
                switch(argumentCount){
                    case 1:
                        return Collections.singletonList(Integer.toString(locationBlock.getBlockX()));
                    case 2:
                        return Collections.singletonList(Integer.toString(locationBlock.getBlockY()));
                    case 3:
                        return Collections.singletonList(Integer.toString(locationBlock.getBlockZ()));
                }
                return new ArrayList<>();
            case VEC2:
                if(!(sender instanceof org.bukkit.entity.Entity))return new ArrayList<>();
                Location locationVec2 = ((org.bukkit.entity.Entity) sender).getLocation();
                switch(argumentCount){
                    case 1:
                        return Collections.singletonList(Double.toString(locationVec2.getX()));
                    case 2:
                        return Collections.singletonList(Double.toString(locationVec2.getY()));
                }
                return new ArrayList<>();
            case COLUMN_POS:
                if(!(sender instanceof org.bukkit.entity.Entity))return new ArrayList<>();
                Location locationColumn = ((org.bukkit.entity.Entity) sender).getLocation();
                switch(argumentCount){
                    case 1:
                        return Collections.singletonList(Integer.toString(locationColumn.getBlockX()));
                    case 2:
                        return Collections.singletonList(Integer.toString(locationColumn.getBlockY()));
                }
                return new ArrayList<>();
            case ENTITY_SUMMON:
                return toStringList(EntityTypes.b());
            case ITEM_ENCHANTMENT:
                return toStringList(Enchantment.enchantments.keySet());
            case ITEM_SLOT:
                return toStringList(slotMap.keySet());
            case MOB_EFFECT:
                return toStringList(MobEffectList.REGISTRY.keySet());
            case OBJECTIVE:
                return toStringList(MinecraftServer.getServer().getWorldServer(0).getScoreboard().getObjectives());
            case PARTICLE:
                return toStringList(EnumParticle.a());
            case ROTATION:
                if(!(sender instanceof org.bukkit.entity.Entity))return new ArrayList<>();
                Location locationRotation = ((org.bukkit.entity.Entity) sender).getLocation();
                switch(argumentCount){
                    case 1:
                        return Collections.singletonList(Float.toString(locationRotation.getYaw()));
                    case 2:
                        return Collections.singletonList(Float.toString(locationRotation.getPitch()));
                }
                return new ArrayList<>();
            case SCOREBOARD_SLOT:
                return Arrays.asList(Scoreboard.h());
        }
    }
    
    public List<String> sort(String inStr){
        List<String> suggestion = new ArrayList<>();
        StringUtil.copyPartialMatches(inStr, this.suggestions, suggestion);
        return suggestion;
    }
    
    private static List<String> toStringList(Collection<?> var1) {
        ArrayList<String> var3 = new ArrayList<>();
        if (!var1.isEmpty()) {
            for(Object var6 : var1){
                if(var6 instanceof ScoreboardObjective){
                    var3.add(((ScoreboardObjective) var6).getName());
                }else{
                    var3.add(String.valueOf(var6));
                }
            }
        }
        
        return var3;
    }
    
    private static int indexOf(String s, int offset){
        return indexOf(s, offset, 1);
    }
    
    private static int indexOf(String s, int offset, int count){
        int j = 0;
        for(int i = offset; i < s.length(); i++){
            if(s.charAt(i) == ' '){
                j++;
            }
            if(j==count){
                return i;
            }
        }
        return j==count-1 ? s.length() : -1;
    }
    
    private static String[] split(String s, int count){
        return s.split(" ", count);
    }
    
    private static final Map<String, Integer> slotMap = new HashMap<>();
    
    static {
        int var1;
        for(var1 = 0; var1 < 54; ++var1) {
            slotMap.put("container." + var1, var1);
        }
    
        for(var1 = 0; var1 < 9; ++var1) {
            slotMap.put("hotbar." + var1, var1);
        }
    
        for(var1 = 0; var1 < 27; ++var1) {
            slotMap.put("inventory." + var1, 9 + var1);
        }
    
        for(var1 = 0; var1 < 27; ++var1) {
            slotMap.put("enderchest." + var1, 200 + var1);
        }
    
        for(var1 = 0; var1 < 8; ++var1) {
            slotMap.put("villager." + var1, 300 + var1);
        }
    
        for(var1 = 0; var1 < 15; ++var1) {
            slotMap.put("horse." + var1, 500 + var1);
        }
    
        slotMap.put("weapon", 98);
        slotMap.put("weapon.mainhand", 98);
        slotMap.put("weapon.offhand", 99);
        slotMap.put("armor.head", 100 + 3);
        slotMap.put("armor.chest", 100 + 2);
        slotMap.put("armor.legs", 100 + 1);
        slotMap.put("armor.feet", 100);
        slotMap.put("horse.saddle", 400);
        slotMap.put("horse.armor", 401);
        slotMap.put("horse.chest", 499);
    }
    
    public static int parseItemSlot(String s) throws CommandException{
        Integer integer = slotMap.get(s);
        if(integer==null){
            throw new CommandException("Invalid slot: " + s);
        }
        return integer;
    }
    
    public static List<EntityPlayer> getPlayers(ICommandListener var0, String var1) throws ExceptionEntityNotFound{
        return (List<EntityPlayer>)(PlayerSelector.isPattern(var1) ? PlayerSelector.getPlayers(var0, var1, EntityPlayer.class) : Lists.newArrayList(new Entity[]{CommandAbstract.a(((CraftServer) Bukkit.getServer()).getServer(), var0, var1, EntityPlayer.class)}));
    }
    
    static class ParsedArgument{
        public Object value;
        public final StringRange range;
        public final String name;
    
        public ParsedArgument(Object value, StringRange range, String name){
            this.value = value;
            this.range = range;
            this.name = name;
        }
    }
    
    public static String getPlayerNames(ICommandListener icommandlistener, String s) {
        List<?> list = PlayerSelector.getPlayers(icommandlistener, s, EntityPlayer.class);
        if (list.isEmpty()) {
            return null;
        } else {
            ArrayList<IChatBaseComponent> arraylist = new ArrayList<>();
    
            for(Object entityPlayer : list){
                arraylist.add(((Entity) entityPlayer).getScoreboardDisplayName());
            }
            
            return CommandAbstract.a(arraylist).getText();
        }
    }
    
    public static int findClosingIndex(String in, int offset){
        int[] occurrances = new int[nbtNestingChars.length/2 + nbtStringChars.length];
        boolean occured = false;
        strLoop:
        for(int i = offset; i < in.length(); i++){
            char c = in.charAt(i);
            chLoop:
            {
                for(int j = 0; j < nbtNestingChars.length; j++){
                    if(c == nbtNestingChars[j]){
                        occurrances[j/2]+= (1 + ((j % 2) * -2));
                        break chLoop;
                    }
                }
                for(int j = 0; j < nbtStringChars.length; j++){
                    if(c == nbtStringChars[j]){
                        occurrances[nbtNestingChars.length/2-1+j] = occurrances[nbtNestingChars.length/2-1+j]==0 ? 1 : 0;
                        break chLoop;
                    }
                }
            }
            for(int occurrance : occurrances){
                if(occurrance != 0){
                    occured = true;
                    continue strLoop;
                }
            }
            return !occured ? indexOf(in, offset, 1) : ++i;
        }
        return -1;
    }
    
    public LiteralNode getCommandNode(){
        return commandNode;
    }
    
    @Override
    public String toString(){
        return "CommandController{name=" + this.commandNode.getName() + "}";
    }
}
