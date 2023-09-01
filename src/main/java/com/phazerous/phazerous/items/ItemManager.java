package com.phazerous.phazerous.items;

import com.phazerous.phazerous.items.models.ItemDto;
import com.phazerous.phazerous.enums.RarityType;
import com.phazerous.phazerous.db.DBManager;
import org.bson.types.ObjectId;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
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
            byte additionalMaterialType = itemDto.getAdditionalMaterialType();

            ItemStack itemStack = additionalMaterialType == 0 ? new ItemStack(material) : new ItemStack(material, 1, additionalMaterialType);

            ItemMeta itemMeta = itemStack.getItemMeta();
            setItemDescription(itemMeta, title, itemDto.getRarityType());
            itemStack.setItemMeta(itemMeta);

            itemsHashmap.put(itemId, itemStack);
        }

        return itemsHashmap
                .get(itemId)
                .clone();
    }

    public List<ItemStack> getItemsByIds(List<ObjectId> itemIds) {
        return itemIds
                .stream()
                .map(this::getItemById)
                .collect(Collectors.toList());
    }

    private void setItemDescription(ItemMeta itemMeta, String title, RarityType rarityType) {
        String color = rarityType.getColor();

        String formattedTitle = ChatColor.translateAlternateColorCodes('§', "§r" + color + title);
        String formattedRarity = ChatColor.translateAlternateColorCodes('§', "§r§l" + color + rarityType.getTitle());

        itemMeta.setDisplayName(formattedTitle);

        List<String> itemLore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
        itemLore.add(formattedRarity);

        itemMeta.setLore(itemLore);
    }
}