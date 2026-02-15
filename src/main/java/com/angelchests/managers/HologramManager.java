package com.angelchests.managers;

import com.angelchests.models.Grave;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HologramManager {
    private final Map<Location, ArmorStand> holograms;

    public HologramManager() {
        this.holograms = new ConcurrentHashMap<>();
    }

    public void createHologram(Grave grave) {
        Location loc = grave.getLocation().clone().add(0.5, 1.5, 0.5);
        
        ArmorStand hologram = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        hologram.setVisible(false);
        hologram.setGravity(false);
        hologram.setCustomNameVisible(true);
        hologram.setMarker(true);
        hologram.setInvulnerable(true);
        
        updateHologramText(hologram, grave);
        holograms.put(grave.getLocation(), hologram);
    }

    public void updateHologramText(ArmorStand hologram, Grave grave) {
        String text = "§6§lTumba de §e" + grave.getOwnerName() + "\n" +
                     "§7Expira en: §c" + grave.getFormattedTimeRemaining();
        hologram.setCustomName(text);
    }

    public void removeHologram(Location location) {
        ArmorStand hologram = holograms.remove(location);
        if (hologram != null && !hologram.isDead()) {
            hologram.remove();
        }
    }

    public ArmorStand getHologram(Location location) {
        return holograms.get(location);
    }

    public void updateAllHolograms(GraveManager graveManager) {
        for (Grave grave : graveManager.getAllGraves()) {
            ArmorStand hologram = holograms.get(grave.getLocation());
            if (hologram != null && !hologram.isDead()) {
                updateHologramText(hologram, grave);
            }
        }
    }

    public void removeAllHolograms() {
        for (ArmorStand hologram : holograms.values()) {
            if (hologram != null && !hologram.isDead()) {
                hologram.remove();
            }
        }
        holograms.clear();
    }
}
