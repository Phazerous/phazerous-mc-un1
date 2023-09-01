package com.phazerous.phazerous.entities;

import com.phazerous.phazerous.entities.models.*;
import lombok.Getter;

@Getter
public enum EntityType {
    GATHERING_ENTITY(0, GatheringEntity.class, BaseRuntimeEntity.class), MOB_ENTITY(1, MobEntity.class, MobRuntimeEntity.class);


    private final Integer entityType;
    private final Class<? extends BaseEntity> entityClass;
    private final Class<? extends BaseRuntimeEntity> runtimeEntityClass;

    EntityType(Integer entityType, Class<? extends BaseEntity> entityClass, Class<? extends BaseRuntimeEntity> runtimeEntityClass) {
        this.entityType = entityType;
        this.entityClass = entityClass;
        this.runtimeEntityClass = runtimeEntityClass;
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
