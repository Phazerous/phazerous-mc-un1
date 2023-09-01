package com.phazerous.phazerous.entities;

import lombok.Getter;

@Getter
public enum EntityType {
    GATHERING_ENTITY(0),
    MOB_ENTITY(1);


    private final Integer entityType;

    EntityType(Integer entityType) {
        this.entityType = entityType;
    }

    public static EntityType fromInteger(Integer entityType) {
        for (EntityType type : EntityType.values()) {
            if (type.entityType.equals(entityType)) {
                return type;
            }
        }
        return null;
    }
}
