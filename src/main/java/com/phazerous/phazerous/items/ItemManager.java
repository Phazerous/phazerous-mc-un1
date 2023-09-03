package com.phazerous.phazerous.items;

import com.phazerous.phazerous.db.enums.CollectionType;
import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.items.enums.RarityType;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.items.models.CustomItem;
import com.phazerous.phazerous.items.utils.ItemUtils;
import com.phazerous.phazerous.utils.NBTEditor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemManager {

    private final DBManager dbManager;
    private final HashMap<ObjectId, ItemStack> itemsHashmap = new HashMap<>();

    public ItemManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public ItemStack getItemById(ObjectId itemId) {
        if (!itemsHashmap.containsKey(itemId)) {
            Document itemDoc = getItemDoc(itemId);
            CustomItem item = DocumentParser.parseDocument(itemDoc, CustomItem.class);

            String title = item.getTitle();
            Material material = Material.getMaterial(item.getMaterialType());
            Integer additionalMaterialType = item.getAdditionalMaterialType();

            ItemStack itemStack = additionalMaterialType == null ? new ItemStack(material) : new ItemStack(material, 1, additionalMaterialType.shortValue());

            ItemMeta itemMeta = itemStack.getItemMeta();
            setItemDescription(itemMeta, title, item.getRarityType());
            itemStack.setItemMeta(itemMeta);

            ItemStack finalItemStack = ItemUtils.setItemId(itemStack, itemId.toHexString());

            itemsHashmap.put(itemId, finalItemStack);
        }

        return itemsHashmap.get(itemId).clone();
    }

    public String getItemTitle(ObjectId itemId) {
        Document itemDoc = getItemDoc(itemId);
        return itemDoc.getString("title");
    }

    private Document getItemDoc(ObjectId itemId) {
        return dbManager.getDocument(itemId, CollectionType.ITEMS);
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
