package com.phazerous.phazerous.entities.bosses.models;

import com.phazerous.phazerous.entities.EntitySpawnManager;
import com.phazerous.phazerous.entities.models.entities.BossEntity;
import com.phazerous.phazerous.entities.models.entities.LocationedEntity;
import org.bson.types.ObjectId;

import java.util.List;

public abstract class AbstractBossWithMinions extends AbstractBoss {
    protected List<ObjectId> minionsIds;

    public AbstractBossWithMinions(EntitySpawnManager spawnManager, LocationedEntity locationedEntity, BossEntity bossModel, List<ObjectId> minionsIds) {
        super(spawnManager, locationedEntity, bossModel);
        this.minionsIds = minionsIds;
    }
}
