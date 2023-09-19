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

    public Region getPreviousRegion(UUID uuid) {
        return playersRegion.get(uuid);
    }

    public void test() {
        Region regionWhite = new Region();
        regionWhite.setName("White");
        regionWhite.setX1Pos(-18);
        regionWhite.setZ1Pos(-8);
        regionWhite.setX2Pos(-9);
        regionWhite.setZ2Pos(1);

        Region regionOrange = new Region();
        regionOrange.setName("Orange");
        regionOrange.setX1Pos(-9);
        regionOrange.setZ1Pos(2);
        regionOrange.setX2Pos(-18);
        regionOrange.setZ2Pos(11);

        regions.add(regionWhite);
        regions.add(regionOrange);
    }

    @Override
    public void onRegionChange(Player player, Region region) {
        playersRegion.put(player.getUniqueId(), region);
    }
}
