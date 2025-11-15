package com.stardevllc.starspawners.plugin;

import com.stardevllc.starmclib.StarMCLib;
import com.stardevllc.starmclib.plugin.ExtendedJavaPlugin;
import com.stardevllc.starspawners.SpawnerCommand;
import com.stardevllc.starspawners.StarSpawners;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class StandaloneStarSpawnersPlugin extends ExtendedJavaPlugin {
    @Override
    public void onEnable() {
        Plugin starmclibPlugin = Bukkit.getPluginManager().getPlugin("StarMCLib");
        if (starmclibPlugin != null) {
            getLogger().severe("StarMCLib plugin detected with the StarSpawners-Standalone plugin");
            getLogger().severe("Please either replace StarSpawners-Standalone with StarSpawners Plugin or remove StarMCLib plugin");
            getLogger().severe("Please see the wiki page for more information");
            getLogger().severe("https://github.com/StarDevelopmentLLC/StarSpawners/wiki/Available-Binaries");
        }
        
        Plugin starcorePlugin = Bukkit.getPluginManager().getPlugin("StarCore");
        if (starcorePlugin != null) {
            getLogger().severe("StarCore plugin detected with the StarSpawners-Standalone plugin");
            getLogger().severe("Please either replace StarSpawners-Standalone with StarSpawners Plugin or remove StarCore");
            getLogger().severe("Please see the wiki page for more information");
            getLogger().severe("https://github.com/StarDevelopmentLLC/StarSpawners/wiki/Available-Binaries");
        }
        
        StarMCLib.init(this);
        super.onEnable();
        StarSpawners.init(this);
        registerCommand("spawner", new SpawnerCommand(this));
    }
}