package com.phazerous.phazerous.entities.bosses;

import com.phazerous.phazerous.entities.EntityManager;
import com.phazerous.phazerous.entities.models.entities.BossEntity;
import com.phazerous.phazerous.utils.NBTEditor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.List;

@Setter
public class AbstractBoss {
    protected EntityManager entityManager;
    protected Location location;
    protected LivingEntity boss;
    protected BossEntity bossModel;
    protected List<ObjectId> minions;

    public void spawnBoss() {
        LivingEntity bossEntity = (LivingEntity) entityManager.spawnMobEntity(location, bossModel);
        this.boss = bossEntity;
    }
}
