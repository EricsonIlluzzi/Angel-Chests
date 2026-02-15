package com.angelchests.listeners;

import com.angelchests.AngelChests;
import com.angelchests.models.Grave;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerDeathListener implements Listener {
    private final AngelChests plugin;

    public PlayerDeathListener(AngelChests plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        
        // Capturar items
        List<ItemStack> items = new ArrayList<>(event.getDrops());
        
        // Capturar experiencia
        double expPercentage = plugin.getConfig().getDouble("experience-percentage", 1.0);
        int experience = (int) (player.getTotalExperience() * expPercentage);
        
        // Limpiar drops para evitar duplicación
        event.getDrops().clear();
        event.setKeepLevel(true);
        event.setDroppedExp(0);
        
        // Validar ubicación segura
        Location deathLocation = player.getLocation();
        Location safeLocation = findSafeLocation(deathLocation);
        
        // Crear tumba física
        Block graveBlock = safeLocation.getBlock();
        graveBlock.setType(Material.PLAYER_HEAD);
        
        if (graveBlock.getState() instanceof Skull) {
            Skull skull = (Skull) graveBlock.getState();
            skull.setOwningPlayer(player);
            skull.update();
        }
        
        // Usar la ubicación exacta del bloque para evitar problemas de precisión
        Location blockLocation = graveBlock.getLocation();
        
        // EFECTOS VISUALES: Rayo cosmético (sin daño)
        blockLocation.getWorld().strikeLightningEffect(blockLocation);
        
        // EFECTOS DE SONIDO: Sonido épico al crear tumba
        blockLocation.getWorld().playSound(blockLocation, 
            org.bukkit.Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
        blockLocation.getWorld().playSound(blockLocation, 
            org.bukkit.Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 0.8f);
        
        // Registrar tumba en el manager
        plugin.getGraveManager().createGrave(
            player.getUniqueId(),
            player.getName(),
            blockLocation,
            items,
            experience
        );
        
        // Crear holograma
        Grave grave = plugin.getGraveManager().getGrave(blockLocation);
        if (grave != null) {
            plugin.getHologramManager().createHologram(grave);
        } else {
            plugin.getLogger().warning("No se pudo crear holograma para tumba de " + player.getName());
        }
        
        // Notificar al jugador
        String message = plugin.getConfig().getString("messages.grave-created",
            "&7Tu tumba ha sido creada")
            .replace("{x}", String.valueOf(blockLocation.getBlockX()))
            .replace("{y}", String.valueOf(blockLocation.getBlockY()))
            .replace("{z}", String.valueOf(blockLocation.getBlockZ()))
            .replace("&", "§");
        player.sendMessage(message);
    }

    private Location findSafeLocation(Location original) {
        int minHeight = plugin.getConfig().getInt("min-spawn-height", -60);
        
        // Si está en el vacío, buscar superficie
        if (original.getY() < minHeight) {
            Location surface = original.getWorld().getHighestBlockAt(original).getLocation().add(0, 1, 0);
            return surface;
        }
        
        // Si está en lava o agua, buscar bloque de aire cercano
        Block block = original.getBlock();
        if (block.getType() == Material.LAVA || block.getType() == Material.WATER) {
            for (int y = 0; y <= 3; y++) {
                Location testLoc = original.clone().add(0, y, 0);
                if (testLoc.getBlock().getType() == Material.AIR) {
                    return testLoc;
                }
            }
        }
        
        return original;
    }
}
