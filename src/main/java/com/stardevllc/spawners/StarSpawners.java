package com.stardevllc.spawners;

import com.stardevllc.starcore.StarCore;
import com.stardevllc.starcore.config.Config;
import com.stardevllc.starcore.wrapper.PlayerHandWrapper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class StarSpawners extends JavaPlugin {
    
    private StarCore starCore;
    
    private Config mainConfig;
    private SpawnerManager spawnerManager;

    @Override
    public void onEnable() {
        this.starCore = (StarCore) Bukkit.getServer().getPluginManager().getPlugin("StarCore");
        this.mainConfig = new Config(new File(getDataFolder(), "config.yml"));
        this.mainConfig.addDefault("spawner.name", "&d{ENTITYNAME} Spawner", " The name for the spawner item", " The only applies to spawners that are picked up via silk touch", "You don't need the entity name in the display name. The plugin tracks the items using NBT Tags");
//        this.mainConfig.addDefault("spawner.unique", false, "This setting makes it so that all spawners that are created are unique", "This makes them unstackable and makes sure that they cannot be duped");
        
        this.mainConfig.addDefault("spawner.pickupmode", "DROP", " Controls the mode in how spawner items are handled when picked up using Silk Touch", " DROP is where the item is dropped on the ground where the spawner was located", " INVENTORY is where the item is given to the player that broke the block directly", " It is recommended on larger servers to change this to INVENTORY to help with item lag");
        this.mainConfig.addDefault("spawner.mintoolmaterial", "IRON", " Controls the minimum tool material needed to be able to silk touch a spawner");
        this.mainConfig.save();
        getServer().getPluginManager().registerEvents(new SpawnerListener(this), this);
        spawnerManager = new SpawnerManager(this);
        getCommand("spawner").setExecutor(new SpawnerCommand(this));
    }

    public SpawnerManager getSpawnerManager() {
        return spawnerManager;
    }

    public PlayerHandWrapper getPlayerHandWrapper() {
        return starCore.getPlayerHandWrapper();
    }

    public Config getMainConfig() {
        return mainConfig;
    }
}
