package com.phazerous.phazerous.regions.interfaces;

import com.phazerous.phazerous.regions.models.Region;
import org.bukkit.entity.Player;

public interface IRegionChangeObserver {
    void onRegionChange(Player player, Region region);
}
