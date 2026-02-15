package com.angelchests.tasks;

import com.angelchests.AngelChests;
import com.angelchests.models.Grave;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

public class GraveParticleTask extends BukkitRunnable {
    private final AngelChests plugin;
    private double angle = 0;

    public GraveParticleTask(AngelChests plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Grave grave : plugin.getGraveManager().getAllGraves()) {
            Location loc = grave.getLocation().clone().add(0.5, 0, 0.5);
            
            // Efecto de beam vertical (como beacon)
            for (int i = 0; i < 3; i++) {
                Location particleLoc = loc.clone().add(0, i * 0.5, 0);
                particleLoc.getWorld().spawnParticle(
                    Particle.END_ROD, 
                    particleLoc, 
                    1, 
                    0.1, 0.1, 0.1, 
                    0.01
                );
            }
            
            // Efecto de espiral alrededor de la tumba
            double radius = 0.8;
            for (int i = 0; i < 2; i++) {
                double x = radius * Math.cos(angle + (i * Math.PI));
                double z = radius * Math.sin(angle + (i * Math.PI));
                Location spiralLoc = loc.clone().add(x, 0.5, z);
                
                spiralLoc.getWorld().spawnParticle(
                    Particle.SOUL_FIRE_FLAME,
                    spiralLoc,
                    1,
                    0, 0, 0,
                    0.01
                );
            }
            
            // Partículas flotantes aleatorias
            if (Math.random() < 0.3) {
                Location randomLoc = loc.clone().add(
                    (Math.random() - 0.5) * 1.5,
                    Math.random() * 2,
                    (Math.random() - 0.5) * 1.5
                );
                randomLoc.getWorld().spawnParticle(
                    Particle.SOUL,
                    randomLoc,
                    1,
                    0, 0.1, 0,
                    0.02
                );
            }
        }
        
        // Incrementar ángulo para la rotación
        angle += 0.15;
        if (angle > Math.PI * 2) {
            angle = 0;
        }
    }
}
