package com.woodspixel.core.database;

import com.woodspixel.core.island.Island;
import com.woodspixel.core.minions.Minion;
import com.woodspixel.core.player.PlayerData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {

    private final DatabaseConnector connector;

    public DatabaseManager(DatabaseConnector connector) {
        this.connector = connector;
    }

    private Connection requireConnection() throws SQLException {
        Connection connection = connector.getConnection();
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Database connection is not available.");
        }
        return connection;
    }

    public void initializeTables() throws SQLException {
        Connection connection = requireConnection();

        connection.prepareStatement("""
                CREATE TABLE IF NOT EXISTS wp_players (
                    uuid VARCHAR(36) PRIMARY KEY,
                    player_name VARCHAR(16) NOT NULL,
                    coins DOUBLE NOT NULL,
                    mining INT NOT NULL,
                    farming INT NOT NULL,
                    combat INT NOT NULL,
                    foraging INT NOT NULL,
                    fishing INT NOT NULL,
                    alchemy INT NOT NULL,
                    enchanting INT NOT NULL
                )
                """).executeUpdate();

        connection.prepareStatement("""
                CREATE TABLE IF NOT EXISTS wp_islands (
                    owner_uuid VARCHAR(36) PRIMARY KEY,
                    level INT NOT NULL,
                    members TEXT NOT NULL
                )
                """).executeUpdate();

        connection.prepareStatement("""
                CREATE TABLE IF NOT EXISTS wp_minions (
                    owner_uuid VARCHAR(36) NOT NULL,
                    minion_type VARCHAR(32) NOT NULL,
                    level INT NOT NULL,
                    stored_items INT NOT NULL,
                    last_collection BIGINT NOT NULL,
                    PRIMARY KEY (owner_uuid, minion_type)
                )
                """).executeUpdate();
    }

    public void savePlayerData(PlayerData playerData) throws SQLException {
        String sql = """
                INSERT INTO wp_players (uuid, player_name, coins, mining, farming, combat, foraging, fishing, alchemy, enchanting)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    player_name = VALUES(player_name),
                    coins = VALUES(coins),
                    mining = VALUES(mining),
                    farming = VALUES(farming),
                    combat = VALUES(combat),
                    foraging = VALUES(foraging),
                    fishing = VALUES(fishing),
                    alchemy = VALUES(alchemy),
                    enchanting = VALUES(enchanting)
                """;

        try (PreparedStatement statement = requireConnection().prepareStatement(sql)) {
            statement.setString(1, playerData.getUuid().toString());
            statement.setString(2, playerData.getPlayerName());
            statement.setDouble(3, playerData.getCoins());
            statement.setInt(4, playerData.getSkillLevel(PlayerData.SkillType.MINING));
            statement.setInt(5, playerData.getSkillLevel(PlayerData.SkillType.FARMING));
            statement.setInt(6, playerData.getSkillLevel(PlayerData.SkillType.COMBAT));
            statement.setInt(7, playerData.getSkillLevel(PlayerData.SkillType.FORAGING));
            statement.setInt(8, playerData.getSkillLevel(PlayerData.SkillType.FISHING));
            statement.setInt(9, playerData.getSkillLevel(PlayerData.SkillType.ALCHEMY));
            statement.setInt(10, playerData.getSkillLevel(PlayerData.SkillType.ENCHANTING));
            statement.executeUpdate();
        }
    }

    public PlayerData loadPlayerData(UUID uuid) throws SQLException {
        String sql = "SELECT * FROM wp_players WHERE uuid = ?";

        try (PreparedStatement statement = requireConnection().prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                PlayerData data = new PlayerData(uuid, rs.getString("player_name"));
                data.setCoins(rs.getDouble("coins"));
                data.setSkillLevel(PlayerData.SkillType.MINING, rs.getInt("mining"));
                data.setSkillLevel(PlayerData.SkillType.FARMING, rs.getInt("farming"));
                data.setSkillLevel(PlayerData.SkillType.COMBAT, rs.getInt("combat"));
                data.setSkillLevel(PlayerData.SkillType.FORAGING, rs.getInt("foraging"));
                data.setSkillLevel(PlayerData.SkillType.FISHING, rs.getInt("fishing"));
                data.setSkillLevel(PlayerData.SkillType.ALCHEMY, rs.getInt("alchemy"));
                data.setSkillLevel(PlayerData.SkillType.ENCHANTING, rs.getInt("enchanting"));
                return data;
            }
        }
    }

    public void saveIsland(Island island) throws SQLException {
        String members = island.getMembers().stream().map(UUID::toString).reduce((a, b) -> a + "," + b).orElse("");
        String sql = """
                INSERT INTO wp_islands (owner_uuid, level, members)
                VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    level = VALUES(level),
                    members = VALUES(members)
                """;

        try (PreparedStatement statement = requireConnection().prepareStatement(sql)) {
            statement.setString(1, island.getOwner().toString());
            statement.setInt(2, island.getLevel());
            statement.setString(3, members);
            statement.executeUpdate();
        }
    }

    public Island loadIsland(UUID owner) {
        return new Island(owner);
    }

    public void saveMinion(Minion minion) throws SQLException {
        String sql = """
                INSERT INTO wp_minions (owner_uuid, minion_type, level, stored_items, last_collection)
                VALUES (?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    level = VALUES(level),
                    stored_items = VALUES(stored_items),
                    last_collection = VALUES(last_collection)
                """;

        try (PreparedStatement statement = requireConnection().prepareStatement(sql)) {
            statement.setString(1, minion.getOwner().toString());
            statement.setString(2, minion.getType().name());
            statement.setInt(3, minion.getLevel());
            statement.setInt(4, minion.getStoredItems());
            statement.setLong(5, minion.getLastCollection().toEpochMilli());
            statement.executeUpdate();
        }
    }

    public List<Minion> loadMinions(UUID owner) {
        return new ArrayList<>();
    }
}
