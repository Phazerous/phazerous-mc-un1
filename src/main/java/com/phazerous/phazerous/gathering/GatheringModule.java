package com.phazerous.phazerous.gathering;

import com.phazerous.phazerous.archtecture.AbstractModule;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.gathering.commands.ToolsCommand;

public class GatheringModule extends AbstractModule {
    public GatheringModule(DBManager dbManager) {
        GatheringManager gatheringManager = new GatheringManager(dbManager);

        addCommand(new ToolsCommand(gatheringManager));
    }
}
