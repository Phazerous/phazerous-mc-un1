package com.phazerous.phazerous.player;

import com.phazerous.phazerous.archtecture.AbstractModule;
import com.phazerous.phazerous.shared.SharedModule;
import lombok.Getter;

@Getter
public class PlayerModule extends AbstractModule {
    private final SharedModule sharedModule;
    private final PlayerRepository playerRepository;

    public PlayerModule(SharedModule sharedModule) {
        this.sharedModule = sharedModule;

        this.playerRepository = new PlayerRepository(sharedModule.getDbManager());
    }
}
