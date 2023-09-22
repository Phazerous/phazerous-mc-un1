package com.phazerous.phazerous.gathering.listeners;

import com.phazerous.phazerous.gathering.manager.GatheringManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class InventoryClickListener implements Listener {
    private final Set<String> availableInventoryTitles = new HashSet<>(Arrays.asList("mining", "chopping", "gathering"));

    private final GatheringManager gatheringManager;

    public InventoryClickListener(GatheringManager gatheringManager) {
        this.gatheringManager = gatheringManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();

        if (!isGatheringInventory(inventory)) return;

//        gatheringManager.handleClick((Player) event.getWhoClicked(), event.getCurrentItem());

        event.setCancelled(true);
    }

    private boolean isGatheringInventory(Inventory inventory) {
        return availableInventoryTitles.contains(inventory.getTitle().toLowerCase());
    }
}
