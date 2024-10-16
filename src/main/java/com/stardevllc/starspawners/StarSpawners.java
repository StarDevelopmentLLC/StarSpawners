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
