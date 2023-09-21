package com.phazerous.phazerous.gathering.models;

import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class VeinEntity {
    private ObjectId veinId;
    private int entityId;

    public VeinEntity(ObjectId veinId, int entityId) {
        this.veinId = veinId;
        this.entityId = entityId;
    }
}
