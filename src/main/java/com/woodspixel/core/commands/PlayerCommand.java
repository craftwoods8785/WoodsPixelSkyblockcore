package com.woodspixel.core.commands;

import com.woodspixel.core.WoodsPixelCore;
import com.woodspixel.core.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommand implements CommandExecutor {

    private final WoodsPixelCore plugin;

    public PlayerCommand(WoodsPixelCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can view WoodsPixel stats.");
            return true;
        }

        PlayerData data = plugin.getPlayerManager().getOrCreatePlayerData(player);
        player.sendMessage(ChatColor.GOLD + "=== WoodsPixel Player Stats ===");
        player.sendMessage(ChatColor.YELLOW + "Coins: " + ChatColor.WHITE + String.format("%,.2f", data.getCoins()));
        for (PlayerData.SkillType skillType : PlayerData.SkillType.values()) {
            player.sendMessage(ChatColor.GREEN + skillType.name() + ": " + ChatColor.WHITE + data.getSkillLevel(skillType));
        }
        player.sendMessage(ChatColor.GRAY + "GUI placeholder: replace with full profile menu if needed.");
        return true;
    }
}
