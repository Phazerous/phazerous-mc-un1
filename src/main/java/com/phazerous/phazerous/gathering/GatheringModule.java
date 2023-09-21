package com.phazerous.phazerous.gathering;

import com.phazerous.phazerous.archtecture.AbstractModule;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.gathering.commands.ToolsCommand;
import com.phazerous.phazerous.gathering.listeners.InventoryClickListener;

public class GatheringModule extends AbstractModule {
    public GatheringModule(DBManager dbManager) {
        ToolsManager toolsManager = new ToolsManager(dbManager);
        GatheringManager gatheringManager = new GatheringManager(dbManager, toolsManager);

        addCommand(new ToolsCommand(gatheringManager));
        addListener(new InventoryClickListener(gatheringManager));
    }
}
