package com.woodspixel.core.commands;

import com.woodspixel.core.WoodsPixelCore;
import com.woodspixel.core.island.Island;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IslandCommand implements CommandExecutor {

    private final WoodsPixelCore plugin;

    public IslandCommand(WoodsPixelCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use island commands.");
            return true;
        }

        Island island = plugin.getIslandManager().getOrCreateIsland(player.getUniqueId());
        player.sendMessage(ChatColor.AQUA + "Teleporting you to your WoodsPixel island...");
        player.sendMessage(ChatColor.GRAY + "Island Level: " + island.getLevel() + " | Members: " + island.getMembers().size());
        player.sendMessage(ChatColor.GRAY + "Teleport placeholder: set island world/coordinates logic here.");
        return true;
    }
}
