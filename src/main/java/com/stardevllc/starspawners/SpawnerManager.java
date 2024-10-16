package com.stardevllc.starspawners;

import com.stardevllc.starcore.color.ColorHandler;
import de.tr7zw.nbtapi.NBT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerManager {
    private StarSpawners plugin;

    public SpawnerManager(StarSpawners plugin) {
        this.plugin = plugin;
    }

    public void giveSpawner(Player player, int amount, String spawnerName){
        ItemStack spawner = new ItemStack(Material.SPAWNER,amount);
        NBT.modify(spawner, nbt -> {
            nbt.setString("spawnerType", spawnerName);
        });
        ItemMeta spawnerMeta = spawner.getItemMeta();
        spawnerMeta.setDisplayName(ColorHandler.getInstance().color("&d" + spawnerName + " Spawner"));
        spawner.setItemMeta(spawnerMeta);
        player.getInventory().addItem(spawner);
    }

    public void setSpawnerType(CreatureSpawner creatureSpawner, EntityType entityType) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            creatureSpawner.setSpawnedType(entityType);
            creatureSpawner.update();
        }, 1L);
    }
}
