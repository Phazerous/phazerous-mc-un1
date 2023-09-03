package com.phazerous.phazerous.items.utils;

import com.phazerous.phazerous.items.enums.ItemType;
import com.phazerous.phazerous.utils.NBTEditor;
import org.bson.types.ObjectId;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {

    /**
     * Sets the id to corresponding item in the database.
     */
    public static ItemStack setItemId(ItemStack item, String itemId) {
        return NBTEditor.setString(item, "itemId", itemId);
    }

    public static ObjectId getItemId(ItemStack item) {
        if (!NBTEditor.hasKey(item, "itemId")) return null;

        return new ObjectId(NBTEditor.getString(item, "itemId"));
    }

    public static ItemType getItemType(ItemStack item) {
        if (!NBTEditor.hasKey(item, "itemType")) return null;

        Integer itemTypeCode = NBTEditor.getInteger(item, "itemType");

        return ItemType.getItemTypeByValue(itemTypeCode);
    }

    public static ItemStack setItemType(ItemStack item, ItemType itemType) {
        return NBTEditor.setInteger(item, "itemType", itemType.getItemType());
    }

    public static boolean hasItemSpeed(ItemStack item) {
        return NBTEditor.hasKey(item, "speed");
    }

    public static Long getItemSpeed(ItemStack item) {
        if (!NBTEditor.hasKey(item, "speed")) return null;

        return NBTEditor.getLong(item, "speed");
    }

    public static ItemStack setItemSpeed(ItemStack item, Long speed) {
        return NBTEditor.setLong(item, "speed", speed);
    }
}
