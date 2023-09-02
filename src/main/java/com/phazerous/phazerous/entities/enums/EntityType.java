package com.phazerous.phazerous.entities.enums;

import com.phazerous.phazerous.entities.models.entities.BaseEntity;
import com.phazerous.phazerous.entities.models.entities.BossEntity;
import com.phazerous.phazerous.entities.models.entities.GatheringEntity;
import com.phazerous.phazerous.entities.models.entities.MobEntity;
import com.phazerous.phazerous.entities.runtime.models.RuntimeBaseEntity;
import com.phazerous.phazerous.entities.runtime.models.RuntimeGatheringEntity;
import com.phazerous.phazerous.entities.runtime.models.RuntimeMobEntity;
import lombok.Getter;

@Getter
public enum EntityType {
    GATHERING_ENTITY(0, GatheringEntity.class, RuntimeGatheringEntity.class), MOB_ENTITY(1, MobEntity.class, RuntimeMobEntity.class), BOSS_ENTITY(2, BossEntity.class, RuntimeMobEntity.class);


    private final Integer entityType;
    private final Class<? extends BaseEntity> entityClass;
    private final Class<? extends RuntimeBaseEntity> runtimeEntityClass;

    EntityType(Integer entityType, Class<? extends BaseEntity> entityClass, Class<? extends RuntimeBaseEntity> runtimeEntityClass) {
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
