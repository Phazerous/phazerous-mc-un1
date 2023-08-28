package com.phazerous.phazerous.listeners;

import com.phazerous.phazerous.gui.CustomInventoryManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryClickListener implements Listener {
    private final CustomInventoryManager customInventoryManager;

    public InventoryClickListener(CustomInventoryManager customInventoryManager) {
        this.customInventoryManager = customInventoryManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();

        if (customInventoryManager.isCustomInventory(inventory)) event.setCancelled(true);
    }
}
