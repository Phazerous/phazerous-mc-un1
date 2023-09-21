package com.phazerous.phazerous.utils;

import com.phazerous.phazerous.items.utils.ItemUtils;
import org.bson.types.ObjectId;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

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

    public static int[] getItemsSlotsByPattern(List<String> pattern) {
        return getItemsSlotsByPattern(pattern, 0);
    }

    public static int[] getItemsSlotsByPattern(String pattern) {
        return getItemsSlotsByPattern(new ArrayList<String>() {{
            add(pattern);
        }}, 0);
    }

    public static int[] getItemsSlotsByPattern(String pattern, int startRow) {
        return getItemsSlotsByPattern(new ArrayList<String>() {{
            add(pattern);
        }}, startRow);
    }

    /**
     * Returns the position of the items in the inventory that match the string pattern.
     *
     * @param pattern
     * @param startRow The row where the pattern starts. (starts at 0)
     * @return
     */
    public static int[] getItemsSlotsByPattern(List<String> pattern, int startRow) {
        List<Integer> positionsList = new ArrayList<>();

        for (int i = 0; i < pattern.size(); i++) {
            String row = pattern.get(i);

            if (row.length() != 9)
                throw new IllegalArgumentException("The pattern must have 9 characters per row.");

            for (int j = 0; j < row.length(); j++) {
                char character = row.charAt(j);

                if (character == 'x') positionsList.add((i + startRow) * 9 + j);
            }
        }

        return ArrayUtils.getIntArrayFromList(positionsList);
    }
}
