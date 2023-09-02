package com.phazerous.phazerous.entities.bosses;

import com.phazerous.phazerous.entities.EntityManager;
import com.phazerous.phazerous.utils.NBTEditor;
import com.phazerous.phazerous.utils.Scheduler;
import org.bson.types.ObjectId;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BossZombieKing extends AbstractBoss {
    private int intervalId = -1;

    public BossZombieKing(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void activateSkill() {
        Scheduler scheduler = Scheduler.getInstance();

        scheduler.runTaskLater(this::moveBossToCenter, 20L);
        scheduler.runTaskLater(this::moveBossUp, 40L);
        scheduler.runTaskLater(this::bossFreeze, 60L);
        scheduler.runTaskLater(() -> {
            List<Entity> minions = spawnMinions();
            intervalId = scheduler.runInterval(() -> {
                if (isAllMinionsDead(minions)) {
                    bossUnfreeze();
                    scheduler.cancelTask(intervalId);
                }
            }, 100L);
        }, 80L);
    }

    private void moveBossToCenter() {
        Location currentLocation = boss.getLocation();
        Location targetLocation = location;

        Vector direction = targetLocation.toVector().subtract(currentLocation.toVector()).normalize();

        boss.setVelocity(direction.multiply(2));
    }

    private void moveBossUp() {
        Vector upwardVector = new Vector(0, 1, 0);
        boss.setVelocity(upwardVector.multiply(2));
    }

    private void bossFreeze() {
        NBTEditor.setAI(boss, false);
    }

    private List<Entity> spawnMinions() {
        List<Entity> spawnedMinions = new ArrayList<>();

        for (ObjectId minionId : minions) {
//            spawnedMinions.add(entityManager.spawnEntity(minionId));
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
    }
}
