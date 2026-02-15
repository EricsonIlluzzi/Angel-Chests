package com.angelchests;

import com.angelchests.integrations.AngelChestsExpansion;
import com.angelchests.listeners.ChestInteractionListener;
import com.angelchests.listeners.ChestProtectionListener;
import com.angelchests.listeners.PlayerDeathListener;
import com.angelchests.managers.GraveManager;
import com.angelchests.managers.HologramManager;
import com.angelchests.tasks.GraveExpirationTask;
import com.angelchests.tasks.GraveParticleTask;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AngelChests extends JavaPlugin {
    private GraveManager graveManager;
    private HologramManager hologramManager;
    private GraveExpirationTask expirationTask;
    private GraveParticleTask particleTask;

    @Override
    public void onEnable() {
        // Guardar config por defecto
        saveDefaultConfig();
        
        // Inicializar managers
        graveManager = new GraveManager(this);
        hologramManager = new HologramManager();
        
        // Registrar listeners
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new ChestProtectionListener(this), this);
        getServer().getPluginManager().registerEvents(new ChestInteractionListener(this), this);
        
        // Iniciar tarea de expiración (cada segundo = 20 ticks)
        expirationTask = new GraveExpirationTask(this);
        expirationTask.runTaskTimer(this, 20L, 20L);
        
        // Iniciar tarea de partículas (cada 5 ticks = 4 veces por segundo)
        particleTask = new GraveParticleTask(this);
        particleTask.runTaskTimer(this, 10L, 5L);
        
        // Registrar PlaceholderAPI si está disponible
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new AngelChestsExpansion(this).register();
            getLogger().info("PlaceholderAPI detectado! Placeholders registrados.");
        }
        
        getLogger().info("Angel Chests habilitado correctamente!");
    }

    @Override
    public void onDisable() {
        // Cancelar tareas
        if (expirationTask != null) {
            expirationTask.cancel();
        }
        if (particleTask != null) {
            particleTask.cancel();
        }
        
        // Guardar tumbas
        if (graveManager != null) {
            graveManager.saveGraves();
        }
        
        // Limpiar hologramas
        if (hologramManager != null) {
            hologramManager.removeAllHolograms();
        }
        
        getLogger().info("Angel Chests deshabilitado correctamente!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("angelchests")) {
            return false;
        }
        
        if (!sender.hasPermission("angelchests.admin")) {
            sender.sendMessage("§cNo tienes permiso para usar este comando");
            return true;
        }
        
        if (args.length == 0) {
            sender.sendMessage("§6§lAngel Chests §7v" + getDescription().getVersion());
            sender.sendMessage("§7Uso: /angelchests <reload|list>");
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "reload":
                reloadConfig();
                graveManager = new GraveManager(this);
                sender.sendMessage("§aConfiguración recargada correctamente");
                break;
                
            case "list":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("§cEste comando solo puede ser usado por jugadores");
                    return true;
                }
                
                Player player = (Player) sender;
                var playerGraves = graveManager.getPlayerGraves(player.getUniqueId());
                
                if (playerGraves.isEmpty()) {
                    sender.sendMessage("§7No tienes tumbas activas");
                } else {
                    sender.sendMessage("§6§lTus tumbas activas:");
                    playerGraves.forEach(grave -> {
                        var loc = grave.getLocation();
                        sender.sendMessage(String.format("§7- §e%d, %d, %d §7(Expira en: §c%s§7)",
                            loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
                            grave.getFormattedTimeRemaining()));
                    });
                }
                break;
                
            default:
                sender.sendMessage("§cUso: /angelchests <reload|list>");
        }
        
        return true;
    }

    public GraveManager getGraveManager() {
        return graveManager;
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }
}
