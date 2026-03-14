package com.woodspixel.core.database;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private final JavaPlugin plugin;
    private Connection connection;

    public DatabaseConnector(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void connect() throws SQLException {
        if (isConnected()) {
            return;
        }

        String host = plugin.getConfig().getString("mysql.host", "localhost");
        int port = plugin.getConfig().getInt("mysql.port", 3306);
        String database = plugin.getConfig().getString("mysql.database", "woodspixel");
        String username = plugin.getConfig().getString("mysql.username", "root");
        String password = plugin.getConfig().getString("mysql.password", "");
        boolean ssl = plugin.getConfig().getBoolean("mysql.ssl", false);

        String jdbcUrl = String.format(
                "jdbc:mysql://%s:%d/%s?useSSL=%s&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                host, port, database, ssl
        );

        connection = DriverManager.getConnection(jdbcUrl, username, password);
        plugin.getLogger().info("Connected to MySQL database for WoodsPixel Core.");
    }

    public void disconnect() {
        if (!isConnected()) {
            return;
        }
        try {
            connection.close();
            plugin.getLogger().info("Disconnected from MySQL database.");
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to close MySQL connection: " + e.getMessage());
        } finally {
            connection = null;
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
