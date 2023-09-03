package com.phazerous.phazerous.utils;

import com.phazerous.phazerous.items.utils.ItemUtils;
import org.bson.types.ObjectId;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {
    public static int countItemsInInventory(Player player, ObjectId requestedItemId) {
        int amount = 0;

        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR) continue;

            ObjectId itemId = ItemUtils.getItemId(item);

            if (requestedItemId.equals(itemId)) {
                amount += item.getAmount();
            }
        }

        return amount;
    }

    public static boolean withdrawItemsFromInventory(Player player, ObjectId requestedItemId, int amount) {
        Inventory inventory = player.getInventory();
        int amountLeft = amount;

        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR) continue;

            ObjectId itemId = ItemUtils.getItemId(item);

            if (requestedItemId.equals(itemId)) {
                int itemAmount = item.getAmount();

                if (itemAmount > amountLeft) {
                    item.setAmount(itemAmount - amountLeft);
                    return true;
                } else if (itemAmount < amountLeft) {
                    amountLeft -= itemAmount;
                    inventory.remove(item);
                } else {
                    inventory.remove(item);
                    return true;
                }
            }
        }

        return false;
    }
}
