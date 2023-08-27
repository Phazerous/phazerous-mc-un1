package com.phazerous.phazerous.gui;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class CustomInventoryItem {
    private final ItemStack item;
    private final List<Integer> slots;

    public CustomInventoryItem(ItemStack item, List<Integer> slots) {
        this.item = item;
        this.slots = slots;
    }
}
