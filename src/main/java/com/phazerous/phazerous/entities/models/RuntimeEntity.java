package com.phazerous.phazerous.entities.models;

import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.UUID;

@Getter @Setter
public class RuntimeEntity {
    private ObjectId _id;

    private ObjectId locationedEntityId;

    private UUID uuid;

    public static Document toInsertDocument(RuntimeEntity runtimeEntity) {
        Document document = new Document();
        document.append("uuid", runtimeEntity.getUuid());
        document.append("locationedEntityId", runtimeEntity.getLocationedEntityId());
        return document;
    }
}
