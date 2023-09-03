package com.phazerous.phazerous.gui.models;

import com.phazerous.phazerous.gui.actions.models.AbstractGUIAction;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class CustomInventoryItem {
    private final ItemStack item;
    private final List<Integer> slots;
    private AbstractGUIAction action;

    public CustomInventoryItem(ItemStack item, List<Integer> slots, AbstractGUIAction action) {
        this.item = item;
        this.slots = slots;
        this.action = action;
    }
}
