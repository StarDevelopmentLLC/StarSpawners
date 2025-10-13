package com.stardevllc.spawners;

import com.stardevllc.config.file.FileConfig;
import com.stardevllc.config.file.yaml.YamlConfig;
import com.stardevllc.smcversion.MCWrappers;
import com.stardevllc.smcversion.wrappers.PlayerHandWrapper;
import com.stardevllc.starmclib.StarMCLib;
import com.stardevllc.starmclib.plugin.ExtendedJavaPlugin;

import java.io.File;

public class StarSpawners extends ExtendedJavaPlugin {
    
    private FileConfig mainConfig;
    private SpawnerManager spawnerManager;

    @Override
    public void onEnable() {
        super.onEnable();
        StarMCLib.registerPluginInjector(this, getInjector());
        StarMCLib.registerPluginEventBus(getEventBus());
        this.mainConfig = new YamlConfig(new File(getDataFolder(), "config.yml"));
        this.mainConfig.addDefault("spawner.name", "&d{ENTITYNAME} Spawner", " The name for the spawner item", " The only applies to spawners that are picked up via silk touch", "You don't need the entity name in the display name. The plugin tracks the items using NBT Tags");
//        this.mainConfig.addDefault("spawner.unique", false, "This setting makes it so that all spawners that are created are unique", "This makes them unstackable and makes sure that they cannot be duped");
        
        this.mainConfig.addDefault("spawner.pickupmode", "DROP", " Controls the mode in how spawner items are handled when picked up using Silk Touch", " DROP is where the item is dropped on the ground where the spawner was located", " INVENTORY is where the item is given to the player that broke the block directly", " It is recommended on larger servers to change this to INVENTORY to help with item lag");
        this.mainConfig.addDefault("spawner.mintoolmaterial", "IRON", " Controls the minimum tool material needed to be able to silk touch a spawner");
        this.mainConfig.save();
        
        registerListeners(new SpawnerListener());
        
        spawnerManager = getInjector().inject(new SpawnerManager());
        StarMCLib.GLOBAL_INJECTOR.setInstance(spawnerManager);
        
        registerCommand("spawner", new SpawnerCommand());
    }

    public SpawnerManager getSpawnerManager() {
        return spawnerManager;
    }

    public PlayerHandWrapper getPlayerHandWrapper() {
        return getServer().getServicesManager().getRegistration(MCWrappers.class).getProvider().getPlayerHandWrapper();
    }

    public FileConfig getMainConfig() {
        return mainConfig;
    }
}
