package com.stardevllc.starspawners.plugin;

import com.stardevllc.starmclib.plugin.ExtendedJavaPlugin;
import com.stardevllc.starspawners.SpawnerCommand;
import com.stardevllc.starspawners.StarSpawners;

public class StarSpawnersPlugin extends ExtendedJavaPlugin {
    @Override
    public void onEnable() {
        super.onEnable();
        StarSpawners.init(this);
        registerCommand("spawner", new SpawnerCommand(this));
    }
}