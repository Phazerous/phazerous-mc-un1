package com.phazerous.phazerous.gui;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class CustomInventoryItem {
    private final ItemStack item;
    private final List<Integer> slots;
    private final String actionId;

    public CustomInventoryItem(ItemStack item, List<Integer> slots, String actionId) {
        this.item = item;
        this.slots = slots;
        this.actionId = actionId;
    }
}
