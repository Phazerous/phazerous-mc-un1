package com.phazerous.phazerous.gui.actions;

import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.db.enums.CollectionType;
import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.gui.GUISharedConstants;
import com.phazerous.phazerous.gui.actions.models.AbstractGUIAction;
import com.phazerous.phazerous.economy.EconomyManager;
import com.phazerous.phazerous.gui.actions.models.PurchaseItemWithItemAction;
import com.phazerous.phazerous.gui.actions.models.PurchaseItemWithMoneyAction;
import com.phazerous.phazerous.items.ItemManager;
import com.phazerous.phazerous.utils.InventoryUtils;
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
        return NBTEditor.hasKey(item, GUISharedConstants.ACTION_ID_NAME);
    }

    public ObjectId getActionId(ItemStack item) {
        String actionIdString = NBTEditor.getString(item, GUISharedConstants.ACTION_ID_NAME);
        return new ObjectId(actionIdString);
    }

    public void executeAction(ObjectId actionId, Player player) {
        AbstractGUIAction action = getAction(actionId);

        if (action instanceof PurchaseItemWithMoneyAction)
            executePurchaseItemWithMoneyAction((PurchaseItemWithMoneyAction) action, player);
        else if (action instanceof PurchaseItemWithItemAction)
            execturePurchaseItemWithItemAction((PurchaseItemWithItemAction) action, player);
    }


    public AbstractGUIAction getAction(ObjectId actionId) {
        Document actionDoc = dbManager.getDocument(actionId, CollectionType.GUI_ACTIONS);

        if (actionDoc == null) return null;

        GUIActionType actionType = getActionType(actionDoc);

        AbstractGUIAction abstractGUIAction = DocumentParser.parse(actionDoc, actionType.getActionSchema());

        if (actionType == GUIActionType.PURCHASE_ITEM_WITH_ITEM) {
            PurchaseItemWithItemAction purchaseItemWithItemAction = (PurchaseItemWithItemAction) abstractGUIAction;
            ObjectId requestedItemId = purchaseItemWithItemAction.getRequestedItemId();
            String requestedItemTitle = itemManager.getItemTitle(requestedItemId);
            purchaseItemWithItemAction.setRequestedItemName(requestedItemTitle);
        }

        return abstractGUIAction;
    }

    private void executePurchaseItemWithMoneyAction(PurchaseItemWithMoneyAction action, Player player) {
        Long price = action.getPrice();

        if (!(economyManager.withdraw(player.getUniqueId(), price))) return;

        ObjectId itemId = action.getItemIdToPurchase();
        ItemStack item = itemManager.getItemById(itemId);

        player.getInventory().addItem(item);
    }

    private void execturePurchaseItemWithItemAction(PurchaseItemWithItemAction action, Player player) {
        ObjectId requestedItemId = action.getRequestedItemId();
        Integer amount = action.getAmount();

        if (InventoryUtils.countItemsInInventory(player, requestedItemId) < amount)
            return;

        if (!InventoryUtils.withdrawItemsFromInventory(player, requestedItemId, amount))
            return;

        ObjectId itemIdToPurchase = action.getItemIdToPurchase();
        ItemStack purchasedItem = itemManager.getItemById(itemIdToPurchase);

        player.getInventory().addItem(purchasedItem);
    }

    private GUIActionType getActionType(Document document) {
        final String ACTION_TYPE_NAME = "type";

        Integer type = document.getInteger(ACTION_TYPE_NAME);

        return GUIActionType.fromInteger(type);
    }
}
