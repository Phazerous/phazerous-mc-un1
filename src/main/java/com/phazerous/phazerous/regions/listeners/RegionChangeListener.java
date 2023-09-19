package com.phazerous.phazerous.regions.listeners;

import com.phazerous.phazerous.regions.Region;
import com.phazerous.phazerous.regions.RegionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class RegionChangeListener implements Listener {
    private final RegionManager regionManager;
    private final List<IRegionChangeObserver> subscribers = new ArrayList<>();

    public RegionChangeListener(RegionManager regionManager) {
        this.regionManager = regionManager;
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        Region previousRegion = regionManager.getPreviousRegion(player.getUniqueId());
        Region currentRegion = regionManager.getRegionByLocation(player.getLocation());


        if (previousRegion == currentRegion || currentRegion == null) return;

        handleRegionChange(player, previousRegion, currentRegion);
    }

    private void handleRegionChange(Player player, Region previousRegion, Region currentRegion) {
        for (IRegionChangeObserver observer : subscribers) {
            observer.onRegionChange(player, currentRegion);
        }
    }

    public void subscribe(IRegionChangeObserver observer) {
        subscribers.add(observer);
    }
}
