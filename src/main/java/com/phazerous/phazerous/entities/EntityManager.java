package com.phazerous.phazerous.entities;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.phazerous.phazerous.db.CollectionType;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.db.DocumentBuilder;
import com.phazerous.phazerous.db.DocumentParser;
import com.phazerous.phazerous.entities.models.*;
import com.phazerous.phazerous.entities.models.runtime.RuntimeBaseEntity;
import com.phazerous.phazerous.entities.models.runtime.RuntimeGatheringEntity;
import com.phazerous.phazerous.entities.models.runtime.RuntimeMobEntity;
import com.phazerous.phazerous.utils.NBTEditor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class EntityManager {
    private static EntityManager entityManager;
    private final DBManager dbManager;
    private final World world;

    private final HashMap<ObjectId, BaseEntity> entities = new HashMap<>();

    public EntityManager(DBManager dbManager, World world) {
        this.dbManager = dbManager;
        this.world = world;
        EntityManager.entityManager = this;
    }

    public Document spawnEntity(LocationedEntity locationedEntity) {
        final String ENTITY_TYPE_NAME = "entityType";

        Location entityLocation = getEntityLocation(locationedEntity);
        ObjectId entityId = locationedEntity.getEntityId();
        Document entityDoc = dbManager.getDocumentById(entityId, CollectionType.ENTITIES);
        Integer entityTypeCode = entityDoc.getInteger(ENTITY_TYPE_NAME);
        EntityType entityType = EntityType.fromInteger(entityTypeCode);

        Document runtimeEntityDoc = null;

        if (entityType == EntityType.GATHERING_ENTITY) {
            GatheringEntity gatheringEntity = getEntity(entityId, GatheringEntity.class);
            runtimeEntityDoc = spawnGatheringEntity(entityLocation, gatheringEntity);
        } else if (entityType == EntityType.MOB_ENTITY) {
            MobEntity mobEntity = getEntity(entityId, MobEntity.class);
            runtimeEntityDoc = spawnMobEntity(entityLocation, mobEntity);
        }

        return runtimeEntityDoc
                .append("locationedEntityId", locationedEntity.get_id())
                .append("entityType", entityTypeCode);
    }

    public void respawnEntity(LocationedEntity locationedEntity) {
        Document runtimeEntityDoc = spawnEntity(locationedEntity);

        dbManager.insertDocument(runtimeEntityDoc, CollectionType.RUNTIME_ENTITY);
    }

    public void loadEntities() {
        List<Document> locationedEntitiesDocs = dbManager.getDocuments(CollectionType.LOCATIONED_ENTITIES);
        List<LocationedEntity> locationedEntities = locationedEntitiesDocs
                .stream()
                .map(it -> DocumentParser.parseDocument(it, LocationedEntity.class))
                .collect(Collectors.toList());

        List<Document> runtimeEntitiesDocs = new ArrayList<>();

        for (LocationedEntity locationedEntity : locationedEntities) {
            Document runtimeEntityDoc = spawnEntity(locationedEntity);
            runtimeEntitiesDocs.add(runtimeEntityDoc);
        }

        dbManager.insertDocuments(runtimeEntitiesDocs, CollectionType.RUNTIME_ENTITY);
    }

    public void unloadEntities() {
        List<RuntimeBaseEntity> runtimeEntities = getRuntimeEntities();

        for (RuntimeBaseEntity runtimeBaseEntity : runtimeEntities) {
            removeEntityByUUID(runtimeBaseEntity.getUuid());
        }

        List<ObjectId> runtimeEntitiesIds = runtimeEntities
                .stream()
                .map(RuntimeBaseEntity::get_id)
                .collect(Collectors.toList());

        dbManager.deleteDocuments(runtimeEntitiesIds, CollectionType.RUNTIME_ENTITY);
    }

    public void removeEntity(Entity entity, RuntimeBaseEntity runtimeBaseEntity) {
        dbManager.deleteDocument(runtimeBaseEntity.get_id(), CollectionType.RUNTIME_ENTITY);

        entity.remove();
    }

    public void removeEntityByUUID(UUID uuid) {
        for (Entity entity : world.getEntities()) {
            if (entity
                    .getUniqueId()
                    .equals(uuid)) {
                entity.remove();
                break;
            }
        }
    }

    private Document spawnGatheringEntity(Location location, GatheringEntity gatheringEntity) {
        ArmorStand armorStand = world.spawn(location, ArmorStand.class);

        armorStand.setCustomName(gatheringEntity.getTitle());
        armorStand.setCustomNameVisible(true);

        armorStand.setGravity(false);
        armorStand.setSmall(true);
        armorStand.setVisible(true);

        NBTEditor.setEntityPersistenceRequired(armorStand);

        RuntimeGatheringEntity runtimeGatheringEntity = new RuntimeGatheringEntity();
        runtimeGatheringEntity.setUuid(armorStand.getUniqueId());
        runtimeGatheringEntity.setLocationedEntityId(gatheringEntity.get_id());
        runtimeGatheringEntity.setHardness(gatheringEntity.getHardness());

        return DocumentBuilder.buildDocument(runtimeGatheringEntity);
    }

    /**
     * Spawns a mob entity at the given location.
     * Prepare MobRuntimeEntity, parse it to the document and return it.
     * ATTENTION: in the environment where the function is called, it's necessary to insert the locationEntityId
     *
     * @param location
     * @param mobEntity
     * @return Document of the mob runtime entity.
     */
    private Document spawnMobEntity(Location location, MobEntity mobEntity) {
        org.bukkit.entity.EntityType mobType = org.bukkit.entity.EntityType.fromId(mobEntity.getMobType());
        Class<? extends Entity> mobClass = mobType.getEntityClass();

        Entity mob = world.spawn(location, mobClass);

        EntityUtils.setMobEntityHPTitle(mob, mobEntity.getTitle(), mobEntity.getMaxHealth(), mobEntity.getMaxHealth());
        mob.setCustomNameVisible(true);

        NBTEditor.setEntityPersistenceRequired(mob);

        UUID uuid = mob.getUniqueId();


        RuntimeMobEntity mobRuntimeEntity = new RuntimeMobEntity();
        mobRuntimeEntity.setUuid(uuid);
        mobRuntimeEntity.setHealth(mobEntity.getMaxHealth());
        mobRuntimeEntity.setMaxHealth(mobEntity.getMaxHealth());
        mobRuntimeEntity.setTitle(mobEntity.getTitle());

        return DocumentBuilder.buildDocument(mobRuntimeEntity);
    }

    public <T extends BaseEntity> T getEntity(ObjectId entityId, Class<T> entityClass) {
        if (!entities.containsKey(entityId)) {
            Document entityDoc = dbManager.getDocumentById(entityId, CollectionType.ENTITIES);
            T entity = DocumentParser.parseDocument(entityDoc, entityClass);
            entities.put(entityId, entity);
        }

        return (T) entities.get(entityId);
    }

    public LocationedEntity getLocationedEntityById(ObjectId id) {
        Document locationedEntityDoc = dbManager.getDocumentById(id, CollectionType.LOCATIONED_ENTITIES);

        return DocumentParser.parseDocument(locationedEntityDoc, LocationedEntity.class);
    }

    private Location getEntityLocation(LocationedEntity locationedEntity) {
        double x = locationedEntity.getX();
        double y = locationedEntity.getY();
        double z = locationedEntity.getZ();

        return new Location(world, x, y, z);
    }

    /**
     * Returns the runtime entity by the given uuid.
     * The function automatically parses the document to the entity type.
     * ATTENTION: the function doesn't check if the entity exists.
     *
     * @param uuid
     * @return
     */
    public Document getRuntimeEntityDocByUUID(UUID uuid) {
        Bson query = Filters.eq("uuid", uuid);

        Document runtimeEntityDoc = dbManager.getDocument(query, CollectionType.RUNTIME_ENTITY);

        return runtimeEntityDoc;
    }

    public void spawnEntityAndInsert(LocationedEntity locationedEntity) {
        Document runtimeEntityDoc = spawnEntity(locationedEntity);
        dbManager.insertDocument(runtimeEntityDoc, CollectionType.RUNTIME_ENTITY);
    }

    public void updateRuntimeEntityHealth(ObjectId objectId, Long health) {
        Bson update = Updates.set("health", health);

        dbManager.updateOne(update, objectId, CollectionType.RUNTIME_ENTITY);
    }

    public static EntityManager getInstance() {
        return EntityManager.entityManager;
    }

    private List<RuntimeBaseEntity> getRuntimeEntities() {
        List<Document> runtimeEntitiesDocs = dbManager.getDocuments(CollectionType.RUNTIME_ENTITY);
        return runtimeEntitiesDocs
                .stream()
                .map(it -> DocumentParser.parseDocument(it, RuntimeBaseEntity.class))
                .collect(Collectors.toList());
    }
}
