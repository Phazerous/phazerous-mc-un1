package com.phazerous.phazerous.gui;

import com.phazerous.phazerous.dtos.CustomInventoryDto;
import com.phazerous.phazerous.managers.DBManager;
import com.phazerous.phazerous.managers.ItemManager;
import org.bson.types.ObjectId;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CustomInventoryManager {
    private final DBManager dbManager;
    private final ItemManager itemManager;
    private final HashMap<ObjectId, Inventory> inventories = new HashMap<>();

    public CustomInventoryManager(DBManager dbManager, ItemManager itemManager) {
        this.dbManager = dbManager;
        this.itemManager = itemManager;
    }

    private void createInventory(ObjectId inventoryId) {
        CustomInventoryDto inventoryDto = dbManager.getCustomInventoryDtoById(inventoryId);

        List<CustomInventoryItem> contents = inventoryDto
                .getContents()
                .stream()
                .map(it -> {
                    List<Integer> slots = it.getSlots();
                    ItemStack itemStack = itemManager.getItemById(it.getItemId());

                    return new CustomInventoryItem(itemStack, slots);
                })
                .collect(Collectors.toList());

        CustomInventory customInventory = new CustomInventory(inventoryDto.getSize(), inventoryDto.getTitle());
        customInventory.setContents(contents);
        Inventory inventory = customInventory.getInventory();

        inventories.put(inventoryId, inventory);
    }

    private Inventory getOrCreateInventory(ObjectId inventoryId) {
        if (!(inventories.containsKey(inventoryId))) createInventory(inventoryId);

        return inventories.get(inventoryId);
    }


    public void openInventory(Player player, ObjectId inventoryId) {
        Inventory inventory = getOrCreateInventory(inventoryId);
        player.openInventory(inventory);
    }
}
