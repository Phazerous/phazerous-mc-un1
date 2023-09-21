package com.phazerous.phazerous.gui.listeners;

import com.phazerous.phazerous.gui.managers.GUIManager;
import com.phazerous.phazerous.gui.actions.GUIActionManager;
import org.bson.types.ObjectId;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {
    private final GUIManager GUIManager;
    private final GUIActionManager GUIActionManager;

    public InventoryClickListener(GUIManager GUIManager, GUIActionManager GUIActionManager) {
        this.GUIManager = GUIManager;
        this.GUIActionManager = GUIActionManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        
        if (GUIManager.isCustomInventory(inventory)) event.setCancelled(true);

        ItemStack item = event.getCurrentItem();

        if (item == null || item.getType() == Material.AIR) return;

        Player player = (Player) event.getWhoClicked();

        if (GUIActionManager.hasAction(item)) {
            ObjectId actionId = GUIActionManager.getActionId(item);
            GUIActionManager.executeAction(actionId, player);
        }
    }
}
