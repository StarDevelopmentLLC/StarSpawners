package com.stardevllc.starspawners.wrapper;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerHandWrapper_1_9 implements PlayerHandWrapper {
    @Override
    public ItemStack getItemInMainHand(Player player) {
        return player.getInventory().getItemInMainHand();
    }

    @Override
    public ItemStack getItemInOffHand(Player player) {
        return player.getInventory().getItemInOffHand();
    }
}
