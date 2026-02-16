package com.angelchests.managers;

import com.angelchests.AngelChests;
import com.angelchests.models.Grave;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GraveManager {
    private final AngelChests plugin;
    private final Map<Location, Grave> graves;
    private final File dataFile;

    public GraveManager(AngelChests plugin) {
        this.plugin = plugin;
        this.graves = new ConcurrentHashMap<>();
        this.dataFile = new File(plugin.getDataFolder(), "data.yml");
        loadGraves();
    }

    public void createGrave(UUID ownerUUID, String ownerName, Location location, 
                           List<ItemStack> items, int experience) {
        long expirationTime = plugin.getConfig().getLong("expiration-time", 900);
        // Normalizar la ubicación a coordenadas de bloque
        Location normalized = location.getBlock().getLocation();
        Grave grave = new Grave(ownerUUID, ownerName, normalized, items, experience, expirationTime);
        graves.put(normalized, grave);
        saveGraves();
    }

    public Grave getGrave(Location location) {
        // Normalizar la ubicación a coordenadas de bloque para evitar problemas con decimales
        Location normalized = location.getBlock().getLocation();
        return graves.get(normalized);
    }

    public void removeGrave(Location location) {
        // Normalizar la ubicación a coordenadas de bloque
        Location normalized = location.getBlock().getLocation();
        graves.remove(normalized);
        saveGraves();
    }

    public Collection<Grave> getAllGraves() {
        return new ArrayList<>(graves.values());
    }

    public List<Grave> getPlayerGraves(UUID playerUUID) {
        List<Grave> playerGraves = new ArrayList<>();
        for (Grave grave : graves.values()) {
            if (grave.getOwnerUUID().equals(playerUUID)) {
                playerGraves.add(grave);
            }
        }
        return playerGraves;
    }

    public void saveGraves() {
        FileConfiguration data = new YamlConfiguration();
        int index = 0;
        
        for (Grave grave : graves.values()) {
            String path = "graves." + index;
            data.set(path + ".owner-uuid", grave.getOwnerUUID().toString());
            data.set(path + ".owner-name", grave.getOwnerName());
            data.set(path + ".location.world", grave.getLocation().getWorld().getName());
            data.set(path + ".location.x", grave.getLocation().getX());
            data.set(path + ".location.y", grave.getLocation().getY());
            data.set(path + ".location.z", grave.getLocation().getZ());
            data.set(path + ".experience", grave.getExperience());
            data.set(path + ".creation-time", grave.getCreationTime());
            data.set(path + ".expiration-time", grave.getExpirationTime());
            
            List<ItemStack> nonNullItems = new ArrayList<>();
            for (ItemStack item : grave.getItems()) {
                if (item != null && item.getType() != Material.AIR) {
                    nonNullItems.add(item);
                }
            }
            data.set(path + ".items", nonNullItems);
            index++;
        }

        try {
            data.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Error al guardar tumbas: " + e.getMessage());
        }
    }

    private void loadGraves() {
        if (!dataFile.exists()) {
            return;
        }

        FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
        
        if (!data.contains("graves")) {
            return;
        }

        for (String key : data.getConfigurationSection("graves").getKeys(false)) {
            String path = "graves." + key;
            // Implementación de carga completa en el método siguiente
        }
    }

    public void clearExpiredGraves() {
        graves.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
}
