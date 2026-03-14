package com.woodspixel.core;

import com.woodspixel.core.commands.IslandCommand;
import com.woodspixel.core.commands.MinionCommand;
import com.woodspixel.core.commands.PlayerCommand;
import com.woodspixel.core.commands.WPCommand;
import com.woodspixel.core.database.DatabaseConnector;
import com.woodspixel.core.database.DatabaseManager;
import com.woodspixel.core.island.IslandManager;
import com.woodspixel.core.minions.MinionManager;
import com.woodspixel.core.player.PlayerManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Objects;

public class WoodsPixelCore extends JavaPlugin {

    private DatabaseConnector databaseConnector;
    private DatabaseManager databaseManager;
    private PlayerManager playerManager;
    private IslandManager islandManager;
    private MinionManager minionManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        setupDatabase();

        this.playerManager = new PlayerManager(databaseManager);
        this.islandManager = new IslandManager(databaseManager, getConfig().getInt("island.max-members", 5));
        this.minionManager = new MinionManager(databaseManager, getConfig().getLong("minion.collection-interval-seconds", 30));

        registerCommands();

        getLogger().info("WoodsPixelCore enabled successfully.");
    }

    @Override
    public void onDisable() {
        if (playerManager != null) {
            playerManager.saveAll();
        }
        if (islandManager != null) {
            islandManager.saveAll();
        }
        if (minionManager != null) {
            minionManager.saveAll();
        }
        if (databaseConnector != null) {
            databaseConnector.disconnect();
        }

        getLogger().info("WoodsPixelCore disabled.");
    }

    private void setupDatabase() {
        this.databaseConnector = new DatabaseConnector(this);
        try {
            databaseConnector.connect();
        } catch (SQLException e) {
            getLogger().warning("Could not connect to MySQL on startup. Plugin will continue with in-memory runtime data.");
            getLogger().warning("Reason: " + e.getMessage());
        }

        this.databaseManager = new DatabaseManager(databaseConnector);
        if (databaseConnector.isConnected()) {
            try {
                databaseManager.initializeTables();
            } catch (SQLException e) {
                getLogger().warning("Failed to initialize database tables: " + e.getMessage());
            }
        }
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("wp"), "Command /wp not defined in plugin.yml").setExecutor(new WPCommand(this));
        Objects.requireNonNull(getCommand("is"), "Command /is not defined in plugin.yml").setExecutor(new IslandCommand(this));
        Objects.requireNonNull(getCommand("player"), "Command /player not defined in plugin.yml").setExecutor(new PlayerCommand(this));
        Objects.requireNonNull(getCommand("minion"), "Command /minion not defined in plugin.yml").setExecutor(new MinionCommand(this));
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public IslandManager getIslandManager() {
        return islandManager;
    }

    public MinionManager getMinionManager() {
        return minionManager;
    }
}
