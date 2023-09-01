package com.phazerous.phazerous.entities.models;

import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.UUID;

@Getter @Setter
public class BaseRuntimeEntity {
    private ObjectId _id;

    private UUID uuid;
    private ObjectId locationedEntityId;
}
