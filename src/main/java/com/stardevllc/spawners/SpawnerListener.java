package com.stardevllc.spawners;

import com.stardevllc.smaterial.ToolSet;
import com.stardevllc.smcversion.MCWrappers;
import com.stardevllc.starlib.injector.Inject;
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
    @Inject
    private StarSpawners plugin;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.SURVIVAL) {
            return;
        }
        
        Player player = e.getPlayer();
        if (e.getBlock().getType() != Material.SPAWNER) {
            return;
        }

        ItemStack item = MCWrappers.getPlayerHandWrapper().getItemInMainHand(e.getPlayer());
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
        
        if (e.getBlockPlaced().getType() != Material.SPAWNER) {
            return;
        }

        try {
            EntityType entityType;
            try {
                entityType = EntityType.valueOf(NBT.get(item, nbt -> {
                    return nbt.getString("spawnerType");
                }));
            } catch (Throwable ex) {
                entityType = EntityType.PIG;
            }
            
            CreatureSpawner creatureSpawner = (CreatureSpawner) e.getBlockPlaced().getState();
            plugin.getSpawnerManager().setSpawnerType(creatureSpawner, entityType, 0);
        } catch (Exception exception) {
            plugin.getLogger().log(Level.SEVERE, "Could not set the spawner type", exception);
        }
    }
}
