package com.angelchests.integrations;

import com.angelchests.AngelChests;
import com.angelchests.models.Grave;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AngelChestsExpansion extends PlaceholderExpansion {
    private final AngelChests plugin;

    public AngelChestsExpansion(AngelChests plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "angelchests";
    }

    @Override
    @NotNull
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    @NotNull
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // Mantener registrado incluso si PlaceholderAPI se recarga
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        // %angelchests_graves_active%
        if (params.equals("graves_active")) {
            List<Grave> playerGraves = plugin.getGraveManager().getPlayerGraves(player.getUniqueId());
            return String.valueOf(playerGraves.size());
        }

        // %angelchests_graves_total%
        if (params.equals("graves_total")) {
            return String.valueOf(plugin.getGraveManager().getAllGraves().size());
        }

        // %angelchests_has_graves%
        if (params.equals("has_graves")) {
            List<Grave> playerGraves = plugin.getGraveManager().getPlayerGraves(player.getUniqueId());
            return playerGraves.isEmpty() ? "No" : "Sí";
        }

        // %angelchests_nearest_grave_distance%
        if (params.equals("nearest_grave_distance")) {
            if (!player.isOnline()) {
                return "N/A";
            }
            
            List<Grave> playerGraves = plugin.getGraveManager().getPlayerGraves(player.getUniqueId());
            if (playerGraves.isEmpty()) {
                return "N/A";
            }

            Location playerLoc = player.getPlayer().getLocation();
            double minDistance = Double.MAX_VALUE;

            for (Grave grave : playerGraves) {
                if (grave.getLocation().getWorld().equals(playerLoc.getWorld())) {
                    double distance = grave.getLocation().distance(playerLoc);
                    if (distance < minDistance) {
                        minDistance = distance;
                    }
                }
            }

            return minDistance == Double.MAX_VALUE ? "N/A" : String.format("%.1f", minDistance);
        }

        // %angelchests_nearest_grave_coords%
        if (params.equals("nearest_grave_coords")) {
            if (!player.isOnline()) {
                return "N/A";
            }
            
            List<Grave> playerGraves = plugin.getGraveManager().getPlayerGraves(player.getUniqueId());
            if (playerGraves.isEmpty()) {
                return "N/A";
            }

            Location playerLoc = player.getPlayer().getLocation();
            double minDistance = Double.MAX_VALUE;
            Grave nearestGrave = null;

            for (Grave grave : playerGraves) {
                if (grave.getLocation().getWorld().equals(playerLoc.getWorld())) {
                    double distance = grave.getLocation().distance(playerLoc);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestGrave = grave;
                    }
                }
            }

            if (nearestGrave == null) {
                return "N/A";
            }

            Location loc = nearestGrave.getLocation();
            return String.format("%d, %d, %d", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        }

        // %angelchests_nearest_grave_time%
        if (params.equals("nearest_grave_time")) {
            if (!player.isOnline()) {
                return "N/A";
            }
            
            List<Grave> playerGraves = plugin.getGraveManager().getPlayerGraves(player.getUniqueId());
            if (playerGraves.isEmpty()) {
                return "N/A";
            }

            Location playerLoc = player.getPlayer().getLocation();
            double minDistance = Double.MAX_VALUE;
            Grave nearestGrave = null;

            for (Grave grave : playerGraves) {
                if (grave.getLocation().getWorld().equals(playerLoc.getWorld())) {
                    double distance = grave.getLocation().distance(playerLoc);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestGrave = grave;
                    }
                }
            }

            return nearestGrave == null ? "N/A" : nearestGrave.getFormattedTimeRemaining();
        }

        // %angelchests_oldest_grave_time%
        if (params.equals("oldest_grave_time")) {
            List<Grave> playerGraves = plugin.getGraveManager().getPlayerGraves(player.getUniqueId());
            if (playerGraves.isEmpty()) {
                return "N/A";
            }

            Grave oldestGrave = playerGraves.stream()
                .min((g1, g2) -> Long.compare(g1.getRemainingSeconds(), g2.getRemainingSeconds()))
                .orElse(null);

            return oldestGrave == null ? "N/A" : oldestGrave.getFormattedTimeRemaining();
        }

        // %angelchests_total_items%
        if (params.equals("total_items")) {
            List<Grave> playerGraves = plugin.getGraveManager().getPlayerGraves(player.getUniqueId());
            int totalItems = 0;
            
            for (Grave grave : playerGraves) {
                totalItems += grave.getItems().stream()
                    .filter(item -> item != null && item.getType() != org.bukkit.Material.AIR)
                    .count();
            }
            
            return String.valueOf(totalItems);
        }

        // %angelchests_total_experience%
        if (params.equals("total_experience")) {
            List<Grave> playerGraves = plugin.getGraveManager().getPlayerGraves(player.getUniqueId());
            int totalExp = 0;
            
            for (Grave grave : playerGraves) {
                totalExp += grave.getExperience();
            }
            
            return String.valueOf(totalExp);
        }

        // %angelchests_grave_<number>_coords%
        if (params.startsWith("grave_") && params.endsWith("_coords")) {
            try {
                String numStr = params.replace("grave_", "").replace("_coords", "");
                int index = Integer.parseInt(numStr) - 1; // 1-indexed para usuarios
                
                List<Grave> playerGraves = plugin.getGraveManager().getPlayerGraves(player.getUniqueId());
                if (index >= 0 && index < playerGraves.size()) {
                    Location loc = playerGraves.get(index).getLocation();
                    return String.format("%d, %d, %d", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
                }
            } catch (NumberFormatException ignored) {
            }
            return "N/A";
        }

        // %angelchests_grave_<number>_time%
        if (params.startsWith("grave_") && params.endsWith("_time")) {
            try {
                String numStr = params.replace("grave_", "").replace("_time", "");
                int index = Integer.parseInt(numStr) - 1;
                
                List<Grave> playerGraves = plugin.getGraveManager().getPlayerGraves(player.getUniqueId());
                if (index >= 0 && index < playerGraves.size()) {
                    return playerGraves.get(index).getFormattedTimeRemaining();
                }
            } catch (NumberFormatException ignored) {
            }
            return "N/A";
        }

        return null; // Placeholder no reconocido
    }
}
