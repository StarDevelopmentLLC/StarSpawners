package com.stardevllc.spawners;

import com.stardevllc.starcore.api.StarColors;
import com.stardevllc.starlib.injector.Inject;
import com.stardevllc.starmclib.names.EntityNames;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpawnerCommand implements CommandExecutor {
    @Inject
    private StarSpawners plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(StarColors.color("&cOnly players can use that command"));
            return true;
        }

        if (!(args.length > 0)) {
            sender.sendMessage(StarColors.color("&cYou must provide a sub command."));
            return true;
        }

        if (!player.hasPermission("starspawners.admin")) {
            player.sendMessage(StarColors.color("&cYou do not have permission to use that command."));
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            if (!player.hasPermission("starspawners.admin.give")) {
                StarColors.coloredMessage(sender, "&cYou do not have permission to use that command.");
                return true;
            }
            
            if (!(args.length >= 2)) {
                player.sendMessage(StarColors.color("&c/" + label + " give <entityType> [player] [amount]"));
                return true;
            }
            
            EntityType entityType;
            
            try {
                entityType = EntityType.valueOf(args[1].toUpperCase());
            } catch (Exception e) {
                player.sendMessage(StarColors.color("&cInvalid entity type: " + args[1]));
                return true;
            }
            
            if (!entityType.isSpawnable()) {
                player.sendMessage(StarColors.color("&cThat entity is not spawnable by mob spawners."));
                return true;
            }
            
            if (!player.hasPermission("starspawners.admin.give." + entityType.name().toLowerCase())) {
                StarColors.coloredMessage(sender, "&cYou do not have permission to give " + EntityNames.getDefaultName(entityType).toLowerCase() + " spawners.");
                return true;
            }
            
            Player target = null;
            
            if (args.length > 2) {
                target = plugin.getServer().getPlayer(args[2]);
                if (target == null && args.length > 3) {
                    target = plugin.getServer().getPlayer(args[3]);
                }
            }
            
            if (target == null) {
                target = player;
            }
            
            if (!target.getUniqueId().equals(player.getUniqueId())) {
                if (!player.hasPermission("starspawners.admin.give.others")) {
                    StarColors.coloredMessage(sender, "&cYou do not have permission to give spawners to other players.");
                    return true;
                }
            }
            
            int amount = 0;
            if (args.length > 2) {
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    try {
                        amount = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e2) {}
                }
            }
            
            if (amount == 0) {
                amount = 1;
            }

            ItemStack spawnerItemStack = plugin.getSpawnerManager().createSpawnerItemStack(entityType, 0);
            spawnerItemStack.setAmount(amount);
            target.getInventory().addItem(spawnerItemStack);
            StarColors.coloredMessage(target, "&aYou have been given a(n) " + EntityNames.getDefaultName(entityType) + " spawner.");
        } else if (args[0].equalsIgnoreCase("set")) {
            if (!player.hasPermission("starspawners.admin.set")) {
                StarColors.coloredMessage(sender, "&cYou do not have permission to use that command.");
                return true;
            }
            
            if (args.length != 2) {
                player.sendMessage(StarColors.color("/" + label + " set <entityType>"));
                return true;
            }
            
            Block block = player.getTargetBlock(null, 4);
            if (block.getType() != Material.SPAWNER) {
                player.sendMessage(StarColors.color("&cThe block you are looking at is not a spawner."));
                return true;
            }

            EntityType entityType;

            try {
                entityType = EntityType.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                player.sendMessage(StarColors.color("&cInvalid entity type: " + args[1]));
                return true;
            }

            if (!entityType.isSpawnable()) {
                player.sendMessage(StarColors.color("&cThat entity is not spawnable by mob spawners."));
                return true;
            }

            if (!player.hasPermission("starspawners.admin.give." + entityType.name().toLowerCase())) {
                StarColors.coloredMessage(sender, "&cYou do not have permission to set " + EntityNames.getDefaultName(entityType).toLowerCase() + " spawners.");
                return true;
            }

            CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
            try {
                plugin.getSpawnerManager().setSpawnerType(creatureSpawner, entityType, 0);
            } catch (Exception e) {
                player.sendMessage(StarColors.color("&cCould not set " + entityType.name() + " to that spawner"));
            }
        }
        return true;
    }
}
