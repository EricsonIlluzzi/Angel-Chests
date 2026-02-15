package com.angelchests.tasks;

import com.angelchests.AngelChests;
import com.angelchests.models.Grave;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class GraveExpirationTask extends BukkitRunnable {
    private final AngelChests plugin;

    public GraveExpirationTask(AngelChests plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        List<Grave> expiredGraves = new ArrayList<>();
        
        for (Grave grave : plugin.getGraveManager().getAllGraves()) {
            if (grave.isExpired()) {
                expiredGraves.add(grave);
            }
        }

        for (Grave grave : expiredGraves) {
            Location loc = grave.getLocation();
            
            // EFECTOS DE EXPIRACIÓN: Partículas y sonido
            Location effectLoc = loc.clone().add(0.5, 1, 0.5);
            effectLoc.getWorld().spawnParticle(
                org.bukkit.Particle.SMOKE,
                effectLoc,
                50,
                0.3, 0.5, 0.3,
                0.05
            );
            effectLoc.getWorld().spawnParticle(
                org.bukkit.Particle.ASH,
                effectLoc,
                30,
                0.4, 0.6, 0.4,
                0.02
            );
            effectLoc.getWorld().playSound(effectLoc, 
                org.bukkit.Sound.ENTITY_PHANTOM_DEATH, 0.8f, 0.7f);
            
            // Remover bloque físico
            if (loc.getBlock().getType().toString().contains("SKULL") || 
                loc.getBlock().getType().toString().contains("HEAD")) {
                loc.getBlock().setType(org.bukkit.Material.AIR);
            }

            // Remover holograma
            plugin.getHologramManager().removeHologram(loc);

            // Notificar al jugador si está online
            Player owner = Bukkit.getPlayer(grave.getOwnerUUID());
            if (owner != null && owner.isOnline()) {
                String message = plugin.getConfig().getString("messages.grave-expired", 
                    "&cTu tumba ha expirado")
                    .replace("{x}", String.valueOf(loc.getBlockX()))
                    .replace("{y}", String.valueOf(loc.getBlockY()))
                    .replace("{z}", String.valueOf(loc.getBlockZ()))
                    .replace("&", "§");
                owner.sendMessage(message);
            }

            // Remover del manager
            plugin.getGraveManager().removeGrave(loc);
        }

        // Actualizar hologramas cada segundo
        plugin.getHologramManager().updateAllHolograms(plugin.getGraveManager());
    }
}
