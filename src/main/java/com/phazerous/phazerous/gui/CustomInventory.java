package com.phazerous.phazerous.gui;

import com.phazerous.phazerous.utils.NBTEditor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class CustomInventory {
    private final Inventory inventory;

    public CustomInventory(int size, String title) {
        this.inventory = Bukkit.createInventory(null, size, title);
    }

    public void setContents(List<CustomInventoryItem> contents) {
        for (CustomInventoryItem item : contents) {
            for (Integer slot : item.getSlots()) {
                ItemStack preparedItem = prepareItem(item.getItem(), item.getActionId());
                inventory.setItem(slot, preparedItem);
            }
        }
    }

    private ItemStack prepareItem(ItemStack item, String actionId) {
        if (actionId == null) return item;

        return NBTEditor.setString(item, GUISharedConstants.ACTION_ID_NAME, actionId);
    }

}
