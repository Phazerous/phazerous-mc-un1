package com.phazerous.phazerous.entities;

import com.phazerous.phazerous.db.enums.CollectionType;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.entities.bosses.AbstractBoss;
import com.phazerous.phazerous.entities.models.entities.BaseEntity;
import com.phazerous.phazerous.entities.models.entities.LocationedEntity;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class EntityManager {
    private static EntityManager entityManager;
    private final DBManager dbManager;

    private final HashMap<ObjectId, BaseEntity> entities = new HashMap<>();
    private final HashMap<String, AbstractBoss> bosses = new HashMap<>();

    public EntityManager(DBManager dbManager) {
        this.dbManager = dbManager;
        EntityManager.entityManager = this;
    }

    public List<LocationedEntity> getLocationedEntities() {
        return dbManager.getDocuments(CollectionType.LOCATIONED_ENTITIES).stream()
                .map(document -> DocumentParser.parseDocument(document, LocationedEntity.class))
                .collect(Collectors.toList());
    }

    public Document getEntityDoc(ObjectId entityId) {
        return dbManager.getDocument(entityId, CollectionType.ENTITIES);
    }

    public LocationedEntity getLocationedEntityById(ObjectId id) {
        Document locationedEntityDoc = dbManager.getDocument(id, CollectionType.LOCATIONED_ENTITIES);

        return DocumentParser.parseDocument(locationedEntityDoc, LocationedEntity.class);
    }
}
