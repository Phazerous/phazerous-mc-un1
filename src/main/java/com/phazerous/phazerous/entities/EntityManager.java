package com.phazerous.phazerous.entities;

import com.phazerous.phazerous.db.CollectionType;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.db.DocumentBuilder;
import com.phazerous.phazerous.db.DocumentParser;
import com.phazerous.phazerous.entities.models.*;
import com.phazerous.phazerous.utils.NBTEditor;
import org.bson.Document;
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
        String ENTITY_TYPE_NAME = "entityType";

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

        return runtimeEntityDoc.append("locationedEntityId", locationedEntity.get_id());
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
        List<BaseRuntimeEntity> runtimeEntities = getRuntimeEntities();

        for (BaseRuntimeEntity baseRuntimeEntity : runtimeEntities) {
            removeEntityByUUID(baseRuntimeEntity.getUuid());
        }

        List<ObjectId> runtimeEntitiesIds = runtimeEntities
                .stream()
                .map(BaseRuntimeEntity::get_id)
                .collect(Collectors.toList());

        dbManager.deleteDocuments(runtimeEntitiesIds, CollectionType.RUNTIME_ENTITY);
    }

    public void removeEntity(Entity entity, BaseRuntimeEntity baseRuntimeEntity) {
        dbManager.deleteDocument(baseRuntimeEntity.get_id(), CollectionType.RUNTIME_ENTITY);

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

        BaseRuntimeEntity baseRuntimeEntity = new BaseRuntimeEntity();
        baseRuntimeEntity.setUuid(armorStand.getUniqueId());
        baseRuntimeEntity.setLocationedEntityId(gatheringEntity.get_id());

        return DocumentBuilder.buildDocument(baseRuntimeEntity);
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

        mob.setCustomName(mobEntity.getTitle());
        mob.setCustomNameVisible(true);

        NBTEditor.setEntityPersistenceRequired(mob);

        UUID uuid = mob.getUniqueId();

        MobRuntimeEntity mobRuntimeEntity = new MobRuntimeEntity();
        mobRuntimeEntity.setUuid(uuid);
        mobRuntimeEntity.setHealth(mobEntity.getHealth());
        mobRuntimeEntity.setMaxHealth(mobEntity.getHealth());

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

    public BaseRuntimeEntity getRuntimeEntityByUUID(UUID uuid) {
        Document runtimeEntityDoc = dbManager.getDocument(new Document("uuid", uuid), CollectionType.RUNTIME_ENTITY);

        return DocumentParser.parseDocument(runtimeEntityDoc, BaseRuntimeEntity.class);
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

    public static EntityManager getInstance() {
        return EntityManager.entityManager;
    }

    private List<BaseRuntimeEntity> getRuntimeEntities() {
        List<Document> runtimeEntitiesDocs = dbManager.getDocuments(CollectionType.RUNTIME_ENTITY);
        return runtimeEntitiesDocs
                .stream()
                .map(it -> DocumentParser.parseDocument(it, BaseRuntimeEntity.class))
                .collect(Collectors.toList());
    }
}
