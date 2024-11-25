package com.stardevllc.spawners.v1_21;

import com.stardevllc.spawners.base.AbstractStarSpawner;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;

public class StarSpawner_1_21 extends AbstractStarSpawner {
    public StarSpawner_1_21(Location spawnerLocation) {
        super(spawnerLocation);
    }

    @Override
    public int getMinSpawnDelay() {
        BlockState blockState = this.spawnerLocation.getBlock().getState();
        if (blockState instanceof CreatureSpawner creatureSpawner) {
            return creatureSpawner.getMinSpawnDelay();
        }
        
        return 0;
    }

    @Override
    public void setMinSpawnDelay(int var1) {
        BlockState blockState = this.spawnerLocation.getBlock().getState();
        if (blockState instanceof CreatureSpawner creatureSpawner) {
            creatureSpawner.setMinSpawnDelay(var1);
        }
    }

    @Override
    public int getMaxSpawnDelay() {
        BlockState blockState = this.spawnerLocation.getBlock().getState();
        if (blockState instanceof CreatureSpawner creatureSpawner) {
            return creatureSpawner.getMaxSpawnDelay();
        }
        return 0;
    }

    @Override
    public void setMaxSpawnDelay(int var1) {
        BlockState blockState = this.spawnerLocation.getBlock().getState();
        if (blockState instanceof CreatureSpawner creatureSpawner) {
            creatureSpawner.setMaxSpawnDelay(var1);
        }
    }

    @Override
    public int getSpawnCount() {
        BlockState blockState = this.spawnerLocation.getBlock().getState();
        if (blockState instanceof CreatureSpawner creatureSpawner) {
            return creatureSpawner.getSpawnCount();
        }
        return 0;
    }

    @Override
    public void setSpawnCount(int var1) {
        BlockState blockState = this.spawnerLocation.getBlock().getState();
        if (blockState instanceof CreatureSpawner creatureSpawner) {
            creatureSpawner.setSpawnCount(var1);
        }
    }

    @Override
    public int getMaxNearbyEntities() {
        BlockState blockState = this.spawnerLocation.getBlock().getState();
        if (blockState instanceof CreatureSpawner creatureSpawner) {
            return creatureSpawner.getMaxNearbyEntities();
        }
        return 0;
    }

    @Override
    public void setMaxNearbyEntities(int var1) {
        BlockState blockState = this.spawnerLocation.getBlock().getState();
        if (blockState instanceof CreatureSpawner creatureSpawner) {
            creatureSpawner.setMaxNearbyEntities(var1);
        }
    }

    @Override
    public int getRequiredPlayerRange() {
        BlockState blockState = this.spawnerLocation.getBlock().getState();
        if (blockState instanceof CreatureSpawner creatureSpawner) {
            return creatureSpawner.getRequiredPlayerRange();
        }
        return 0;
    }

    @Override
    public void setRequiredPlayerRange(int var1) {
        BlockState blockState = this.spawnerLocation.getBlock().getState();
        if (blockState instanceof CreatureSpawner creatureSpawner) {
            creatureSpawner.setRequiredPlayerRange(var1);
        }
    }

    @Override
    public int getSpawnRange() {
        BlockState blockState = this.spawnerLocation.getBlock().getState();
        if (blockState instanceof CreatureSpawner creatureSpawner) {
            return creatureSpawner.getSpawnRange();
        }
        return 0;
    }

    @Override
    public void setSpawnRange(int var1) {
        BlockState blockState = this.spawnerLocation.getBlock().getState();
        if (blockState instanceof CreatureSpawner creatureSpawner) {
            creatureSpawner.setSpawnRange(var1);
        }
    }

//    @Override
//    public StarEntitySnapshot getSpawnedEntity() {
//        BlockState blockState = this.spawnerLocation.getBlock().getState();
//        if (blockState instanceof CreatureSpawner creatureSpawner) {
//            return new StarEntitySnapshot_1_21(creatureSpawner.getSpawnedEntity());
//        }
//        
//        return null;
//    }
//
//    @Override
//    public void setSpawnedEntity(StarEntitySnapshot var1) {
//        BlockState blockState = this.spawnerLocation.getBlock().getState();
//        if (blockState instanceof CreatureSpawner creatureSpawner) {
//            creatureSpawner.setSpawnedEntity((EntitySnapshot) var1);
//        }
//    }

//    @Override
//    public void setSpawnedEntity(StarSpawnerEntry var1) {
//        
//    }
//
//    @Override
//    public void addPotentialSpawn(StarEntitySnapshot var1, int var2, StarSpawnRule var3) {
//
//    }
//
//    @Override
//    public void addPotentialSpawn(StarSpawnerEntry var1) {
//
//    }
//
//    @Override
//    public void setPotentialSpawns(Collection<StarSpawnerEntry> var1) {
//
//    }
//
//    @Override
//    public List<StarSpawnerEntry> getPotentialSpawns() {
//        return List.of();
//    }
}
