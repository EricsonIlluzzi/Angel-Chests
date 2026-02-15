package com.angelchests.listeners;

import com.angelchests.AngelChests;
import com.angelchests.models.Grave;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ChestInteractionListener implements Listener {
    private final AngelChests plugin;
    private final Map<Player, Grave> openGraves;

    public ChestInteractionListener(AngelChests plugin) {
        this.plugin = plugin;
        this.openGraves = new HashMap<>();
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
            // Debug: verificar si hay tumbas registradas
            plugin.getLogger().info("Clic en cabeza pero no hay tumba registrada en: " + 
                block.getLocation().getBlockX() + ", " + 
                block.getLocation().getBlockY() + ", " + 
                block.getLocation().getBlockZ());
            plugin.getLogger().info("Tumbas activas: " + plugin.getGraveManager().getAllGraves().size());
            return;
        }
        
        plugin.getLogger().info("Tumba encontrada! Dueño: " + grave.getOwnerName());
        
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
        if (isInventoryFull(player)) {
            String message = plugin.getConfig().getString("messages.grave-full-inventory",
                "&cTu inventario está lleno")
                .replace("&", "§");
            player.sendMessage(message);
            return;
        }
        
        // Abrir GUI personalizada
        openGraveGUI(player, grave);
        
        // EFECTOS AL ABRIR: Sonido y partículas
        block.getLocation().getWorld().playSound(block.getLocation(), 
            org.bukkit.Sound.BLOCK_CHEST_OPEN, 1.0f, 0.7f);
        block.getLocation().getWorld().playSound(block.getLocation(), 
            org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1.2f);
        
        Location effectLoc = block.getLocation().clone().add(0.5, 1, 0.5);
        effectLoc.getWorld().spawnParticle(
            org.bukkit.Particle.SOUL_FIRE_FLAME,
            effectLoc,
            20,
            0.3, 0.5, 0.3,
            0.05
        );
        effectLoc.getWorld().spawnParticle(
            org.bukkit.Particle.END_ROD,
            effectLoc,
            10,
            0.2, 0.3, 0.2,
            0.1
        );
    }

    private void openGraveGUI(Player player, Grave grave) {
        int size = ((grave.getItems().size() + 8) / 9) * 9;
        size = Math.min(54, Math.max(9, size));
        
        Inventory gui = Bukkit.createInventory(null, size, 
            "§6§lTumba de " + grave.getOwnerName());
        
        for (ItemStack item : grave.getItems()) {
            if (item != null && item.getType() != Material.AIR) {
                gui.addItem(item);
            }
        }
        
        openGraves.put(player, grave);
        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        if (!openGraves.containsKey(player)) {
            return;
        }
        
        // Permitir tomar items pero no poner
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getPlayer();
        Grave grave = openGraves.remove(player);
        
        if (grave == null) {
            return;
        }
        
        // Verificar si quedan items
        boolean hasItems = false;
        for (ItemStack item : event.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                hasItems = true;
                break;
            }
        }
        
        if (!hasItems) {
            // Dar experiencia
            player.giveExp(grave.getExperience());
            
            // EFECTOS AL RECUPERAR: Explosión de partículas
            Location effectLoc = grave.getLocation().clone().add(0.5, 1, 0.5);
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
            
            // Remover tumba física
            grave.getLocation().getBlock().setType(Material.AIR);
            
            // Remover holograma
            plugin.getHologramManager().removeHologram(grave.getLocation());
            
            // Remover del manager
            plugin.getGraveManager().removeGrave(grave.getLocation());
            
            // Notificar
            String message = plugin.getConfig().getString("messages.grave-recovered",
                "&aHas recuperado tus items")
                .replace("&", "§");
            player.sendMessage(message);
        }
    }

    private boolean isInventoryFull(Player player) {
        return player.getInventory().firstEmpty() == -1;
    }
}
