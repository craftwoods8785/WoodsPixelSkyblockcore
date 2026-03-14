package com.woodspixel.core.player;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {

    public enum SkillType {
        MINING,
        FARMING,
        COMBAT,
        FORAGING,
        FISHING,
        ALCHEMY,
        ENCHANTING
    }

    private final UUID uuid;
    private final String playerName;
    private double coins;
    private final Map<SkillType, Integer> skillLevels;

    public PlayerData(UUID uuid, String playerName) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.coins = 0.0;
        this.skillLevels = new EnumMap<>(SkillType.class);
        for (SkillType type : SkillType.values()) {
            this.skillLevels.put(type, 1);
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public double getCoins() {
        return coins;
    }

    public void setCoins(double coins) {
        this.coins = Math.max(0, coins);
    }

    public void addCoins(double amount) {
        if (amount > 0) {
            this.coins += amount;
        }
    }

    public boolean removeCoins(double amount) {
        if (amount <= 0 || coins < amount) {
            return false;
        }
        coins -= amount;
        return true;
    }

    public int getSkillLevel(SkillType skillType) {
        return skillLevels.getOrDefault(skillType, 1);
    }

    public void setSkillLevel(SkillType skillType, int level) {
        skillLevels.put(skillType, Math.max(1, level));
    }

    public void increaseSkillLevel(SkillType skillType, int amount) {
        if (amount > 0) {
            setSkillLevel(skillType, getSkillLevel(skillType) + amount);
        }
    }

    public Map<SkillType, Integer> getSkillLevels() {
        return new EnumMap<>(skillLevels);
    }
}
