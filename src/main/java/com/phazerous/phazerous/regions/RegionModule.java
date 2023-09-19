package com.phazerous.phazerous.regions;

import com.phazerous.phazerous.archtecture.AbstractModule;
import com.phazerous.phazerous.regions.listeners.IRegionChangeObserver;
import com.phazerous.phazerous.regions.listeners.RegionChangeListener;
import lombok.Getter;

public class RegionModule extends AbstractModule {
    private final RegionChangeListener regionChangeListener;

    public RegionModule() {
        RegionManager regionManager = new RegionManager();

        regionManager.test();

        regionChangeListener = new RegionChangeListener(regionManager);
        subscribeToRegionChange(regionManager);

        addListener(regionChangeListener);
    }

    public void subscribeToRegionChange(IRegionChangeObserver observer) {
        regionChangeListener.subscribe(observer);
    }
}
