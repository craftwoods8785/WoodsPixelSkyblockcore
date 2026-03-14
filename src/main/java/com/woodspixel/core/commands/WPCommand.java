package com.woodspixel.core.commands;

import com.woodspixel.core.WoodsPixelCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WPCommand implements CommandExecutor {

    private final WoodsPixelCore plugin;

    public WPCommand(WoodsPixelCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can open the WoodsPixel menu.");
            return true;
        }

        plugin.getPlayerManager().getOrCreatePlayerData(player);
        player.sendMessage(ChatColor.GOLD + "Opening WoodsPixel main menu...");
        player.sendMessage(ChatColor.GRAY + "GUI placeholder: implement WoodsPixel SkyBlock menu here.");
        return true;
    }
}
