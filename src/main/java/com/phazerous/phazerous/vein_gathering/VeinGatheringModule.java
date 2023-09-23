package com.phazerous.phazerous.vein_gathering;

import com.phazerous.phazerous.archtecture.AbstractModule;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.items.utils.DropManager;
import com.phazerous.phazerous.player.PlayerRepository;
import com.phazerous.phazerous.shared.SharedModule;
import com.phazerous.phazerous.vein_gathering.listeners.GatherStartListener;
import com.phazerous.phazerous.vein_gathering.listeners.VeinGUIListener;
import com.phazerous.phazerous.vein_gathering.manager.*;
import com.phazerous.phazerous.vein_gathering.repository.VeinGUIRepository;
import com.phazerous.phazerous.vein_gathering.repository.VeinLayersRepository;
import com.phazerous.phazerous.vein_gathering.repository.VeinRepository;
import com.phazerous.phazerous.vein_gathering.repository.VeinToolsRepository;
import lombok.Getter;

@Getter
public class VeinGatheringModule extends AbstractModule {
    private final VeinManager veinManager;

    public VeinGatheringModule(SharedModule sharedModule, PlayerRepository playerRepository, DropManager dropManager) {
        DBManager dbManager = sharedModule.getDbManager();

        VeinRepository veinRepository = new VeinRepository(dbManager);
        VeinToolsRepository veinToolsRepository = new VeinToolsRepository(dbManager);
        VeinGUIRepository veinGUIRepository = new VeinGUIRepository();
        VeinLayersRepository veinLayersRepository = new VeinLayersRepository();

        VeinManager veinManager = new VeinManager(sharedModule.getSpawnPacketManager(), veinRepository);
        VeinToolsManager veinToolsManager = new VeinToolsManager(playerRepository, veinToolsRepository);
        VeinResourceManager veinResourceManager = new VeinResourceManager();
        VeinGatheringManager veinGatheringManager = new VeinGatheringManager(veinLayersRepository, veinToolsManager, veinManager, veinResourceManager, dropManager);
        VeinGUIManager veinGUIManager = new VeinGUIManager(veinGUIRepository, veinToolsManager, veinGatheringManager, sharedModule.getScheduler());

        veinGatheringManager.setVeinGUIManager(veinGUIManager);

        GatherStartListener gatherStartListener = new GatherStartListener(sharedModule.getPlugin(), veinManager);
        gatherStartListener.addObserver(veinGatheringManager);

        addListener(new VeinGUIListener(veinGUIManager, sharedModule.getScheduler()));

        this.veinManager = veinManager;
    }
}
