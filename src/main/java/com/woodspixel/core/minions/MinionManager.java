package com.woodspixel.core.minions;

import com.woodspixel.core.database.DatabaseManager;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MinionManager {

    private final DatabaseManager databaseManager;
    private final Map<UUID, List<Minion>> minionsByOwner;
    private final long collectionIntervalSeconds;

    public MinionManager(DatabaseManager databaseManager, long collectionIntervalSeconds) {
        this.databaseManager = databaseManager;
        this.collectionIntervalSeconds = collectionIntervalSeconds;
        this.minionsByOwner = new HashMap<>();
    }

    public List<Minion> getMinions(UUID owner) {
        return minionsByOwner.computeIfAbsent(owner, k -> new ArrayList<>());
    }

    public Minion createMinion(UUID owner, MinionType type) {
        Minion minion = new Minion(owner, type);
        getMinions(owner).add(minion);
        return minion;
    }

    public void tickProduction(UUID owner) {
        for (Minion minion : getMinions(owner)) {
            long elapsed = Duration.between(minion.getLastCollection(), Instant.now()).getSeconds();
            if (elapsed >= collectionIntervalSeconds) {
                int cycles = (int) (elapsed / collectionIntervalSeconds);
                int generated = Math.max(1, minion.getLevel()) * cycles;
                minion.addStoredItems(generated);
                minion.setLastCollection(minion.getLastCollection().plusSeconds(cycles * collectionIntervalSeconds));
            }
        }
    }

    public void saveOwnerMinions(UUID owner) {
        for (Minion minion : getMinions(owner)) {
            try {
                databaseManager.saveMinion(minion);
            } catch (SQLException ignored) {
                // Logging can be added later.
            }
        }
    }

    public void saveAll() {
        for (UUID owner : minionsByOwner.keySet()) {
            saveOwnerMinions(owner);
        }
    }
}
