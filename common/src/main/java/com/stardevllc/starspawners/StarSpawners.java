package com.stardevllc.starspawners;

import com.stardevllc.config.file.FileConfig;
import com.stardevllc.config.file.yaml.YamlConfig;
import com.stardevllc.starmclib.StarMCLib;
import com.stardevllc.starmclib.plugin.ExtendedJavaPlugin;

import java.io.File;

public final class StarSpawners {
    private StarSpawners() {}
    
    private static ExtendedJavaPlugin plugin;
    private static SpawnerManager spawnerManager;

    public static void init(ExtendedJavaPlugin plugin) {
        if (StarSpawners.plugin != null) {
            plugin.getLogger().warning("StarSpawners has already been initialized by " + StarSpawners.plugin.getName());
            return;
        }
        
        FileConfig mainConfig = new YamlConfig(new File(plugin.getDataFolder(), "config.yml"));
        mainConfig.addDefault("spawners.name", "&d{ENTITYNAME} Spawner", " The name for the spawner item", " The only applies to spawners that are picked up via silk touch", "You don't need the entity name in the display name. The plugin tracks the items using NBT Tags");
//        this.mainConfig.addDefault("spawner.unique", false, "This setting makes it so that all spawners that are created are unique", "This makes them unstackable and makes sure that they cannot be duped");
        
        mainConfig.addDefault("spawners.pickupmode", "DROP", " Controls the mode in how spawner items are handled when picked up using Silk Touch", " DROP is where the item is dropped on the ground where the spawner was located", " INVENTORY is where the item is given to the player that broke the block directly", " It is recommended on larger servers to change this to INVENTORY to help with item lag");
        mainConfig.addDefault("spawners.mintoolmaterial", "IRON", " Controls the minimum tool material needed to be able to silk touch a spawner");
        mainConfig.save();
        
        spawnerManager = new SpawnerManager(plugin);
        StarMCLib.GLOBAL_INJECTOR.setInstance(spawnerManager);
        
        plugin.getServer().getPluginManager().registerEvents(plugin.getInjector().inject(new SpawnerListener(plugin)), plugin);
    }
    
    public static SpawnerManager getSpawnerManager() {
        return spawnerManager;
    }
}