package com.phazerous.phazerous.gathering;

import com.mongodb.client.model.Filters;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.db.enums.CollectionType;
import com.phazerous.phazerous.gathering.enums.GatheringType;
import com.phazerous.phazerous.utils.InventoryUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GatheringManager {
    private final DBManager dbManager;

    private final Inventory backgroundInventory;

    public GatheringManager(DBManager dbManager) {
        this.dbManager = dbManager;

        this.backgroundInventory = createBackgroundInventory();
    }

    public void handleGathering(Player player, GatheringType gatheringType) {
        Document tools = getToolsDocument(player.getUniqueId(), gatheringType);
    }

    private Document getToolsDocument(UUID playerUUID, GatheringType gatheringType) {
        String propertyName = gatheringType.getDbPropertyName();

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
                add("x-------x");
                add("xxxxxxxxx");
                add("xxxx-xxxx");
                add("xxxxxxxxx");
                add("x-------x");
            }
        };

        return InventoryUtils.getItemsSlotsByPattern(pattern);
    }
}
