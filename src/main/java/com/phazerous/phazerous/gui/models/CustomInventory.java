package com.phazerous.phazerous.gui.models;

import com.phazerous.phazerous.gui.GUISharedConstants;
import com.phazerous.phazerous.gui.actions.models.AbstractGUIAction;
import com.phazerous.phazerous.gui.actions.models.PurchaseItemWithItemAction;
import com.phazerous.phazerous.gui.actions.models.PurchaseItemWithMoneyAction;
import com.phazerous.phazerous.utils.NBTEditor;
import lombok.Getter;
import org.bson.types.ObjectId;
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
                ItemStack preparedItem = prepareItem(item.getItem(), item.getAction());
                inventory.setItem(slot, preparedItem);
            }
        }
    }

    private ItemStack prepareItem(ItemStack item, AbstractGUIAction action) {
        if (action == null) return item;

        if (action instanceof PurchaseItemWithMoneyAction) {
            PurchaseItemWithMoneyAction purchaseItemWithMoneyAction = (PurchaseItemWithMoneyAction) action;
            Double price = purchaseItemWithMoneyAction.getPrice();

            setPrice(item, price);
        } else if (action instanceof PurchaseItemWithItemAction) {
            PurchaseItemWithItemAction purchaseItemWithItemAction = (PurchaseItemWithItemAction) action;
            String requestedItemTitle = purchaseItemWithItemAction.getRequestedItemName();
            Integer amount = purchaseItemWithItemAction.getAmount();

            setRequiredItems(item, requestedItemTitle, amount);
        }

        return NBTEditor.setString(item, GUISharedConstants.ACTION_ID_NAME, action.get_id().toHexString());
    }

    private void setPrice(ItemStack item, Double price) {
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

    private void setRequiredItems(ItemStack item, String requestedItemTitle, Integer amount) {
        ItemMeta meta = item.getItemMeta();

        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

        String requiredItemsTemplate = "§r§5Required Items:";
        String requiredItemString = String.format("§r§5x%d %s", amount, requestedItemTitle);

        lore.add(null); // Add an empty line
        lore.add(requiredItemsTemplate);
        lore.add(requiredItemString);

        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}
