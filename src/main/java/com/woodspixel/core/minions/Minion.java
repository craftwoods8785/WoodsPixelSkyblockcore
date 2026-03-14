package com.woodspixel.core.minions;

import java.time.Instant;
import java.util.UUID;

public class Minion {

    private final UUID owner;
    private final MinionType type;
    private int level;
    private int storedItems;
    private Instant lastCollection;

    public Minion(UUID owner, MinionType type) {
        this.owner = owner;
        this.type = type;
        this.level = 1;
        this.storedItems = 0;
        this.lastCollection = Instant.now();
    }

    public UUID getOwner() {
        return owner;
    }

    public MinionType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = Math.max(1, level);
    }

    public void levelUp() {
        level++;
    }

    public int getStoredItems() {
        return storedItems;
    }

    public void setStoredItems(int storedItems) {
        this.storedItems = Math.max(0, storedItems);
    }

    public void addStoredItems(int amount) {
        if (amount > 0) {
            this.storedItems += amount;
        }
    }

    public int collectItems() {
        int collected = storedItems;
        storedItems = 0;
        lastCollection = Instant.now();
        return collected;
    }

    public Instant getLastCollection() {
        return lastCollection;
    }

    public void setLastCollection(Instant lastCollection) {
        this.lastCollection = lastCollection;
    }
}
