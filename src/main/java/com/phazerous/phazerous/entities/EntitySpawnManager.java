package com.phazerous.phazerous.entities;

import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.entities.enums.EntityType;
import com.phazerous.phazerous.entities.interfaces.IEntitySpawnSubscriber;
import com.phazerous.phazerous.entities.models.entities.BaseEntity;
import com.phazerous.phazerous.entities.models.entities.GatheringEntity;
import com.phazerous.phazerous.entities.models.entities.LocationedEntity;
import com.phazerous.phazerous.entities.models.entities.MobEntity;
import com.phazerous.phazerous.entities.runtime.models.RuntimeBaseEntity;
import com.phazerous.phazerous.entities.runtime.EntityRuntimeManager;
import com.phazerous.phazerous.entities.utils.EntityUtils;
import com.phazerous.phazerous.utils.NBTEditor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class EntitySpawnManager {
    private final EntityManager entityManager;
    private final EntityRuntimeManager entityRuntimeManager;
    private final List<IEntitySpawnSubscriber> subscribers = new ArrayList<>();

    public EntitySpawnManager(EntityManager entityManager, EntityRuntimeManager entityRuntimeManager) {
        this.entityManager = entityManager;
        this.entityRuntimeManager = entityRuntimeManager;
    }

    public Entity spawnEntity(LocationedEntity locationedEntity) {
        ObjectId entityId = locationedEntity.getEntityId();
        Document entityDoc = entityManager.getEntityDoc(entityId);
        EntityType entityType = EntityUtils.getEntityType(entityDoc);
        Class<? extends BaseEntity> entityTypeClass = entityType.getEntityClass();

        Location entityLocation = EntityUtils.getEntityLocation(locationedEntity);

        Entity entity = null;

        BaseEntity parsedEntity = DocumentParser.parseDocument(entityDoc, entityTypeClass);

        if (entityType == EntityType.GATHERING_ENTITY) {
            entity = spawnGatheringEntity(entityLocation, (GatheringEntity) parsedEntity);
        } else if (entityType == EntityType.MOB_ENTITY) {
            entity = spawnMobEntity(entityLocation, (MobEntity) parsedEntity);
        } else if (entityType == EntityType.BOSS_ENTITY) {
            return null;
        }

        NBTEditor.setEntityPersistenceRequired(entity);

        handleSpawn(entity.getUniqueId(), parsedEntity, locationedEntity.get_id());

        return entity;
    }

    private Entity spawnGatheringEntity(Location location, GatheringEntity gatheringEntity) {
        ArmorStand armorStand = (ArmorStand) location.getWorld()
                .spawnEntity(location, org.bukkit.entity.EntityType.ARMOR_STAND);

        armorStand.setCustomName(gatheringEntity.getTitle());
        armorStand.setCustomNameVisible(true);

        armorStand.setGravity(false);
        armorStand.setVisible(true);
        armorStand.setVisible(false);

        return armorStand;
    }

    private Entity spawnMobEntity(Location location, MobEntity mobEntity) {
        org.bukkit.entity.EntityType mobType = org.bukkit.entity.EntityType.fromId(mobEntity.getMobType());
        Class<? extends Entity> mobClass = mobType.getEntityClass();

        Entity mob = location.getWorld().spawn(location, mobClass);

        EntityUtils.setMobEntityHPTitle(mob, mobEntity.getTitle(), mobEntity.getMaxHealth(), mobEntity.getMaxHealth());
        mob.setCustomNameVisible(true);

        ((LivingEntity) mob).getEquipment().setHelmet(new ItemStack(Material.STONE_BUTTON)); // SUNSCREEN

        return mob;
    }

    public void spawnEntities() {
        List<LocationedEntity> locationedEntities = entityManager.getLocationedEntities();

        for (LocationedEntity locationedEntity : locationedEntities) {
            spawnEntity(locationedEntity);
        }
    }

    public void despawnEntities() {
        List<UUID> runtimeEntitiesUUIDs = entityRuntimeManager.getRuntimeEntities().stream()
                .map(RuntimeBaseEntity::getUuid).collect(Collectors.toList());

        for (Entity entity : Bukkit.getWorld("world").getEntities()) {
            if (runtimeEntitiesUUIDs.contains(entity.getUniqueId())) {
                entity.remove();
                handleDespawn(entity.getUniqueId());
            }
        }
    }

    public void subscribe(IEntitySpawnSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    private void handleSpawn(UUID entityUUID, BaseEntity baseEntity, ObjectId locationEntityId) {
        for (IEntitySpawnSubscriber subscriber : subscribers) {
            subscriber.handleSpawn(entityUUID, baseEntity, locationEntityId);
        }
    }

    private void handleDespawn(UUID entityUUID) {
        for (IEntitySpawnSubscriber subscriber : subscribers) {
            subscriber.handleDespawn(entityUUID);
        }
    }
}
