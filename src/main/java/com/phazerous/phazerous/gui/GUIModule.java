package com.phazerous.phazerous.gui;

import com.phazerous.phazerous.archtecture.AbstractModule;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.economy.EconomyManager;
import com.phazerous.phazerous.gui.actions.GUIActionManager;
import com.phazerous.phazerous.gui.commands.TestCommand;
import com.phazerous.phazerous.items.ItemManager;
import com.phazerous.phazerous.gui.listeners.InventoryClickListener;

public class GUIModule extends AbstractModule {
    public GUIModule(DBManager dbManager, ItemManager itemManager, EconomyManager economyManager) {
        GUIManager guiManager = new GUIManager(dbManager, itemManager);
        GUIActionManager guiActionManager = new GUIActionManager(dbManager, economyManager, itemManager);

        addListener(new InventoryClickListener(guiManager, guiActionManager));
        addCommand(new TestCommand(guiManager));
    }
}
