package com.woodspixel.core.player;

import com.woodspixel.core.database.DatabaseManager;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {

    private final DatabaseManager databaseManager;
    private final Map<UUID, PlayerData> loadedPlayers;

    public PlayerManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.loadedPlayers = new ConcurrentHashMap<>();
    }

    public PlayerData getOrCreatePlayerData(Player player) {
        return loadedPlayers.computeIfAbsent(player.getUniqueId(), uuid -> {
            try {
                PlayerData data = databaseManager.loadPlayerData(uuid);
                return data != null ? data : new PlayerData(uuid, player.getName());
            } catch (SQLException e) {
                return new PlayerData(uuid, player.getName());
            }
        });
    }

    public PlayerData getPlayerData(UUID uuid) {
        return loadedPlayers.get(uuid);
    }

    public void savePlayerData(UUID uuid) {
        PlayerData data = loadedPlayers.get(uuid);
        if (data == null) {
            return;
        }

        try {
            databaseManager.savePlayerData(data);
        } catch (SQLException ignored) {
            // Keep game flow intact; logging can be added in calling layer.
        }
    }

    public void saveAll() {
        for (UUID uuid : loadedPlayers.keySet()) {
            savePlayerData(uuid);
        }
    }

    public void unloadPlayer(UUID uuid) {
        savePlayerData(uuid);
        loadedPlayers.remove(uuid);
    }
}
