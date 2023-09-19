package com.phazerous.phazerous.regions.listeners;

import com.phazerous.phazerous.regions.Region;
import org.bukkit.entity.Player;

public interface IRegionChangeObserver {
    void onRegionChange(Player player, Region region);
}
