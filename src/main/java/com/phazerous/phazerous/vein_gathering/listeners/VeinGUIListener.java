package com.phazerous.phazerous.vein_gathering.listeners;

import com.phazerous.phazerous.shared.Scheduler;
import com.phazerous.phazerous.vein_gathering.manager.VeinGUIManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class VeinGUIListener implements Listener {
    private final VeinGUIManager veinGUIManager;
    private final Scheduler scheduler;

    public VeinGUIListener(VeinGUIManager veinGUIManager, Scheduler scheduler) {
        this.veinGUIManager = veinGUIManager;
        this.scheduler = scheduler;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();

        if (!veinGUIManager.isVeinInventory(inventory)) return;

        veinGUIManager.handleClick((Player) event.getWhoClicked(), event.getSlot());

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!veinGUIManager.isVeinInventory(event.getInventory())) return;

        if (!veinGUIManager.isAllowedToCloseInventory((Player) event.getPlayer())) {
            scheduler.runTaskLater(() -> event
                    .getPlayer()
                    .openInventory(event.getInventory()), 1);
            return;
        }

        veinGUIManager.handleInventoryClose((Player) event.getPlayer());
    }
}
