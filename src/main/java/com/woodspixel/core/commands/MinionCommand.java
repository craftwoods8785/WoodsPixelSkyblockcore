package com.woodspixel.core.commands;

import com.woodspixel.core.WoodsPixelCore;
import com.woodspixel.core.minions.Minion;
import com.woodspixel.core.minions.MinionType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MinionCommand implements CommandExecutor {

    private final WoodsPixelCore plugin;

    public MinionCommand(WoodsPixelCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can manage minions.");
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("create")) {
            MinionType type = MinionType.COBBLESTONE;
            if (args.length > 1) {
                try {
                    type = MinionType.valueOf(args[1].toUpperCase());
                } catch (IllegalArgumentException ignored) {
                    player.sendMessage(ChatColor.RED + "Unknown minion type. Using COBBLESTONE.");
                }
            }

            plugin.getMinionManager().createMinion(player.getUniqueId(), type);
            player.sendMessage(ChatColor.GREEN + "Created a " + type.name() + " minion.");
            return true;
        }

        plugin.getMinionManager().tickProduction(player.getUniqueId());
        List<Minion> minions = plugin.getMinionManager().getMinions(player.getUniqueId());

        player.sendMessage(ChatColor.LIGHT_PURPLE + "=== WoodsPixel Minions ===");
        player.sendMessage(ChatColor.GRAY + "Total Minions: " + minions.size());
        for (Minion minion : minions) {
            player.sendMessage(ChatColor.AQUA + minion.getType().name() + ChatColor.GRAY
                    + " | Level " + minion.getLevel()
                    + " | Stored: " + minion.getStoredItems());
        }
        player.sendMessage(ChatColor.GRAY + "Use /minion create <type> to create a minion.");
        player.sendMessage(ChatColor.GRAY + "GUI placeholder: minion management menu can be added here.");
        return true;
    }
}
