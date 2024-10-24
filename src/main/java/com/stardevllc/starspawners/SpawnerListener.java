package com.stardevllc.starspawners;

import de.tr7zw.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public class SpawnerListener implements Listener {
    private StarSpawners plugin;

    public SpawnerListener(StarSpawners plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (!e.getBlock().getType().equals(Material.SPAWNER)) {
            return;
        }

        ItemStack item = plugin.getPlayerHandWrapper().getItemInMainHand(e.getPlayer());
        if (item == null) {
            return;
        }

        if (!item.containsEnchantment(Enchantment.SILK_TOUCH)) {
            return;
        }

        CreatureSpawner creatureSpawner = (CreatureSpawner) e.getBlock().getState();
        plugin.getSpawnerManager().breakSpawner(player, creatureSpawner);
    }

    @EventHandler
    public void onPlaceEvent(BlockPlaceEvent e) {
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        if (item == null) {
            return;
        }

        if (!(e.getBlockPlaced().getType().equals(Material.SPAWNER))) {
            return;
        }

        try {
            EntityType entityType = EntityType.valueOf(NBT.get(item, nbt -> {
                return nbt.getString("spawnerType");
            }));

            CreatureSpawner creatureSpawner = (CreatureSpawner) e.getBlockPlaced().getState();
            plugin.getSpawnerManager().setSpawnerType(creatureSpawner, entityType, 0);
        } catch (Exception exception) {
            plugin.getLogger().log(Level.SEVERE, "Could not set the spawner type", exception);
        }
    }
}
