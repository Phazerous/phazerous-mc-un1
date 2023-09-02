package com.phazerous.phazerous.entities.bosses.models;

import com.phazerous.phazerous.entities.EntitySpawnManager;
import com.phazerous.phazerous.entities.interfaces.IEntityDamageSubscriber;
import com.phazerous.phazerous.entities.models.entities.BossEntity;
import com.phazerous.phazerous.entities.models.entities.LocationedEntity;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public abstract class AbstractBoss implements IEntityDamageSubscriber {
    protected EntitySpawnManager spawnManager;
    protected LocationedEntity locationedEntity;
    protected BossEntity bossModel;

    @Getter
    protected LivingEntity boss;

    public AbstractBoss(EntitySpawnManager spawnManager, LocationedEntity locationedEntity, BossEntity bossModel) {
        this.spawnManager = spawnManager;
        this.locationedEntity = locationedEntity;
        this.bossModel = bossModel;
    }

    public void spawnBoss() {
        boss = (LivingEntity) spawnManager.spawnBoss(locationedEntity, bossModel);
    }

    public void handleDamage(UUID entityUUID, Double damage, Long health) {
    }
}
