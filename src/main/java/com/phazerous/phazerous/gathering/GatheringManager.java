package com.phazerous.phazerous.gathering;

import com.mongodb.client.model.Filters;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.db.enums.CollectionType;
import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.gathering.enums.ToolSetType;
import com.phazerous.phazerous.gathering.models.MiningPickaxe;
import com.phazerous.phazerous.gathering.models.PositionedTool;
import com.phazerous.phazerous.gathering.models.ToolsMining;
import com.phazerous.phazerous.items.utils.ItemUtils;
import com.phazerous.phazerous.utils.InventoryUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GatheringManager {
    private final DBManager dbManager;
    private final ToolsManager toolsManager;

    private final Inventory backgroundInventory;
    private final HashMap<UUID, PositionedTool> playersCurrentTools = new HashMap<>();

    public GatheringManager(DBManager dbManager, ToolsManager toolsManager) {
        this.dbManager = dbManager;
        this.toolsManager = toolsManager;

        this.backgroundInventory = createBackgroundInventory();
    }

    public void handleClick(Player player, ItemStack clicked) {
        player.sendMessage("You clicked");
    }

    public void handleStart(Player player, ToolSetType toolSetType) {
        tools

        Inventory inventory = Bukkit.createInventory(null, backgroundInventory.getSize(), "Mining");

        inventory.setContents(backgroundInventory.getContents());
        int[] slotsToPlace = InventoryUtils.getItemsSlotsByPattern("  x      ", 4);

        for (int slot : slotsToPlace) {
            inventory.setItem(slot, buildTool(pickaxe));
            playersCurrentTools.put(player.getUniqueId(), new PositionedTool(slot, pickaxe));
        }

        player.openInventory(inventory);
    }

    private ItemStack buildTool(MiningPickaxe pickaxe) {
        ItemStack itemStack = new ItemStack(pickaxe.getMaterial());

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(pickaxe.getTitle());
        itemStack.setItemMeta(itemMeta);

        ItemUtils.setUnbreakable(itemStack, true);
        ItemUtils.hideAttributes(itemStack);

        return itemStack;
    }

    private Document getToolSetDocument(UUID playerUUID, ToolSetType toolSetType) {
        String propertyName = toolSetType.getDbPropertyName();

        String documentField = "$tools." + propertyName;

        Bson filter = Filters.eq("uuid", UUID.fromString("3e483d88-5b2e-32d2-9c4e-b1540a7aa176"));
        Bson projection = Filters.eq(propertyName, documentField);

        return dbManager.getDocument(filter, CollectionType.PLAYER, projection)
                .get(propertyName, Document.class);
    }

    private Inventory createBackgroundInventory() {
        final int INVENTORY_LINES = 5;

        Inventory inventory = Bukkit.createInventory(null, INVENTORY_LINES * 9, "Gathering Pattern");

        ItemStack backgroundItemInventory = getBackgroundInventoryItem();
        int[] backgroundInventorySlots = getBackgroundInventorySlots();

        for (int slot : backgroundInventorySlots) {
            inventory.setItem(slot, backgroundItemInventory);
        }

        return inventory;
    }

    private ItemStack getBackgroundInventoryItem() {
        final Material material = Material.STAINED_GLASS_PANE;
        final int additionalMaterialType = 7;

        ItemStack itemStack = new ItemStack(material, 1, (short) additionalMaterialType);

        return itemStack;
    }

    private int[] getBackgroundInventorySlots() {
        List<String> pattern = new ArrayList<String>() {
            {
                add("xx-----xx");
                add("xxxxxxxxx");
                add("xxxx-xxxx");
                add("xxxxxxxxx");
                add("xx-----xx");
            }
        };

        return InventoryUtils.getItemsSlotsByPattern(pattern);
    }
}
