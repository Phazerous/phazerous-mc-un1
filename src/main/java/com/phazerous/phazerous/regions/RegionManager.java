package com.phazerous.phazerous.regions;

import com.phazerous.phazerous.regions.listeners.IRegionChangeObserver;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RegionManager implements IRegionChangeObserver {
    private final HashMap<UUID, Region> playersRegion = new HashMap<>();
    private final List<Region> regions = new ArrayList<>();

    public Region getRegionByLocation(Location location) {
        for (Region region : regions) {
            if (Region.isInRegion(location.getX(), location.getZ(), region)) {
                return region;
            }
        }

        return null;
    }
    
    @Override
    public void onRegionChange(Player player, Region region) {
        playersRegion.put(player.getUniqueId(), region);
    }
}
