package com.phazerous.phazerous.gui.actions;

import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.db.enums.CollectionType;
import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.gui.GUISharedConstants;
import com.phazerous.phazerous.gui.actions.models.CustomInventoryAction;
import com.phazerous.phazerous.economy.EconomyManager;
import com.phazerous.phazerous.items.ItemManager;
import com.phazerous.phazerous.utils.NBTEditor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GUIActionManager {
    private final DBManager dbManager;
    private final EconomyManager economyManager;
    private final ItemManager itemManager;

    public GUIActionManager(DBManager dbManager, EconomyManager economyManager, ItemManager itemManager) {
        this.dbManager = dbManager;
        this.economyManager = economyManager;
        this.itemManager = itemManager;
    }

    public boolean hasAction(ItemStack item) {
        return NBTEditor.hasString(item, GUISharedConstants.ACTION_ID_NAME);
    }

    public void executeAction(ItemStack item, Player player) {
        String actionId = NBTEditor.getString(item, GUISharedConstants.ACTION_ID_NAME);

        Document actionDoc = dbManager.getDocument(new ObjectId(actionId), CollectionType.CUSTOM_INVENTORIES_ACTIONS);
        CustomInventoryAction action = DocumentParser.parseDocument(actionDoc, CustomInventoryAction.class);

        GUIActionType actionType = GUIActionType.fromString(action.getType());

        if (actionType == GUIActionType.PURCHASE) executePurchaseAction(action, player);
    }

    private void executePurchaseAction(CustomInventoryAction action, Player player) {
        double price = action.getPrice();

        if (!(economyManager.withdraw(player.getUniqueId(), price))) return;

        ObjectId itemId = action.getItemId();
        ItemStack item = itemManager.getItemById(itemId);

        player.getInventory().addItem(item);
    }
}
