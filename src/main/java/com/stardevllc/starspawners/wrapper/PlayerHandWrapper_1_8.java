package com.stardevllc.starspawners.wrapper;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.lang.reflect.Method;

public class PlayerHandWrapper_1_8 implements PlayerHandWrapper {
    
    private static Method getItemInHandMethod;
    
    static {
        try {
            getItemInHandMethod = PlayerInventory.class.getDeclaredMethod("getItemInHand");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public ItemStack getItemInMainHand(Player player) {
        try {
            return (ItemStack) getItemInHandMethod.invoke(player.getInventory());
        } catch (Throwable e) {
            return null;
        }
    }

    @Override
    public ItemStack getItemInOffHand(Player player) {
        return null;
    }
}
