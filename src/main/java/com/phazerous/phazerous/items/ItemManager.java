package com.phazerous.phazerous.items;

import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.db.enums.CollectionType;
import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.items.enums.ItemType;
import com.phazerous.phazerous.items.enums.RarityType;
import com.phazerous.phazerous.items.models.items.ArmorItem;
import com.phazerous.phazerous.items.models.items.CustomItem;
import com.phazerous.phazerous.items.models.items.GatheringItem;
import com.phazerous.phazerous.items.models.items.WeaponItem;
import com.phazerous.phazerous.items.repository.ItemRepository;
import com.phazerous.phazerous.items.utils.ItemUtils;
import com.phazerous.phazerous.utils.ConsoleDispatcher;
import javafx.util.Pair;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemManager {

    private final DBManager dbManager;
    private final ItemBuilderDirector itemBuilderDirector;
    private final ItemRepository itemRepository;
    private final HashMap<ObjectId, ItemStack> itemsHashmap = new HashMap<>();

    public ItemManager(DBManager dbManager, ItemRepository itemRepository, ItemBuilderDirector itemBuilderDirector) {
        this.dbManager = dbManager;
        this.itemRepository = itemRepository;
        this.itemBuilderDirector = itemBuilderDirector;
    }

    public ItemStack buildItem(ObjectId itemId) {
        Pair<CustomItem, ItemType> item = itemRepository.getItem(itemId);

        try {
            return itemBuilderDirector.buildItem(item.getKey(), item.getValue());
        } catch (Exception e) {
            ConsoleDispatcher.error("Error building item");
            e.printStackTrace();
            return null;
        }
    }

    public ItemStack getItemById(ObjectId itemId) {
        if (!itemsHashmap.containsKey(itemId)) {
            Document itemDoc = getItemDoc(itemId);
            ItemType itemType = getItemType(itemDoc);

            CustomItem item = DocumentParser.parse(itemDoc, itemType.getItemClass());

            String title = item.getTitle();
            Material material = Material.getMaterial(item.getMaterialType());
            Integer additionalMaterialType = item.getAdditionalMaterialType();

            ItemStack itemStack = additionalMaterialType == null ? new ItemStack(material) : new ItemStack(material, 1, additionalMaterialType.shortValue());

            ItemMeta itemMeta = itemStack.getItemMeta();
            setItemDescription(itemMeta, title, item.getRarityType());
            itemStack.setItemMeta(itemMeta);

            itemStack = ItemUtils.setItemId(itemStack, itemId.toHexString());
            itemStack = ItemUtils.setItemType(itemStack, itemType);
            itemStack = ItemUtils.setUnbreakable(itemStack, true);

            if (itemType == ItemType.GATHERING_DIGGING) {
                itemStack = ItemUtils.setItemSpeed(itemStack, ((GatheringItem) item).getSpeed());
            } else if (itemType == ItemType.WEAPON_HANDHELD) {
                itemStack = ItemUtils.setItemDamage(itemStack, ((WeaponItem) item).getDamage());
            } else if (itemType == ItemType.ARMOR) {
                itemStack = ItemUtils.setDefense(itemStack, ((ArmorItem) item).getDefense());
            }


            itemsHashmap.put(itemId, itemStack);
        }

        return itemsHashmap
                .get(itemId)
                .clone();
    }

    private ItemType getItemType(Document itemDoc) {
        final String ITEM_TYPE_STRING = "itemType";

        Integer itemTypeCode = itemDoc.getInteger(ITEM_TYPE_STRING);
        return ItemType.getItemTypeByValue(itemTypeCode);
    }

    public String getItemTitle(ObjectId itemId) {
        Document itemDoc = getItemDoc(itemId);
        return itemDoc.getString("title");
    }

    private Document getItemDoc(ObjectId itemId) {
        return dbManager.getDocument(itemId, CollectionType.ITEMS);
    }

    private void setItemDescription(ItemMeta itemMeta, String title, RarityType rarityType) {
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES); // HIDE +1 ATTACK DAMAGE, ETC.

        String color = rarityType.getColor();

        String formattedTitle = ChatColor.translateAlternateColorCodes('§', "§r" + color + title);
        String formattedRarity = ChatColor.translateAlternateColorCodes('§', "§r§l" + color + rarityType.getTitle());

        itemMeta.setDisplayName(formattedTitle);

        List<String> itemLore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
        itemLore.add(formattedRarity);

        itemMeta.setLore(itemLore);
    }
}
