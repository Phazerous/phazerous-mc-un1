package com.phazerous.phazerous;

import com.phazerous.phazerous.dtos.ItemDto;
import org.bson.types.ObjectId;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ItemManager {

    private final DBManager dbManager;
    private final HashMap<ObjectId, ItemStack> itemsHashmap = new HashMap<>();

    public ItemManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public ItemStack getItemById(ObjectId itemId) {
        if (!itemsHashmap.containsKey(itemId)) {
            ItemDto itemDto = dbManager.getItemDtoById(itemId);

            String title = itemDto.getTitle();
            Material material = Material.getMaterial(itemDto.getMaterialType());

            ItemStack itemStack = new ItemStack(material, 1);

            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(title);
            itemStack.setItemMeta(itemMeta);

            itemsHashmap.put(itemId, itemStack);
        }

        return itemsHashmap.get(itemId).clone();
    }

    public List<ItemStack> getItemsByIds(List<ObjectId> itemIds) {
        return itemIds.stream()
                .map(this::getItemById)
                .collect(Collectors.toList());
    }
}
