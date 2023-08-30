package com.phazerous.phazerous.entities;

import com.phazerous.phazerous.db.CollectionType;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.db.DocumentParser;
import com.phazerous.phazerous.entities.models.BaseEntity;
import com.phazerous.phazerous.entities.models.GatheringEntity;
import com.phazerous.phazerous.entities.models.LocationedEntity;
import com.phazerous.phazerous.entities.models.RuntimeEntity;
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

    public RuntimeEntity spawnEntity(LocationedEntity locationedEntity) {
        String ENTITY_TYPE_NAME = "entityType";

        Location entityLocation = getEntityLocation(locationedEntity);
        ObjectId entityId = locationedEntity.getEntityId();
        Document entityDoc = dbManager.getDocumentById(entityId, CollectionType.ENTITIES);
        Integer entityType = entityDoc.getInteger(ENTITY_TYPE_NAME);

        UUID uuid = null;

        if (entityType == 0) {
            GatheringEntity gatheringEntity = getEntity(entityId, GatheringEntity.class);
            uuid = spawnGatheringEntity(entityLocation, gatheringEntity);
        }

        return prepareRuntimeEntity(uuid, locationedEntity.get_id());
    }

    public void respawnEntity(LocationedEntity locationedEntity) {
        RuntimeEntity runtimeEntity = spawnEntity(locationedEntity);
        Document runtimeEntityDoc = RuntimeEntity.toInsertDocument(runtimeEntity);

        dbManager.insertDocument(runtimeEntityDoc, CollectionType.RUNTIME_ENTITY);
    }

    public void loadEntities() {
        List<Document> locationedEntitiesDocs = dbManager.getDocuments(CollectionType.LOCATIONED_ENTITIES);
        List<LocationedEntity> locationedEntities = locationedEntitiesDocs
                .stream()
                .map(it -> DocumentParser.parseDocument(it, LocationedEntity.class))
                .collect(Collectors.toList());

        List<RuntimeEntity> runtimeEntities = new ArrayList<>();

        for (LocationedEntity locationedEntity : locationedEntities) {
            RuntimeEntity runtimeEntity = spawnEntity(locationedEntity);
            runtimeEntities.add(runtimeEntity);
        }

        List<Document> runtimeEntitiesDocs = runtimeEntities
                .stream()
                .map(RuntimeEntity::toInsertDocument)
                .collect(Collectors.toList());

        dbManager.insertDocuments(runtimeEntitiesDocs, CollectionType.RUNTIME_ENTITY);
    }

    public void unloadEntities() {
        List<RuntimeEntity> runtimeEntities = getRuntimeEntities();

        for (RuntimeEntity runtimeEntity : runtimeEntities) {
            removeEntityByUUID(runtimeEntity.getUuid());
        }

        List<ObjectId> runtimeEntitiesIds = runtimeEntities
                .stream()
                .map(RuntimeEntity::get_id)
                .collect(Collectors.toList());

        dbManager.deleteDocuments(runtimeEntitiesIds, CollectionType.RUNTIME_ENTITY);
    }

    public void removeEntity(Entity entity, RuntimeEntity runtimeEntity) {
        dbManager.deleteDocument(runtimeEntity.get_id(), CollectionType.RUNTIME_ENTITY);

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

    private UUID spawnGatheringEntity(Location location, GatheringEntity gatheringEntity) {
        ArmorStand armorStand = world.spawn(location, ArmorStand.class);

        armorStand.setCustomName(gatheringEntity.getTitle());
        armorStand.setCustomNameVisible(true);

        armorStand.setGravity(false);
        armorStand.setSmall(true);
        armorStand.setVisible(true);

        NBTEditor.setEntityPersistenceRequired(armorStand);

        return armorStand.getUniqueId();
    }

    private RuntimeEntity prepareRuntimeEntity(UUID uuid, ObjectId locationedEntityId) {
        RuntimeEntity runtimeEntity = new RuntimeEntity();
        runtimeEntity.setLocationedEntityId(locationedEntityId);
        runtimeEntity.setUuid(uuid);
        return runtimeEntity;
    }

    public <T extends BaseEntity> T getEntity(ObjectId entityId, Class<T> entityClass) {
        if (!entities.containsKey(entityId)) {
            Document entityDoc = dbManager.getDocumentById(entityId, CollectionType.ENTITIES);
            T entity = DocumentParser.parseDocument(entityDoc, entityClass);
            entities.put(entityId, entity);
        }

        return (T) entities.get(entityId);
    }

    public RuntimeEntity getRuntimeEntityByUUID(UUID uuid) {
        Document runtimeEntityDoc = dbManager.getDocument(new Document("uuid", uuid), CollectionType.RUNTIME_ENTITY);

        return DocumentParser.parseDocument(runtimeEntityDoc, RuntimeEntity.class);
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

    private List<RuntimeEntity> getRuntimeEntities() {
        List<Document> runtimeEntitiesDocs = dbManager.getDocuments(CollectionType.RUNTIME_ENTITY);
        return runtimeEntitiesDocs
                .stream()
                .map(it -> DocumentParser.parseDocument(it, RuntimeEntity.class))
                .collect(Collectors.toList());
    }
}
