package com.phazerous.phazerous.gui.managers;

import com.phazerous.phazerous.db.enums.CollectionType;
import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.gui.actions.GUIActionManager;
import com.phazerous.phazerous.gui.actions.models.AbstractGUIAction;
import com.phazerous.phazerous.gui.actions.models.PurchaseItemWithItemAction;
import com.phazerous.phazerous.gui.models.CustomInventory;
import com.phazerous.phazerous.gui.models.CustomInventoryItem;
import com.phazerous.phazerous.gui.models.db.GUIInventory;
import com.phazerous.phazerous.gui.models.db.GUIItem;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.items.ItemManager;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GUIManager {
    private final DBManager dbManager;
    private final ItemManager itemManager;
    private final GUIActionManager actionManager;
    private final HashMap<ObjectId, Inventory> inventories = new HashMap<>();


    public GUIManager(DBManager dbManager, ItemManager itemManager, GUIActionManager actionManager) {
        this.dbManager = dbManager;
        this.itemManager = itemManager;
        this.actionManager = actionManager;
    }

    private void createInventory(ObjectId inventoryId) {
        Document inventoryDoc = dbManager.getDocument(inventoryId, CollectionType.GUI_INVENTORIES);
        GUIInventory inventoryModel = DocumentParser.parseDocument(inventoryDoc, GUIInventory.class);

        List<GUIItem> contentsModel = inventoryModel.getContents();
        int size = inventoryModel.getSize();
        String title = inventoryModel.getTitle();

        List<CustomInventoryItem> contents = parseCustomInventoryItems(contentsModel);
        CustomInventory customInventory = new CustomInventory(size, title);
        customInventory.setContents(contents);
        Inventory inventory = customInventory.getInventory();

        inventories.put(inventoryId, inventory);
    }

    private List<CustomInventoryItem> parseCustomInventoryItems(List<GUIItem> itemDtos) {
        return itemDtos.stream().map(it -> {
            List<Integer> slots = it.getSlots();
            ItemStack itemStack = itemManager.getItemById(it.getItemId());

            AbstractGUIAction action = actionManager.getAction(it.getActionId());

            return new CustomInventoryItem(itemStack, slots, action);
        }).collect(Collectors.toList());
    }

    private Inventory getOrCreateInventory(ObjectId inventoryId) {
        if (!(inventories.containsKey(inventoryId))) createInventory(inventoryId);

        return inventories.get(inventoryId);
    }


    public void openInventory(Player player, ObjectId inventoryId) {
        Inventory inventory = getOrCreateInventory(inventoryId);
        player.openInventory(inventory);
    }


    /**
     * Checks if the inventory is a custom inventory.
     *
     * @param inventory The inventory to check.
     * @return true if the inventory is a custom inventory, <b>false</b> otherwise.
     */
    public boolean isCustomInventory(Inventory inventory) {
        return inventories.containsValue(inventory);
    }
}
