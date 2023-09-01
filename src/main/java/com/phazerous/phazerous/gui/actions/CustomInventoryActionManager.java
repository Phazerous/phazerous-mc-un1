package com.phazerous.phazerous.gui.actions;

import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.gui.GUISharedConstants;
import com.phazerous.phazerous.gui.actions.dtos.CustomInventoryActionDto;
import com.phazerous.phazerous.economy.EconomyManager;
import com.phazerous.phazerous.items.ItemManager;
import com.phazerous.phazerous.utils.NBTEditor;
import org.bson.types.ObjectId;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CustomInventoryActionManager {
    private final DBManager dbManager;
    private final EconomyManager economyManager;
    private final ItemManager itemManager;

    public CustomInventoryActionManager(DBManager dbManager, EconomyManager economyManager, ItemManager itemManager) {
        this.dbManager = dbManager;
        this.economyManager = economyManager;
        this.itemManager = itemManager;
    }

    public boolean hasAction(ItemStack item) {
        return NBTEditor.hasString(item, GUISharedConstants.ACTION_ID_NAME);
    }

    public void executeAction(ItemStack item, Player player) {
        String actionId = NBTEditor.getString(item, GUISharedConstants.ACTION_ID_NAME);
        CustomInventoryActionDto customInventoryActionDto = dbManager.getCustomInventoryActionByID(new ObjectId(actionId));
        GUIActionType actionType = GUIActionType.fromString(customInventoryActionDto.getType());

        if (actionType == GUIActionType.TRADE) executeBuyAction(customInventoryActionDto, player);
    }

    private void executeBuyAction(CustomInventoryActionDto customInventoryAction, Player player) {
        double price = customInventoryAction.getPrice();

        if (!(economyManager.withdraw(player.getUniqueId(), price))) return;

        ObjectId itemId = customInventoryAction.getItemId();
        ItemStack item = itemManager.getItemById(itemId);

        player
                .getInventory()
                .addItem(item);
    }
}
