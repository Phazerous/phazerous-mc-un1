package com.phazerous.phazerous.gui;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

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
                inventory.setItem(slot, item.getItem());
            }
        }
    }

}
