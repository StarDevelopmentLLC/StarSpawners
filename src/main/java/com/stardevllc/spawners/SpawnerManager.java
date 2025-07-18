package com.stardevllc.spawners;

import com.stardevllc.starcore.api.StarColors;
import com.stardevllc.starmclib.EntityNames;
import de.tr7zw.nbtapi.NBT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class SpawnerManager implements Listener {
    private StarSpawners plugin;

    public SpawnerManager(StarSpawners plugin) {
        this.plugin = plugin;
    }
    
    public void breakSpawner(Player player, CreatureSpawner spawner) {
        String pickupMode = plugin.getMainConfig().getString("spawner.pickupmode");
        if (pickupMode.equalsIgnoreCase("drop")) {
            dropSpawnerItem(spawner);
        } else if (pickupMode.equalsIgnoreCase("inventory")) {
            giveSpawnerItem(player, spawner);
        }
    }
    
    public void dropSpawnerItem(CreatureSpawner spawner) {
        EntityType entityType = spawner.getSpawnedType();
        long id = NBT.getPersistentData(spawner, nbt -> nbt.getLong("spawnerId"));
        spawner.getWorld().dropItem(spawner.getLocation(), createSpawnerItemStack(entityType, id));
    }
    
    public void giveSpawnerItem(Player player, CreatureSpawner spawner) {
        EntityType entityType = spawner.getSpawnedType();
        long id = NBT.getPersistentData(player, nbt -> nbt.getLong("spawnerId"));
        player.getInventory().addItem(createSpawnerItemStack(entityType, id));
    }
    
    public ItemStack createSpawnerItemStack(EntityType entityType, long id) {
        if (entityType == null) {
            return null;
        }
        
        ItemStack spawnerItem = new ItemStack(Material.SPAWNER);
        ItemMeta spawnerMeta = spawnerItem.getItemMeta();
        spawnerMeta.setDisplayName(StarColors.color(plugin.getMainConfig().getString("spawner.name").replace("{ENTITYNAME}", EntityNames.getDefaultName(entityType))));
        spawnerItem.setItemMeta(spawnerMeta);
        if (id == 0 && plugin.getMainConfig().getBoolean("spawner.unique")) {
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
        
        EntityType entityType;
        
        if (rawType == null || rawType.isEmpty()) {
            entityType = null;
        } else {
            entityType = EntityType.valueOf(rawType.toUpperCase());
        }
        
        long spawnerId = NBT.get(handItem, nbt -> {
            return nbt.getLong("spawnerId");
        });
        
        Bukkit.getScheduler().runTaskLater(plugin, () -> NBT.modifyPersistentData(spawner, nbt -> {
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
                NBT.modifyPersistentData(spawner, nbt -> {
                    nbt.setLong("spawnerId", spawnerId);
                });
            }
        }, 1L);
        
    }
}
