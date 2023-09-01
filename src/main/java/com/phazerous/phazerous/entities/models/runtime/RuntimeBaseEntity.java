package com.phazerous.phazerous.entities.models.runtime;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.UUID;

@Getter
@Setter
public class RuntimeBaseEntity {
    private ObjectId _id;

    private UUID uuid;
    private ObjectId locationedEntityId;
}
