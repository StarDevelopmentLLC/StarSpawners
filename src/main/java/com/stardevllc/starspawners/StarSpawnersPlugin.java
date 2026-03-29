package com.stardevllc.starspawners;

import com.stardevllc.plugin.ExtendedJavaPlugin;

public class StarSpawnersPlugin extends ExtendedJavaPlugin {
    @Override
    public void onEnable() {
        super.onEnable();
        StarSpawners.init(this);
        registerCommand("spawner", new SpawnerCommand(this));
    }
}