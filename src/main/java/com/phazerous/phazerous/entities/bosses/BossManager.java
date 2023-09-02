package com.phazerous.phazerous.entities.bosses;

import com.phazerous.phazerous.entities.EntitySpawnManager;
import com.phazerous.phazerous.entities.bosses.enums.BossType;
import com.phazerous.phazerous.entities.bosses.models.AbstractBoss;
import com.phazerous.phazerous.entities.bosses.models.BossZombieKing;
import com.phazerous.phazerous.entities.interfaces.IEntityDamageSubscriber;
import com.phazerous.phazerous.entities.models.entities.BossEntity;
import com.phazerous.phazerous.entities.models.entities.LocationedEntity;
import org.bukkit.entity.LivingEntity;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BossManager implements IEntityDamageSubscriber {
    private final HashMap<BossType, AbstractBoss> bosses = new HashMap<>();

    private final EntitySpawnManager entitySpawnManager;

    public BossManager(EntitySpawnManager entitySpawnManager) {
        this.entitySpawnManager = entitySpawnManager;
    }

    public void spawnBoss(BossType bossType) {
        AbstractBoss boss = bosses.get(bossType);
        boss.spawnBoss();
    }

    public void saveBossModel(LocationedEntity locationedEntity, BossEntity bossModel) {
        String bossTitle = bossModel.getTitle();

        if (bossTitle.equalsIgnoreCase(BossType.ZOMBIE_KING.getName())) {
            BossZombieKing bossZombieKing = new BossZombieKing(entitySpawnManager, locationedEntity, bossModel, bossModel.getMinionsIds());
            bosses.put(BossType.ZOMBIE_KING, bossZombieKing);
        }
    }

    @Override
    public void handleDamage(UUID entityUUID, Double damage, Long health) {
        List<AbstractBoss> concreteBosses = new ArrayList<>(bosses.values());

        for (AbstractBoss boss : concreteBosses) {
            LivingEntity bossEntity = boss.getBoss();

            if (bossEntity == null || bossEntity.isDead()) continue;

            UUID bossUUID = bossEntity.getUniqueId();

            if (bossUUID == entityUUID) {
                boss.handleDamage(entityUUID, damage, health);
                return;
            }
        }
    }
}
