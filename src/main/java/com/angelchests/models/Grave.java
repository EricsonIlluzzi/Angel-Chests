package com.angelchests.models;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Grave {
    private final UUID ownerUUID;
    private final String ownerName;
    private final Location location;
    private final List<ItemStack> items;
    private final int experience;
    private final long creationTime;
    private final long expirationTime;

    public Grave(UUID ownerUUID, String ownerName, Location location, 
                 List<ItemStack> items, int experience, long expirationSeconds) {
        this.ownerUUID = ownerUUID;
        this.ownerName = ownerName;
        this.location = location;
        this.items = new ArrayList<>(items);
        this.experience = experience;
        this.creationTime = System.currentTimeMillis();
        this.expirationTime = creationTime + (expirationSeconds * 1000);
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public Location getLocation() {
        return location;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public int getExperience() {
        return experience;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= expirationTime;
    }

    public long getRemainingSeconds() {
        long remaining = (expirationTime - System.currentTimeMillis()) / 1000;
        return Math.max(0, remaining);
    }

    public String getFormattedTimeRemaining() {
        long seconds = getRemainingSeconds();
        long minutes = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }
}
