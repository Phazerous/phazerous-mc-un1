package com.phazerous.phazerous.entities.models.entities;

import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
public class BossEntity extends MobEntity {
    private List<ObjectId> minionsIds;
}
