package com.stardevllc.spawners;

import com.stardevllc.starcore.utils.ToolSet;
import de.tr7zw.nbtapi.NBT;
import org.bukkit.GameMode;
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
        if (e.getPlayer().getGameMode() != GameMode.SURVIVAL) {
            return;
        }
        
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
        
        ToolSet minToolSet;
        try {
            minToolSet = ToolSet.valueOf(plugin.getMainConfig().getString("spawner.mintoolmaterial"));
        } catch (Exception ex) {
            plugin.getLogger().severe("Invalid material name for the spawner.mintoolmaterial config option");
            return;
        }
        
        boolean toolMatches = false;
        
        for (int i = minToolSet.ordinal(); i < ToolSet.values().length; i++) {
            if (item.getType() == ToolSet.values()[i].getPickaxe().parseMaterial()) {
                toolMatches = true;
                break;
            }
        }
        
        if (!toolMatches) {
            return;
        }

        CreatureSpawner creatureSpawner = (CreatureSpawner) e.getBlock().getState();
        plugin.getSpawnerManager().breakSpawner(player, creatureSpawner);
        e.setExpToDrop(0);
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
