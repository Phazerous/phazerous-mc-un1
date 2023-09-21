package com.phazerous.phazerous.gathering;

import com.phazerous.phazerous.archtecture.AbstractModule;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.gathering.commands.ToolsCommand;
import com.phazerous.phazerous.gathering.listeners.GatherStartListener;
import com.phazerous.phazerous.gathering.listeners.InventoryClickListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class GatheringModule extends AbstractModule {
    private final GatheringManager gatheringManager;

    public GatheringModule(JavaPlugin plugin, DBManager dbManager) {
        ToolsManager toolsManager = new ToolsManager(dbManager);
        GatheringSpawnManager gatheringSpawnManager = new GatheringSpawnManager();
        GatherStartListener gatherStartListener = new GatherStartListener(plugin, gatheringSpawnManager);
        gatheringManager = new GatheringManager(dbManager, toolsManager, new VeinManager(dbManager), gatheringSpawnManager);


        gatherStartListener.addObserver(gatheringManager);


        addCommand(new ToolsCommand(gatheringManager));
        addListener(new InventoryClickListener(gatheringManager));
    }
}
