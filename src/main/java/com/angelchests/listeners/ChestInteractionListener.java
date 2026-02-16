package com.angelchests.listeners;

import com.angelchests.AngelChests;
import com.angelchests.models.Grave;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ChestInteractionListener implements Listener {
    private final AngelChests plugin;

    public ChestInteractionListener(AngelChests plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
        
        // Verificar si es una cabeza de jugador
        Material type = block.getType();
        if (type != Material.PLAYER_HEAD && type != Material.PLAYER_WALL_HEAD) {
            return;
        }
        
        Grave grave = plugin.getGraveManager().getGrave(block.getLocation());
        if (grave == null) {
            return;
        }
        
        event.setCancelled(true);
        Player player = event.getPlayer();
        
        // Verificar permisos
        if (!grave.getOwnerUUID().equals(player.getUniqueId()) && 
            !player.hasPermission("angelchests.bypass")) {
            String message = plugin.getConfig().getString("messages.no-permission",
                "&cNo tienes permiso para abrir esta tumba")
                .replace("&", "§");
            player.sendMessage(message);
            return;
        }
        
        // Verificar espacio en inventario
        int emptySlots = 0;
        for (ItemStack item : player.getInventory().getStorageContents()) {
            if (item == null || item.getType() == Material.AIR) {
                emptySlots++;
            }
        }
        
        int itemsNeeded = 0;
        for (ItemStack item : grave.getItems()) {
            if (item != null && item.getType() != Material.AIR) {
                itemsNeeded++;
            }
        }
        
        if (emptySlots < itemsNeeded) {
            String message = plugin.getConfig().getString("messages.grave-full-inventory",
                "&cTu inventario está lleno. Necesitas al menos " + itemsNeeded + " espacios libres")
                .replace("&", "§");
            player.sendMessage(message);
            return;
        }
        
        // Dar todos los items al jugador
        for (ItemStack item : grave.getItems()) {
            if (item != null && item.getType() != Material.AIR) {
                player.getInventory().addItem(item.clone());
            }
        }
        
        // Dar experiencia
        player.giveExp(grave.getExperience());
        
        // EFECTOS AL RECUPERAR: Explosión de partículas
        Location effectLoc = block.getLocation().clone().add(0.5, 1, 0.5);
        effectLoc.getWorld().spawnParticle(
            org.bukkit.Particle.TOTEM_OF_UNDYING,
            effectLoc,
            30,
            0.5, 0.5, 0.5,
            0.1
        );
        effectLoc.getWorld().spawnParticle(
            org.bukkit.Particle.END_ROD,
            effectLoc,
            20,
            0.3, 0.5, 0.3,
            0.15
        );
        
        // Sonidos épicos
        effectLoc.getWorld().playSound(effectLoc, 
            org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
        effectLoc.getWorld().playSound(effectLoc, 
            org.bukkit.Sound.BLOCK_BEACON_DEACTIVATE, 0.8f, 1.2f);
        effectLoc.getWorld().playSound(effectLoc, 
            org.bukkit.Sound.BLOCK_CHEST_OPEN, 1.0f, 0.7f);
        
        // Remover tumba física
        block.setType(Material.AIR);
        
        // Remover holograma
        plugin.getHologramManager().removeHologram(grave.getLocation());
        
        // Remover del manager
        plugin.getGraveManager().removeGrave(grave.getLocation());
        
        // Notificar
        String message = plugin.getConfig().getString("messages.grave-recovered",
            "&aHas recuperado tus items de la tumba")
            .replace("&", "§");
        player.sendMessage(message);
    }
}
