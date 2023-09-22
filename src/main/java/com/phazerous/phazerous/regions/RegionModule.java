package com.phazerous.phazerous.regions;

import com.phazerous.phazerous.archtecture.AbstractModule;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.player.PlayerRepository;
import com.phazerous.phazerous.regions.interfaces.IRegionChangeObserver;
import com.phazerous.phazerous.regions.listeners.RegionChangeListener;
import com.phazerous.phazerous.shared.SharedModule;

public class RegionModule extends AbstractModule {
    private final RegionChangeListener regionChangeListener;

    public RegionModule(SharedModule sharedModule) {
        RegionManager regionManager = new RegionManager(sharedModule.getDbManager());

        regionChangeListener = new RegionChangeListener(regionManager);
        subscribeToRegionChange(regionManager);

        addListener(regionChangeListener);
    }

    public void subscribeToRegionChange(IRegionChangeObserver observer) {
        regionChangeListener.subscribe(observer);
    }
}
