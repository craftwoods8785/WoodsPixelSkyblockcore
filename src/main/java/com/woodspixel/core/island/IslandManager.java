package com.woodspixel.core.island;

import com.woodspixel.core.database.DatabaseManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IslandManager {

    private final DatabaseManager databaseManager;
    private final Map<UUID, Island> islandsByOwner;
    private final int maxMembers;

    public IslandManager(DatabaseManager databaseManager, int maxMembers) {
        this.databaseManager = databaseManager;
        this.maxMembers = maxMembers;
        this.islandsByOwner = new HashMap<>();
    }

    public Island getOrCreateIsland(UUID owner) {
        return islandsByOwner.computeIfAbsent(owner, Island::new);
    }

    public Island getIslandByOwner(UUID owner) {
        return islandsByOwner.get(owner);
    }

    public boolean addMember(UUID owner, UUID member) {
        Island island = getOrCreateIsland(owner);
        return island.addMember(member, maxMembers);
    }

    public boolean upgradeIsland(UUID owner, double upgradeCost, double currentCoins) {
        if (currentCoins < upgradeCost) {
            return false;
        }
        Island island = getOrCreateIsland(owner);
        island.upgradeLevel();
        return true;
    }

    public void saveIsland(UUID owner) {
        Island island = islandsByOwner.get(owner);
        if (island == null) {
            return;
        }

        try {
            databaseManager.saveIsland(island);
        } catch (SQLException ignored) {
            // Logging can be added at higher layers.
        }
    }

    public void saveAll() {
        for (UUID owner : islandsByOwner.keySet()) {
            saveIsland(owner);
        }
    }
}
