package com.phazerous.phazerous.gui.actions;

import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.db.enums.CollectionType;
import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.gui.GUISharedConstants;
import com.phazerous.phazerous.gui.actions.models.AbstractGUIAction;
import com.phazerous.phazerous.economy.EconomyManager;
import com.phazerous.phazerous.gui.actions.models.PurchaseItemWithMoneyAction;
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

    public ObjectId getActionId(ItemStack item) {
        String actionIdString = NBTEditor.getString(item, GUISharedConstants.ACTION_ID_NAME);
        return new ObjectId(actionIdString);
    }

    public void executeAction(ObjectId actionId, Player player) {
        AbstractGUIAction action = getAction(actionId);

        if (action instanceof PurchaseItemWithMoneyAction)
            executePurchaseItemWithMoneyAction((PurchaseItemWithMoneyAction) action, player);
    }


    public AbstractGUIAction getAction(ObjectId actionId) {
        Document actionDoc = dbManager.getDocument(actionId, CollectionType.GUI_ACTIONS);

        if (actionDoc == null) return null;

        GUIActionType actionType = getActionType(actionDoc);

        return DocumentParser.parseDocument(actionDoc, actionType.getActionSchema());
    }

    private void executePurchaseItemWithMoneyAction(PurchaseItemWithMoneyAction action, Player player) {
        Double price = action.getPrice();

        if (!(economyManager.withdraw(player.getUniqueId(), price))) return;

        ObjectId itemId = action.getItemIdToPurchase();
        ItemStack item = itemManager.getItemById(itemId);

        player.getInventory().addItem(item);
    }

    private GUIActionType getActionType(Document document) {
        final String ACTION_TYPE_NAME = "type";

        Integer type = document.getInteger(ACTION_TYPE_NAME);

        return GUIActionType.fromInteger(type);
    }
}
