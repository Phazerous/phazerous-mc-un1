package com.phazerous.phazerous.entities.bosses.models;

import com.phazerous.phazerous.entities.EntitySpawnManager;
import com.phazerous.phazerous.entities.models.entities.BossEntity;
import com.phazerous.phazerous.entities.models.entities.LocationedEntity;
import com.phazerous.phazerous.entities.utils.EntityUtils;
import com.phazerous.phazerous.utils.NBTEditor;
import org.bson.types.ObjectId;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BossZombieKing extends AbstractBossWithMinions {
    private final int intervalId = -1;
    private final boolean isSpecialAbilityActivated = false;

    public BossZombieKing(EntitySpawnManager entitySpawnManager, LocationedEntity locationedEntity, BossEntity bossModel, List<ObjectId> minionsIds) {
        super(entitySpawnManager, locationedEntity, bossModel, minionsIds);
    }

    @Override
    public void handleDamage(UUID entityUUID, Double damage, Long health) {
        if (!isSpecialAbilityActivated && health <= bossModel.getMaxHealth() / 2) {
            activateSkill();
        }

    }

    public void activateSkill() {
//        isSpecialAbilityActivated = true;
//
//        Scheduler scheduler = Scheduler.getInstance();
//
//        scheduler.runTaskLater(this::moveBossToCenter, 20L);
//        scheduler.runTaskLater(this::moveBossUp, 40L);
//        scheduler.runTaskLater(this::bossFreeze, 60L);
//        scheduler.runTaskLater(() -> {
//            List<Entity> minions = spawnMinions();
//            intervalId = scheduler.runInterval(() -> {
//                if (isAllMinionsDead(minions)) {
//                    bossUnfreeze();
//                    scheduler.cancelTask(intervalId);
//                }
//            }, 100L);
//        }, 80L);
    }

    private void moveBossToCenter() {
        Location currentLocation = boss.getLocation();
        Location targetLocation = EntityUtils.getEntityLocation(locationedEntity);

        Vector direction = targetLocation.toVector().subtract(currentLocation.toVector())
                .normalize();

        boss.setVelocity(direction.multiply(1));
    }

    private void moveBossUp() {
        Vector upwardVector = new Vector(0, 1, 0);
        boss.setVelocity(upwardVector.multiply(1.2));
    }

    private void bossFreeze() {
        NBTEditor.setAI(boss, false);
        NBTEditor.setInvulnerable(boss, true);
    }

    private List<Entity> spawnMinions() {
        List<Entity> spawnedMinions = new ArrayList<>();

        for (ObjectId minionId : minionsIds) {
            spawnedMinions.add(spawnManager.spawnEntity(minionId));
        }

        return spawnedMinions;
    }

    private boolean isAllMinionsDead(List<Entity> minions) {
        for (Entity minion : minions) {
            if (!(minion.isDead())) {
                return false;
            }
        }
        return true;
    }

    private void bossUnfreeze() {
        NBTEditor.setAI(boss, true);
        NBTEditor.setInvulnerable(boss, false);
    }
}
