package com.woodspixel.core.island;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Island {

    private final UUID owner;
    private int level;
    private final Set<UUID> members;

    public Island(UUID owner) {
        this.owner = owner;
        this.level = 1;
        this.members = new HashSet<>();
        this.members.add(owner);
    }

    public UUID getOwner() {
        return owner;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = Math.max(1, level);
    }

    public void upgradeLevel() {
        this.level++;
    }

    public boolean addMember(UUID uuid, int maxMembers) {
        if (members.size() >= maxMembers) {
            return false;
        }
        return members.add(uuid);
    }

    public boolean removeMember(UUID uuid) {
        if (owner.equals(uuid)) {
            return false;
        }
        return members.remove(uuid);
    }

    public boolean isMember(UUID uuid) {
        return members.contains(uuid);
    }

    public Set<UUID> getMembers() {
        return Collections.unmodifiableSet(members);
    }
}
