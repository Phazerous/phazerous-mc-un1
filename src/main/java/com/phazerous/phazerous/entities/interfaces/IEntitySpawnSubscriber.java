package com.phazerous.phazerous.entities.interfaces;

import com.phazerous.phazerous.entities.models.entities.BaseEntity;
import org.bson.types.ObjectId;

import java.util.UUID;

public interface IEntitySpawnSubscriber {
    void handleSpawn(UUID entityUUID, BaseEntity entity, ObjectId locationedEntityId);

    void handleDespawn(UUID entityUUID);
}
