package com.stardevllc.starspawners;

import com.stardevllc.config.file.FileConfig;
import com.stardevllc.starmclib.names.EntityNames;
import com.stardevllc.starmclib.plugin.ExtendedJavaPlugin;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;
import java.util.function.Function;

public class SpawnerManager {
    private ExtendedJavaPlugin plugin;
    
    public SpawnerManager(ExtendedJavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public FileConfig getPluginConfig() {
        return plugin.getMainConfig();
    }
    
    public void breakSpawner(Player player, CreatureSpawner spawner) {
        String pickupMode = getPluginConfig().getString("spawners.pickupmode");
        EntityType entityType = spawner.getSpawnedType();
        
        if (pickupMode.equalsIgnoreCase("drop")) {
            dropSpawnerItem(spawner);
        } else if (pickupMode.equalsIgnoreCase("inventory")) {
            giveSpawnerItem(player, spawner);
        }
    }
    
    public void dropSpawnerItem(CreatureSpawner spawner) {
        EntityType entityType = spawner.getSpawnedType();
        long id = NBT.get(spawner, (Function<ReadableNBT, Long>) nbt -> nbt.getLong("spawnerId"));
        spawner.getWorld().dropItem(spawner.getLocation(), createSpawnerItemStack(entityType, id));
    }
    
    public void giveSpawnerItem(Player player, CreatureSpawner spawner) {
        EntityType entityType = spawner.getSpawnedType();
        long id = NBT.get(player, (Function<ReadableNBT, Long>) nbt -> nbt.getLong("spawnerId"));
        player.getInventory().addItem(createSpawnerItemStack(entityType, id));
    }
    
    public ItemStack createSpawnerItemStack(EntityType entityType, long id) {
        if (entityType == null) {
            return null;
        }
        
        ItemStack spawnerItem = new ItemStack(Material.SPAWNER);
        ItemMeta spawnerMeta = spawnerItem.getItemMeta();
        String configSpawnerName = getPluginConfig().getString("spawners.name");
        if (configSpawnerName != null) {
            String spawnerName = configSpawnerName.replace("{ENTITYNAME}", EntityNames.getDefaultName(entityType));
            spawnerMeta.setDisplayName(plugin.getColors().getSectionLegacy().serialize(plugin.getColors().getAmpersandLegacy().deserialize(spawnerName)));
        }
        spawnerItem.setItemMeta(spawnerMeta);
        if (id == 0 && getPluginConfig().getBoolean("spawners.unique")) {
            id = new Random().nextLong();
        }

        long finalId = id;
        NBT.modify(spawnerItem, nbt -> {
            nbt.setLong("spawnerId", finalId);
            nbt.setString("spawnerType", entityType.name());
        });
        return spawnerItem;
    }
    
    public void placeSpawner(Player player, ItemStack handItem, CreatureSpawner spawner) {
        String rawType = NBT.get(handItem, nbt -> {
            return nbt.getString("spawnerType");
        });
        
        EntityType parsedEntityType;
        
        if (rawType == null || rawType.isEmpty()) {
            parsedEntityType = EntityType.PIG;
        } else {
            try {
                parsedEntityType = EntityType.valueOf(rawType.toUpperCase());
            } catch (Throwable e) {
                parsedEntityType = EntityType.PIG;
            }
        }
        
        final EntityType entityType = parsedEntityType;
        
        long spawnerId = NBT.get(handItem, nbt -> {
            return nbt.getLong("spawnerId");
        });
        
        Bukkit.getScheduler().runTaskLater(plugin, () -> NBT.modify(spawner, nbt -> {
            nbt.setLong("spawnerId", spawnerId);
            spawner.setSpawnedType(entityType);
            spawner.update();
        }), 1L);
    }
    
    public void setSpawnerType(CreatureSpawner spawner, EntityType entityType, long spawnerId) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            spawner.setSpawnedType(entityType);
            spawner.update();
            if (spawnerId > 0) {
                NBT.modify(spawner, nbt -> {
                    nbt.setLong("spawnerId", spawnerId);
                });
            }
        }, 1L);
    }
}
