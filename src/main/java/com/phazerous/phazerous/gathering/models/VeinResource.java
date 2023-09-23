package com.phazerous.phazerous.gathering.models;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class VeinResource {
    private final int maxDurability;
    private final ItemStack layerItem;
    private int currentDurability;

    public VeinResource(int maxDurability, ItemStack layerItem) {
        this.maxDurability = maxDurability;
        this.currentDurability = maxDurability;
        this.layerItem = layerItem;
    }

    public void restoreDurability() {
        currentDurability = maxDurability;
    }

    public void setCurrentDurability(int currentDurability) {
        this.currentDurability = currentDurability;

        layerItem.setAmount(currentDurability);
    }
}
