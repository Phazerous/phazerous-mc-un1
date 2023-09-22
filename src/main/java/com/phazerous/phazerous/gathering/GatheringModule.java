package com.phazerous.phazerous.gathering;

import com.phazerous.phazerous.archtecture.AbstractModule;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.gathering.listeners.GatherStartListener;
import com.phazerous.phazerous.gathering.listeners.InventoryClickListener;
import com.phazerous.phazerous.gathering.manager.GatheringManager;
import com.phazerous.phazerous.gathering.manager.VeinGUIManager;
import com.phazerous.phazerous.gathering.manager.VeinManager;
import com.phazerous.phazerous.gathering.manager.VeinToolsManager;
import com.phazerous.phazerous.gathering.repository.VeinRepository;
import com.phazerous.phazerous.gathering.repository.VeinToolsRepository;
import com.phazerous.phazerous.player.PlayerRepository;
import com.phazerous.phazerous.shared.SharedModule;
import lombok.Getter;

@Getter
public class GatheringModule extends AbstractModule {
    private final VeinManager veinManager;

    public GatheringModule(SharedModule sharedModule, PlayerRepository playerRepository) {
        DBManager dbManager = sharedModule.getDbManager();

        VeinRepository veinRepository = new VeinRepository(dbManager);
        VeinToolsRepository veinToolsRepository = new VeinToolsRepository(dbManager);

        VeinManager veinManager = new VeinManager(sharedModule.getSpawnPacketManager(), veinRepository);
        VeinToolsManager veinToolsManager = new VeinToolsManager(playerRepository, veinToolsRepository);
        VeinGUIManager veinGUIManager = new VeinGUIManager(veinToolsManager);
        GatheringManager gatheringManager = new GatheringManager(veinToolsManager, veinManager, veinGUIManager);

        GatherStartListener gatherStartListener = new GatherStartListener(sharedModule.getPlugin(), veinManager);
        gatherStartListener.addObserver(gatheringManager);

        addListener(new InventoryClickListener(gatheringManager));

        this.veinManager = veinManager;
    }
}
