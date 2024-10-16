package com.stardevllc.starspawners;

import com.stardevllc.starcore.color.ColorHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnerCommand implements CommandExecutor {
    private StarSpawners plugin;

    public SpawnerCommand(StarSpawners plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorHandler.getInstance().color("&cOnly players can use that command"));
            return true;
        }

        if (!(args.length > 0)) {
            sender.sendMessage(ColorHandler.getInstance().color("&cYou must provide a sub command."));
            return true;
        }

        if (!player.hasPermission("starspawners.admin")) {
            player.sendMessage(ColorHandler.getInstance().color("&cYou do not have permission to use that command"));
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            if (args.length != 4) {
                player.sendMessage(ColorHandler.getInstance().color("/" + label + " give <player> <entityType> <amount>"));
                return true;
            }
            Player p = Bukkit.getPlayer(args[1]);
            String spawner = args[2].toUpperCase();
            int amount = Integer.parseInt(args[3]);
            plugin.getSpawnerManager().giveSpawner(p, amount, spawner);
        } else if (args[0].equalsIgnoreCase("set")) {
            if (args.length != 2) {
                player.sendMessage(ColorHandler.getInstance().color("/" + label + " set <entityType>"));
                return true;
            }
            Block block = player.getTargetBlock(null, 4);
            if (!block.getType().equals(Material.SPAWNER)) {
                player.sendMessage(ColorHandler.getInstance().color("&cThe block you are looking at is not a spawner."));
                return true;
            }

            EntityType entityType;

            try {
                entityType = EntityType.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                player.sendMessage(ColorHandler.getInstance().color("&cInvalid entity type: " + args[1]));
                return true;
            }

            CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
            try {
                plugin.getSpawnerManager().setSpawnerType(creatureSpawner, entityType);
            } catch (Exception e) {
                player.sendMessage(ColorHandler.getInstance().color("&cCould not set " + entityType.name() + " to that spawner"));
            }
        }
        return true;
    }
}
