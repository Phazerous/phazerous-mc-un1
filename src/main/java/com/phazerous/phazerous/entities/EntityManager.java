package com.phazerous.phazerous.entities;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.phazerous.phazerous.db.CollectionType;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.db.utils.DocumentBuilder;
import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.entities.bosses.AbstractBoss;
import com.phazerous.phazerous.entities.bosses.BossZombieKing;
import com.phazerous.phazerous.entities.models.entities.*;
import com.phazerous.phazerous.entities.models.runtime.RuntimeBaseEntity;
import com.phazerous.phazerous.entities.models.runtime.RuntimeGatheringEntity;
import com.phazerous.phazerous.entities.models.runtime.RuntimeMobEntity;
import com.phazerous.phazerous.utils.NBTEditor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import javax.swing.*;
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
    private final HashMap<String, AbstractBoss> bosses = new HashMap<>();

    public EntityManager(DBManager dbManager, World world) {
        this.dbManager = dbManager;
        this.world = world;
        EntityManager.entityManager = this;
    }

    public Entity spawnEntity(ObjectId locationedEntity) {
        LocationedEntity locationedEntityModel = getLocationedEntityById(locationedEntity);
        return spawnEntity(locationedEntityModel);
    }

    public Entity spawnEntity(LocationedEntity locationedEntity) {
        final String ENTITY_TYPE_NAME = "entityType";

        Location entityLocation = EntityUtils.getEntityLocation(locationedEntity);
        ObjectId entityId = locationedEntity.getEntityId();
        Document entityDoc = dbManager.getDocumentById(entityId, CollectionType.ENTITIES);
        Integer entityTypeCode = entityDoc.getInteger(ENTITY_TYPE_NAME);
        EntityType entityType = EntityType.fromInteger(entityTypeCode);

        Entity entity = null;


//        if (entityType == EntityType.GATHERING_ENTITY) {
//            Document runtimeEntityDoc = null;

//            GatheringEntity gatheringEntity = getEntity(entityId, GatheringEntity.class);
//            runtimeEntityDoc = spawnGatheringEntity(entityLocation, gatheringEntity);

//            return runtimeEntityDoc
//                    .append("locationedEntityId", locationedEntity.get_id())
//                    .append("entityType", entityTypeCode);
        if (entityType == EntityType.MOB_ENTITY) {
            MobEntity mobEntity = getEntity(entityId, MobEntity.class);
            entity = spawnMobEntity(entityLocation, mobEntity);
        } else if (entityType == EntityType.BOSS_ENTITY) {
            BossEntity bossEntity = getEntity(entityId, BossEntity.class);

            BossZombieKing abstractBoss = new BossZombieKing(this);
            abstractBoss.setBossModel(bossEntity);
            abstractBoss.setLocation(entityLocation);
            abstractBoss.setMinions(bossEntity.getMinions());

            bosses.put("ZOMBIE_KING", abstractBoss);
        }

        return entity;
    }

    public void respawnEntity(LocationedEntity locationedEntity) {
//        Document runtimeEntityDoc = spawnEntity(locationedEntity);
//
//        dbManager.insertDocument(runtimeEntityDoc, CollectionType.RUNTIME_ENTITY);
    }

    public void loadEntities() {
        List<Document> locationedEntitiesDocs = dbManager.getDocuments(CollectionType.LOCATIONED_ENTITIES);
        List<LocationedEntity> locationedEntities = locationedEntitiesDocs
                .stream()
                .map(it -> DocumentParser.parseDocument(it, LocationedEntity.class))
                .collect(Collectors.toList());

        List<Document> runtimeEntitiesDocs = new ArrayList<>();

        for (LocationedEntity locationedEntity : locationedEntities) {
            spawnEntity(locationedEntity);
        }

//        dbManager.insertDocuments(runtimeEntitiesDocs, CollectionType.RUNTIME_ENTITY);
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
    public Entity spawnMobEntity(Location location, MobEntity mobEntity) {
        org.bukkit.entity.EntityType mobType = org.bukkit.entity.EntityType.fromId(mobEntity.getMobType());
        Class<? extends Entity> mobClass = mobType.getEntityClass();

        Entity mob = world.spawn(location, mobClass);

        EntityUtils.setMobEntityHPTitle(mob, mobEntity.getTitle(), mobEntity.getMaxHealth(), mobEntity.getMaxHealth());
        mob.setCustomNameVisible(true);

        NBTEditor.setEntityPersistenceRequired(mob);


        if (mob instanceof LivingEntity) {
            ((LivingEntity) mob)
                    .getEquipment()
                    .setHelmet(new ItemStack(Material.STONE_BUTTON));
        }

        UUID uuid = mob.getUniqueId();

        RuntimeMobEntity mobRuntimeEntity = new RuntimeMobEntity();
        mobRuntimeEntity.setUuid(uuid);
        mobRuntimeEntity.setHealth(mobEntity.getMaxHealth());
        mobRuntimeEntity.setMaxHealth(mobEntity.getMaxHealth());
        mobRuntimeEntity.setTitle(mobEntity.getTitle());

        Document mobRuntimeEntityDoc = DocumentBuilder.buildDocument(mobRuntimeEntity);
        dbManager.insertDocument(mobRuntimeEntityDoc, CollectionType.RUNTIME_ENTITY);

        return mob;
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
//        Document runtimeEntityDoc = spawnEntity(locationedEntity);
//        dbManager.insertDocument(runtimeEntityDoc, CollectionType.RUNTIME_ENTITY);
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

    public AbstractBoss getBoss() {
        return bosses.get("ZOMBIE_KING");
    }
}
