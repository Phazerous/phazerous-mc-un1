package com.phazerous.phazerous.gathering;

import com.phazerous.phazerous.archtecture.AbstractModule;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.gathering.listeners.GatherStartListener;
import com.phazerous.phazerous.gathering.listeners.VeinGUIListener;
import com.phazerous.phazerous.gathering.manager.*;
import com.phazerous.phazerous.gathering.repository.VeinGUIRepository;
import com.phazerous.phazerous.gathering.repository.VeinLayersRepository;
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
        VeinGUIRepository veinGUIRepository = new VeinGUIRepository();
        VeinLayersRepository veinLayersRepository = new VeinLayersRepository();

        VeinManager veinManager = new VeinManager(sharedModule.getSpawnPacketManager(), veinRepository);
        VeinToolsManager veinToolsManager = new VeinToolsManager(playerRepository, veinToolsRepository);
        VeinResourceManager veinResourceManager = new VeinResourceManager();
        VeinGatheringManager veinGatheringManager = new VeinGatheringManager(veinLayersRepository, veinToolsManager, veinManager, veinResourceManager);
        VeinGUIManager veinGUIManager = new VeinGUIManager(veinGUIRepository, veinToolsManager, veinGatheringManager, sharedModule.getScheduler());

        veinGatheringManager.setVeinGUIManager(veinGUIManager);

        GatherStartListener gatherStartListener = new GatherStartListener(sharedModule.getPlugin(), veinManager);
        gatherStartListener.addObserver(veinGatheringManager);

        addListener(new VeinGUIListener(veinGUIManager, sharedModule.getScheduler()));

        this.veinManager = veinManager;
    }
}
