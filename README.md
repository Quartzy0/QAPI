# **QAPI - A powerful multi-version API**

## Features
- [NBT API](#nbt-api)
- Powerful command API with 1.13+ command completion

## NBT API

### Editing item NBT
To edit an item's nbt the method `NBTProvider.getNBTFromItem` can be used. This method takes in an `ItemStack` and returns a `NBTCompound` instance.
```
NBTProvider nbtProvider = QAPI.nbtProvider();
NBTCompound itemNBT = nbtProvider.getNBTFromItem(itemStack);
itemNBT.setString("value", "key");
nbtProvider.applyNBTToItem(itemStack, itemNBT);
```
Changing the `NBTCompound` instance will not change the item's actual NBT value.
To actually update the item's NBT value you must call the `NBTProvider.applyNBTToItem` method.

### Editing entity NBT
Getting an entity's NBT is very similar to getting an item's NBT. All you have to do is call the `NBTProvider.getNBTFromEntity` method.
This method takes in an `Entity` and returns a `NBTCompound` instance.
```
NBTProvider nbtProvider = QAPI.nbtProvider();
NBTCompound entityNBT = nbtProvider.getNBTFromEntity(entity);
entityNBT.setBoolean("Invulnerable", true);
nbtProvider.applyNBTToEntity(entity, entityNBT);
```
Because this NBT API is just a wrapper around the NBT API already included in bukkit, just multi-version, setting a custom tag on an entity will not work.
This is because minecraft does not actually save any entity NBT. For entities, NBT is simply a way of setting variables, like Invulnerability, Speed and so on.
If you wanted to store custom NBT data for entities, you would need to do that manually.