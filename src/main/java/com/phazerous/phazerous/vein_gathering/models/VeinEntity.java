package com.phazerous.phazerous.vein_gathering.models;

import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class VeinEntity {
    private final ObjectId veinId;
    private final int entityId;

    public VeinEntity(ObjectId veinId, int entityId) {
        this.veinId = veinId;
        this.entityId = entityId;
    }
}
