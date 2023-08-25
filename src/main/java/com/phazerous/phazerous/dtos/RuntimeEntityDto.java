package com.phazerous.phazerous.dtos;

import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.UUID;

@Getter @Setter
public class RuntimeEntityDto {
    private ObjectId id;
    private ObjectId locationedEntityId;

    private UUID uuid;

    public static Document toInsertDocument(RuntimeEntityDto runtimeEntityDto) {
        Document document = new Document();
        document.append("uuid", runtimeEntityDto.getUuid());
        document.append("locationedEntityId", runtimeEntityDto.getLocationedEntityId());
        return document;
    }
}
