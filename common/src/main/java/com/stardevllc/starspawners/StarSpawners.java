package com.stardevllc.starspawners;

import com.stardevllc.config.file.FileConfig;
import com.stardevllc.smaterial.ToolSet;
import com.stardevllc.starmclib.StarMCLib;
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

@SuppressWarnings("removal")
public final class StarSpawners {
    private StarSpawners() {}
    
    private static ExtendedJavaPlugin plugin;
    @Deprecated(forRemoval = true, since = "0.6.0")
    private static SpawnerManager spawnerManager;

    public static void init(ExtendedJavaPlugin plugin) {
        if (StarSpawners.plugin != null) {
            plugin.getLogger().warning("StarSpawners has already been initialized by " + StarSpawners.plugin.getName());
            return;
        }
        
        FileConfig mainConfig = plugin.getMainConfig();
        mainConfig.load();
        mainConfig.addDefault("spawners.name", "&d{ENTITYNAME} Spawner", "The name for the spawner item", "This only applies to spawners that are picked up via silk touch", "You don't need the entity name in the display name. The plugin tracks the items using NBT Tags");
//        this.mainConfig.addDefault("spawner.unique", false, "This setting makes it so that all spawners that are created are unique", "This makes them unstackable and makes sure that they cannot be duped");
        
        mainConfig.addDefault("spawners.pickupmode", "DROP", "Controls the mode in how spawner items are handled when picked up using Silk Touch", " DROP is where the item is dropped on the ground where the spawner was located", " INVENTORY is where the item is given to the player that broke the block directly", "It is recommended on larger servers to change this to INVENTORY to help with item lag");
        mainConfig.addDefault("spawners.mintoolmaterial", "IRON", "Controls the minimum tool material needed to be able to silk touch a spawner");
        mainConfig.addDefault("spawners.requiresilktouch", true, "Setting to control if silk touch is needed on the pickaxe to get the spawner item");
        mainConfig.addDefault("spawners.entitypermissions.break", false, "Setting to control if players need a permission to break certain types of spawners. The permission is starspawners.break.type.<entityname> in all lower case");
        mainConfig.addDefault("spawners.entitypermissions.place", false, "Setting to control if players need a permission to place certain types of spawners. The permission is starspawners.place.type.<entityname> in all lower case");
        mainConfig.addDefault("spawners.entitypermissions.change", false, "Setting to control if players need a permission to change a spawner with a spawn egg. The permission is starspawners.change.type.<entityname> in all lower case");
        mainConfig.reload(true);
        
        spawnerManager = new SpawnerManager(plugin);
        StarMCLib.GLOBAL_INJECTOR.set(spawnerManager);
        
        plugin.getServer().getPluginManager().registerEvents(plugin.getInjector().inject(new SpawnerListener(plugin)), plugin);
    }
    
    public static ToolSet getMinToolSet() {
        try {
            return ToolSet.valueOf(plugin.getMainConfig().getString("spawners.mintoolmaterial"));
        } catch (Throwable t) {}
        return null;
    }
    
    public static void breakSpawner(Player player, CreatureSpawner spawner) {
        String pickupMode = plugin.getMainConfig().getString("spawners.pickupmode");
        EntityType entityType = spawner.getSpawnedType();
        
        if (pickupMode.equalsIgnoreCase("drop")) {
            dropSpawnerItem(spawner);
        } else if (pickupMode.equalsIgnoreCase("inventory")) {
            giveSpawnerItem(player, spawner);
        }
    }
    
    public static void dropSpawnerItem(CreatureSpawner spawner) {
        EntityType entityType = spawner.getSpawnedType();
        long id = NBT.get(spawner, (Function<ReadableNBT, Long>) nbt -> nbt.getLong("spawnerId"));
        spawner.getWorld().dropItem(spawner.getLocation(), createSpawnerItemStack(entityType, id));
    }
    
    public static void giveSpawnerItem(Player player, CreatureSpawner spawner) {
        EntityType entityType = spawner.getSpawnedType();
        long id = NBT.get(player, (Function<ReadableNBT, Long>) nbt -> nbt.getLong("spawnerId"));
        player.getInventory().addItem(createSpawnerItemStack(entityType, id));
    }
    
    public static ItemStack createSpawnerItemStack(EntityType entityType, long id) {
        if (entityType == null) {
            return null;
        }
        
        ItemStack spawnerItem = new ItemStack(Material.SPAWNER);
        ItemMeta spawnerMeta = spawnerItem.getItemMeta();
        String configSpawnerName = plugin.getMainConfig().getString("spawners.name");
        if (configSpawnerName != null) {
            String spawnerName = configSpawnerName.replace("{ENTITYNAME}", EntityNames.getDefaultName(entityType));
            spawnerMeta.setDisplayName(plugin.getColors().getSectionLegacy().serialize(plugin.getColors().getAmpersandLegacy().deserialize(spawnerName)));
        }
        spawnerItem.setItemMeta(spawnerMeta);
        if (id == 0 && plugin.getMainConfig().getBoolean("spawners.unique")) {
            id = new Random().nextLong();
        }
        
        long finalId = id;
        NBT.modify(spawnerItem, nbt -> {
            nbt.setLong("spawnerId", finalId);
            nbt.setString("spawnerType", entityType.name());
        });
        return spawnerItem;
    }
    
    public static void placeSpawner(Player player, ItemStack handItem, CreatureSpawner spawner) {
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
    
    public static void setSpawnerType(CreatureSpawner spawner, EntityType entityType, long spawnerId) {
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
    
    @Deprecated(forRemoval = true, since = "0.6.0")
    public static SpawnerManager getSpawnerManager() {
        return spawnerManager;
    }
}