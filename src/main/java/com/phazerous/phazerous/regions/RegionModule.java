package com.phazerous.phazerous.regions;

import com.phazerous.phazerous.archtecture.AbstractModule;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.regions.interfaces.IRegionChangeObserver;
import com.phazerous.phazerous.regions.listeners.RegionChangeListener;

public class RegionModule extends AbstractModule {
    private final RegionChangeListener regionChangeListener;

    public RegionModule(DBManager dbManager) {
        RegionManager regionManager = new RegionManager(dbManager);

        regionChangeListener = new RegionChangeListener(regionManager);
        subscribeToRegionChange(regionManager);

        addListener(regionChangeListener);
    }

    public void subscribeToRegionChange(IRegionChangeObserver observer) {
        regionChangeListener.subscribe(observer);
    }
}
