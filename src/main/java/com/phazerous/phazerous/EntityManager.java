package com.phazerous.phazerous;

import com.phazerous.phazerous.dtos.EntityDto;
import com.phazerous.phazerous.dtos.LocationedEntityDto;
import com.phazerous.phazerous.dtos.RuntimeEntityDto;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bson.types.ObjectId;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EntityManager {
    private static EntityManager entityManager;
    private final DBManager dbManager;
    private final World world;

    private final HashMap<ObjectId, EntityDto> entitiesDtos = new HashMap<>();

    public EntityManager(DBManager dbManager, World world) {
        this.dbManager = dbManager;
        this.world = world;
        EntityManager.entityManager = this;
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

    public void respawnEntity(LocationedEntityDto locationedEntityDto) {
        RuntimeEntityDto runtimeEntityDto = spawnEntity(locationedEntityDto);

        List<RuntimeEntityDto> runtimeEntityDtos = new ArrayList<>();
        runtimeEntityDtos.add(runtimeEntityDto);

        dbManager.insertRuntimeEntitiesDtos(runtimeEntityDtos);
    }

    public RuntimeEntityDto spawnEntity(Location location, EntityDto entityDto) {
        byte entityType = entityDto.getType();

        UUID uuid = null;

        if (entityType == 0) {
            uuid = spawnArmorStand(location, entityDto);
        }

        if (uuid == null) return null;

        return prepareRuntimeEntity(uuid, null);
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
            removeEntityByUUID(runtimeEntityDto.getUuid());
        }

        dbManager.deleteRuntimeEntities(runtimeEntityDtos);
    }

    public void removeEntity(Entity entity, RuntimeEntityDto runtimeEntityDto) {
        dbManager.deleteRuntimeEntities(new ArrayList<RuntimeEntityDto>() { { add(runtimeEntityDto); } });
        entity.remove();
    }

    public void removeEntityByUUID(UUID uuid) {
        for (Entity entity : world.getEntities()) {
            if (entity.getUniqueId().equals(uuid)) {
                entity.remove();
                break;
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

        setEntityPersistenceRequired(armorStand);

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

    public RuntimeEntityDto getRuntimeEntityDtoByUUID(UUID uuid) {
        return dbManager.getRuntimeEntityDtoByUUID(uuid);
    }

    public LocationedEntityDto getLocationedEntityDtoById(ObjectId id) {
        return dbManager.getLocationedEntityDtoById(id);
    }

    public EntityDto getEntityDtoById(ObjectId id) {
        return dbManager.getEntityDtoById(id);
    }

    private Location getEntityLocation(LocationedEntityDto locationedEntityDto) {
        double x = locationedEntityDto.getX();
        double y = locationedEntityDto.getY();
        double z = locationedEntityDto.getZ();

        return new Location(world, x, y, z);
    }

    private void setEntityPersistenceRequired(Entity entity) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        NBTTagCompound tag = new NBTTagCompound();
        nmsEntity.c(tag);

        tag.setByte("PersistenceRequired", (byte) 1);

        nmsEntity.f(tag);
    }

    public static EntityManager getInstance() {
        return EntityManager.entityManager;
    }
}
