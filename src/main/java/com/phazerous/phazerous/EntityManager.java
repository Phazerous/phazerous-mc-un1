package com.phazerous.phazerous;

import com.phazerous.phazerous.dtos.EntityDto;
import com.phazerous.phazerous.dtos.LocationedEntityDto;
import com.phazerous.phazerous.dtos.RuntimeEntityDto;
import org.bson.types.ObjectId;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EntityManager {
    private final DBManager dbManager;
    private final World world;

    private final HashMap<ObjectId, EntityDto> entitiesDtos = new HashMap<>();

    public EntityManager(DBManager dbManager, World world) {
        this.dbManager = dbManager;
        this.world = world;
    }

    public RuntimeEntityDto spawnEntity(LocationedEntityDto locationedEntityDto) {
        Location entityLocation = getEntityLocation(locationedEntityDto);
        EntityDto entityDto = getEntityDto(locationedEntityDto.getEntityId());
        byte entityType = entityDto.getType();

        UUID uuid = null;

        if (entityType == 0) {
            uuid = spawnArmorStand(entityLocation, entityDto);
        }

        if (uuid == null) return null;

        return prepareRuntimeEntity(uuid, locationedEntityDto.getId());
    }

    public void loadEntities() {
        List<LocationedEntityDto> locationedEntitiesDtos = dbManager.getLocationedEntitiesDtos();

        List<RuntimeEntityDto> runtimeEntities = new ArrayList<>();

        for (LocationedEntityDto locationedEntityDto : locationedEntitiesDtos) {
            RuntimeEntityDto runtimeEntity = spawnEntity(locationedEntityDto);
            runtimeEntities.add(runtimeEntity);
        }

        dbManager.insertRuntimeEntitiesDtos(runtimeEntities);
    }

    public void unloadEntities() {
        List<RuntimeEntityDto> runtimeEntityDtos = dbManager.getRuntimeEntitiesDtos();

        for (RuntimeEntityDto runtimeEntityDto : runtimeEntityDtos) {
            removeEntity(runtimeEntityDto.getUuid());
        }

        dbManager.deleteRuntimeEntities(runtimeEntityDtos);
    }

    private void removeEntity(UUID uuid) {
        for (Entity entity : world.getEntities()) {
            if (entity.getUniqueId().equals(uuid)) {
                entity.remove();
                return;
            }
        }
    }

    private UUID spawnArmorStand(Location location, EntityDto entityDto) {
        ArmorStand armorStand = world.spawn(location, ArmorStand.class);

        armorStand.setCustomName(entityDto.getTitle());
        armorStand.setCustomNameVisible(true);

        armorStand.setGravity(false);
        armorStand.setSmall(true);
        armorStand.setVisible(true);

        return armorStand.getUniqueId();
    }

    private RuntimeEntityDto prepareRuntimeEntity(UUID uuid, ObjectId locationedEntityId) {
        RuntimeEntityDto runtimeEntityDto = new RuntimeEntityDto();
        runtimeEntityDto.setLocationedEntityId(locationedEntityId);
        runtimeEntityDto.setUuid(uuid);
        return runtimeEntityDto;
    }

    private EntityDto getEntityDto(ObjectId entityId) {
        if (!entitiesDtos.containsKey(entityId)) {
            EntityDto entityDto = dbManager.getEntityDtoById(entityId);
            entitiesDtos.put(entityId, entityDto);
        }

        return entitiesDtos.get(entityId);
    }

    private Location getEntityLocation(LocationedEntityDto locationedEntityDto) {
        double x = locationedEntityDto.getX();
        double y = locationedEntityDto.getY();
        double z = locationedEntityDto.getZ();

        return new Location(world, x, y, z);
    }
}
