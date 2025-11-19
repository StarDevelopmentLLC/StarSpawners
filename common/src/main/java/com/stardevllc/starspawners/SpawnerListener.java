package com.stardevllc.starspawners;

import com.stardevllc.smaterial.ToolSet;
import com.stardevllc.smcversion.MCWrappers;
import com.stardevllc.starlib.injector.Inject;
import com.stardevllc.starmclib.plugin.ExtendedJavaPlugin;
import de.tr7zw.nbtapi.NBT;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.logging.Level;

public class SpawnerListener implements Listener {
    
    private ExtendedJavaPlugin plugin;
    
    @Inject
    private SpawnerManager spawnerManager;
    
    public SpawnerListener(ExtendedJavaPlugin plugin) {
        this.plugin = plugin;
    }
    
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

        if (plugin.getMainConfig().getBoolean("spawners.requiresilktouch")) {
            if (!item.containsEnchantment(Enchantment.SILK_TOUCH)) {
                return;
            }
        }
        
        ToolSet minToolSet;
        try {
            minToolSet = ToolSet.valueOf(spawnerManager.getPluginConfig().getString("spawners.mintoolmaterial"));
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
        if (plugin.getMainConfig().getBoolean("spawners.entitypermissions.break")) {
            if (!player.hasPermission("starspawners.break.type." + creatureSpawner.getSpawnedType().name().toLowerCase())) {
                e.setCancelled(true);
                plugin.getColors().coloredLegacy(player, "&cYou do not have permission to break " + creatureSpawner.getSpawnedType().name() + " spawners");
                return;
            }
        }
        
        spawnerManager.breakSpawner(player, creatureSpawner);
        e.setExpToDrop(0);
    }

    @EventHandler
    public void onPlaceEvent(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        
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
            
            if (plugin.getMainConfig().getBoolean("spawners.entitypermissions.place")) {
                if (!player.hasPermission("starspawners.place.type." + entityType.name().toLowerCase())) {
                    plugin.getColors().coloredLegacy(player, "&cYou do not have permission to place " + entityType.name() + " spawners");
                    e.setCancelled(true);
                    return;
                }
            }
            
            CreatureSpawner creatureSpawner = (CreatureSpawner) e.getBlockPlaced().getState();
            spawnerManager.setSpawnerType(creatureSpawner, entityType, 0);
        } catch (Exception exception) {
            plugin.getLogger().log(Level.SEVERE, "Could not set the spawner type", exception);
        }
    }
    
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!plugin.getMainConfig().getBoolean("spawners.entitypermissions.change")) {
            return;
        }
        
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }
        
        ItemStack item = e.getItem();
        if (item == null) {
            return;
        }
        
        ItemMeta itemMeta = item.getItemMeta();
        if (!(itemMeta instanceof SpawnEggMeta spawnEggMeta)) {
            return;
        }
        
        if (block.getType() != Material.SPAWNER) {
            return;
        }
        Player player = e.getPlayer();
        
        EntityType entityType;
        try {
            entityType = spawnEggMeta.getSpawnedType();
        } catch (Throwable t) {
            try {
                entityType = spawnEggMeta.getSpawnedEntity().getEntityType();
            } catch (Throwable t1) {
                try {
                    entityType = EntityType.valueOf(item.getType().name().replace("_SPAWN_EGG", "").toUpperCase());
                } catch (Throwable t2) {
                    plugin.getColors().coloredLegacy(player, "&cError while determining Entity Type. Please report as a bug to plugin author");
                    return;
                }
            }
        }
        
        if (!player.hasPermission("starspawners.change.type." + entityType.name().toLowerCase())) {
            plugin.getColors().coloredLegacy(player, "&cYou do not have permission change spawners to " + entityType.name());
            e.setCancelled(true);
        }
    }
}
