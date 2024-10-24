package com.stardevllc.starspawners;

import com.stardevllc.starcore.NMSVersion;
import com.stardevllc.starcore.config.Config;
import com.stardevllc.starspawners.wrapper.PlayerHandWrapper;
import com.stardevllc.starspawners.wrapper.PlayerHandWrapper_1_8;
import com.stardevllc.starspawners.wrapper.PlayerHandWrapper_1_9;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class StarSpawners extends JavaPlugin {
    
    private Config mainConfig;
    private PlayerHandWrapper playerHandWrapper;
    
    private SpawnerManager spawnerManager;

    @Override
    public void onEnable() {
        this.mainConfig = new Config(new File(getDataFolder(), "config.yml"));
        this.mainConfig.addDefault("spawner.name", "&d{ENTITYNAME} Spawner", " The name for the spawner item", " The only applies to spawners that are picked up via silk touch", "You don't need the entity name in the display name. The plugin tracks the items using NBT Tags");
//        this.mainConfig.addDefault("spawner.unique", false, "This setting makes it so that all spawners that are created are unique", "This makes them unstackable and makes sure that they cannot be duped");
        
        this.mainConfig.addDefault("spawner.pickupmode", "DROP", " Controls the mode in how spawner items are handled when picked up using Silk Touch", " DROP is where the item is dropped on the ground where the spawner was located", " INVENTORY is where the item is given to the player that broke the block directly", " It is recommended on larger servers to change this to INVENTORY to help with item lag");
        
        if (NMSVersion.CURRENT_VERSION.ordinal() <= NMSVersion.v1_8_R3.ordinal()) {
            playerHandWrapper = new PlayerHandWrapper_1_8();
        } else {
            playerHandWrapper = new PlayerHandWrapper_1_9();
        }
        
        getServer().getPluginManager().registerEvents(new SpawnerListener(this), this);
        
        spawnerManager = new SpawnerManager(this);
        
        getCommand("spawner").setExecutor(new SpawnerCommand(this));
    }

    public SpawnerManager getSpawnerManager() {
        return spawnerManager;
    }

    public PlayerHandWrapper getPlayerHandWrapper() {
        return playerHandWrapper;
    }

    public Config getMainConfig() {
        return mainConfig;
    }
}
