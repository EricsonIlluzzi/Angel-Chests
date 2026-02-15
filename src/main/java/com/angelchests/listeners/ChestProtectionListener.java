package com.angelchests.listeners;

import com.angelchests.AngelChests;
import com.angelchests.models.Grave;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ChestProtectionListener implements Listener {
    private final AngelChests plugin;

    public ChestProtectionListener(AngelChests plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Grave grave = plugin.getGraveManager().getGrave(block.getLocation());
        
        if (grave == null) {
            return;
        }
        
        Player player = event.getPlayer();
        
        // Solo el dueño o admins pueden romper
        if (!grave.getOwnerUUID().equals(player.getUniqueId()) && 
            !player.hasPermission("angelchests.bypass")) {
            event.setCancelled(true);
            String message = plugin.getConfig().getString("messages.no-permission",
                "&cNo tienes permiso para romper esta tumba")
                .replace("&", "§");
            player.sendMessage(message);
            return;
        }
        
        // Si el dueño o admin rompe la tumba, limpiar todo
        event.setDropItems(false); // No dropear la cabeza
        
        // Remover holograma
        plugin.getHologramManager().removeHologram(grave.getLocation());
        
        // Remover del manager
        plugin.getGraveManager().removeGrave(grave.getLocation());
        
        // Notificar
        player.sendMessage("§cTumba destruida. Los items se han perdido.");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!plugin.getConfig().getBoolean("explosion-protection", true)) {
            return;
        }
        
        event.blockList().removeIf(block -> 
            plugin.getGraveManager().getGrave(block.getLocation()) != null
        );
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockExplode(BlockExplodeEvent event) {
        if (!plugin.getConfig().getBoolean("explosion-protection", true)) {
            return;
        }
        
        event.blockList().removeIf(block -> 
            plugin.getGraveManager().getGrave(block.getLocation()) != null
        );
    }
}
