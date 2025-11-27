package com.stardevllc.starspawners;

import com.stardevllc.config.file.FileConfig;
import com.stardevllc.starmclib.plugin.ExtendedJavaPlugin;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Deprecated(forRemoval = true, since = "0.6.0")
public class SpawnerManager {
    private ExtendedJavaPlugin plugin;
    
    public SpawnerManager(ExtendedJavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public FileConfig getPluginConfig() {
        return plugin.getMainConfig();
    }
    
    public void breakSpawner(Player player, CreatureSpawner spawner) {
        StarSpawners.breakSpawner(player, spawner);
    }
    
    public void dropSpawnerItem(CreatureSpawner spawner) {
        StarSpawners.dropSpawnerItem(spawner);
    }
    
    public void giveSpawnerItem(Player player, CreatureSpawner spawner) {
        StarSpawners.giveSpawnerItem(player, spawner);
    }
    
    public ItemStack createSpawnerItemStack(EntityType entityType, long id) {
        return StarSpawners.createSpawnerItemStack(entityType, id);
    }
    
    public void placeSpawner(Player player, ItemStack handItem, CreatureSpawner spawner) {
        StarSpawners.placeSpawner(player, handItem, spawner);
    }
    
    public void setSpawnerType(CreatureSpawner spawner, EntityType entityType, long spawnerId) {
        StarSpawners.setSpawnerType(spawner, entityType, spawnerId);
    }
}