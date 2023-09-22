package com.phazerous.phazerous.gathering.manager;

import com.phazerous.phazerous.gathering.models.Vein;
import com.phazerous.phazerous.gathering.models.VeinEntity;
import com.phazerous.phazerous.gathering.models.VeinLocation;
import com.phazerous.phazerous.gathering.repository.VeinRepository;
import com.phazerous.phazerous.regions.interfaces.IRegionChangeObserver;
import com.phazerous.phazerous.regions.models.Region;
import com.phazerous.phazerous.shared.SpawnPacketManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class VeinManager implements IRegionChangeObserver {
    private final SpawnPacketManager spawnPacketManager;
    private final VeinRepository veinRepository;
    private final HashMap<UUID, List<VeinEntity>> playerVeinsEntities = new HashMap<>();

    public VeinManager(SpawnPacketManager spawnPacketManager, VeinRepository veinRepository) {
        this.spawnPacketManager = spawnPacketManager;
        this.veinRepository = veinRepository;
    }

    public Vein getVein(Player player, int entityId) {
        List<VeinEntity> veinEntities = playerVeinsEntities.get(player.getUniqueId());

        if (veinEntities != null) {
            for (VeinEntity veinEntity : veinEntities) {
                if (veinEntity.getEntityId() == entityId) {
                    return veinRepository.getVein(veinEntity);
                }
            }
        }

        return null;
    }

    public boolean isVein(Player player, int entityId) {
        List<VeinEntity> veinEntities = playerVeinsEntities.get(player.getUniqueId());

        if (veinEntities != null) {
            for (VeinEntity veinEntity : veinEntities) {
                if (veinEntity.getEntityId() == entityId) {
                    return true;
                }
            }
        }

        return false;
    }

    private void createVeins(Player player, List<VeinLocation> veinLocations) {
        for (VeinLocation veinLocation : veinLocations) {
            Location location = new Location(null, veinLocation.getX(), veinLocation.getY(), veinLocation.getZ());
            int entityId = spawnPacketManager.spawnArmorStand(player, location);

            playerVeinsEntities.get(player.getUniqueId())
                    .add(new VeinEntity(veinLocation.getVeinId(), entityId));
        }
    }

    private void removeVeins(Player player) {
        List<VeinEntity> playerVeinEntities = playerVeinsEntities.get(player.getUniqueId());

        if (playerVeinEntities != null) {
            for (VeinEntity veinEntity : playerVeinEntities) {
                spawnPacketManager.despawnVeinEntity(player, veinEntity.getEntityId());
            }
        }

        playerVeinsEntities.put(player.getUniqueId(), new ArrayList<>());
    }

    @Override
    public void onRegionChange(Player player, Region region) {
        removeVeins(player);

        if (region != null) {
            createVeins(player, region.getVeinsLocations());
        }
    }
}
