package com.stardevllc.starspawners.plugin;

import com.stardevllc.starmclib.StarMCLib;
import com.stardevllc.starmclib.plugin.ExtendedJavaPlugin;
import com.stardevllc.starspawners.SpawnerCommand;
import com.stardevllc.starspawners.StarSpawners;

public class StandaloneStarItemsPlugin extends ExtendedJavaPlugin {
    @Override
    public void onEnable() {
        StarMCLib.init(this);
        super.onEnable();
        StarMCLib.registerPluginInjector(this, injector);
        StarSpawners.init(this);
        registerCommand("spawner", new SpawnerCommand(this));
    }
}