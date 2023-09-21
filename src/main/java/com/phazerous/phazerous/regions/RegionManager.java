package com.phazerous.phazerous.regions;

import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.db.enums.CollectionType;
import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.regions.interfaces.IRegionChangeObserver;
import com.phazerous.phazerous.regions.models.Region;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RegionManager implements IRegionChangeObserver {
    private final DBManager dbManager;

    private List<Region> regions;
    private final HashMap<UUID, Region> playersRegion = new HashMap<>();

    public Region getPlayerRegion(UUID uuid) {
        return playersRegion.get(uuid);
    }

    public RegionManager(DBManager dbManager) {
        this.dbManager = dbManager;

        loadRegions();
    }

    public Region getRegionByLocation(Location location) {
        for (Region region : regions) {
            if (Region.isInRegion(location.getX(), location.getZ(), region)) {
                return region;
            }
        }

        return null;
    }

    private void loadRegions() {
        List<Document> documents = dbManager.getDocuments(CollectionType.REGIONS);

        regions = documents.stream()
                .map(document -> DocumentParser.parse(document, Region.class))
                .collect(Collectors.toList());
    }

    @Override
    public void onRegionChange(Player player, Region region) {
        playersRegion.put(player.getUniqueId(), region);
    }
}
