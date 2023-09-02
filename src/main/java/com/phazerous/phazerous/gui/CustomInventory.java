package com.phazerous.phazerous.gui;

import com.phazerous.phazerous.utils.NBTEditor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
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
                ItemStack preparedItem = prepareItem(item.getItem(), item.getActionId(), item.getPrice());
                inventory.setItem(slot, preparedItem);
            }
        }
    }

    private ItemStack prepareItem(ItemStack item, String actionId, Double price) {
        if (actionId == null) return item;

        if (price != null) setItemPrice(item, price);

        return NBTEditor.setString(item, GUISharedConstants.ACTION_ID_NAME, actionId);
    }

    private void setItemPrice(ItemStack item, Double price) {
        if (price == null) return;

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

        String priceTemplate = "§r§6Price: %.2f";
        String priceString = String.format(priceTemplate, price);
        String mcString = ChatColor.translateAlternateColorCodes('§', priceString);

        lore.add(null); // Add an empty line
        lore.add(mcString);

        meta.setLore(lore);

        item.setItemMeta(meta);
    }
}
