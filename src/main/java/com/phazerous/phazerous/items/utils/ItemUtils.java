package com.phazerous.phazerous.items.utils;

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
        if (!NBTEditor.hasString(item, "itemId")) return null;
        
        return new ObjectId(NBTEditor.getString(item, "itemId"));
    }
}
