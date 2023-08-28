package com.phazerous.phazerous.listeners;

import com.phazerous.phazerous.gui.CustomInventoryManager;
import com.phazerous.phazerous.gui.actions.CustomInventoryActionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {
    private final CustomInventoryManager customInventoryManager;
    private final CustomInventoryActionManager customInventoryActionManager;

    public InventoryClickListener(CustomInventoryManager customInventoryManager, CustomInventoryActionManager customInventoryActionManager) {
        this.customInventoryManager = customInventoryManager;
        this.customInventoryActionManager = customInventoryActionManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();

        if (customInventoryManager.isCustomInventory(inventory)) event.setCancelled(true);

        ItemStack item = event.getCurrentItem();

        if (item == null) return;

        Player player = (Player) event.getWhoClicked();

        if (customInventoryActionManager.hasAction(item)) customInventoryActionManager.executeAction(item, player);
    }
}
