package com.phazerous.phazerous.entities.runtime;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.phazerous.phazerous.db.enums.CollectionType;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.entities.interfaces.IEntitySpawnSubscriber;
import com.phazerous.phazerous.entities.models.entities.BaseEntity;
import com.phazerous.phazerous.entities.models.entities.GatheringEntity;
import com.phazerous.phazerous.entities.models.entities.MobEntity;
import com.phazerous.phazerous.entities.runtime.models.RuntimeBaseEntity;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class EntityRuntimeManager implements IEntitySpawnSubscriber {
    private final DBManager dbManager;

    public EntityRuntimeManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public Document getRuntimeEntityDoc(UUID uuid) {
        Bson query = Filters.eq("uuid", uuid);

        return dbManager.getDocument(query, CollectionType.RUNTIME_ENTITIES);
    }

    public List<RuntimeBaseEntity> getRuntimeEntities() {
        return dbManager.getDocuments(CollectionType.RUNTIME_ENTITIES).stream()
                .map(document -> DocumentParser.parse(document, RuntimeBaseEntity.class))
                .collect(Collectors.toList());
    }

    public void handleHealthChange(ObjectId runtimeEntityId, Long health) {
        Bson update = Updates.set("health", health);

        dbManager.updateOne(update, runtimeEntityId, CollectionType.RUNTIME_ENTITIES);
    }

    @Override
    public void handleSpawn(UUID uuid, BaseEntity baseEntity, ObjectId locationedEntityId) {
        Document document = new Document();

        document.append("uuid", uuid);
        document.append("entityType", baseEntity.getEntityType());
        document.append("locationedEntityId", locationedEntityId);

        if (baseEntity instanceof GatheringEntity) {
            GatheringEntity gatheringEntity = (GatheringEntity) baseEntity;
            document.append("hardness", gatheringEntity.getHardness());
        } else if (baseEntity instanceof MobEntity) {
            MobEntity mobEntity = (MobEntity) baseEntity;
            document.append("health", mobEntity.getMaxHealth());
            document.append("title", mobEntity.getTitle());
            document.append("maxHealth", mobEntity.getMaxHealth());
            document.append("attack", mobEntity.getAttack());
        }

        dbManager.insertDocument(document, CollectionType.RUNTIME_ENTITIES);
    }

    @Override
    public void handleDespawn(UUID entityUUID) {
        Bson query = Filters.eq("uuid", entityUUID);

        dbManager.deleteDocument(query, CollectionType.RUNTIME_ENTITIES);
    }
}
