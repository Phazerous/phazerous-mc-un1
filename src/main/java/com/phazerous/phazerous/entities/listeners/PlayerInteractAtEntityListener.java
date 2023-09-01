package com.phazerous.phazerous.entities.listeners;

import com.phazerous.phazerous.db.DocumentParser;
import com.phazerous.phazerous.entities.EntityManager;
import com.phazerous.phazerous.entities.EntityTerminateManager;
import com.phazerous.phazerous.entities.EntityType;
import com.phazerous.phazerous.entities.EntityUtils;
import com.phazerous.phazerous.entities.models.runtime.RuntimeBaseEntity;
import com.phazerous.phazerous.entities.models.runtime.RuntimeGatheringEntity;
import com.phazerous.phazerous.utils.Scheduler;
import org.bson.Document;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.concurrent.atomic.AtomicInteger;


public class PlayerInteractAtEntityListener implements Listener {

    private final EntityManager entityManager;
    private final EntityTerminateManager entityTerminateManager;
    private final Scheduler scheduler;

    public PlayerInteractAtEntityListener(EntityManager entityManager, EntityTerminateManager entityTerminateManager, Scheduler scheduler) {
        this.entityManager = entityManager;
        this.entityTerminateManager = entityTerminateManager;
        this.scheduler = scheduler;
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        Entity entity = event.getRightClicked();
        Document runtimeEntityDoc = entityManager.getRuntimeEntityDocByUUID(entity.getUniqueId());
        EntityType entityType = EntityUtils.getRuntimeEntityType(runtimeEntityDoc);

        if (runtimeEntityDoc == null) return;

        Class<? extends RuntimeBaseEntity> runtimeEntityClass = entityType.getRuntimeEntityClass();
        RuntimeBaseEntity runtimeBaseEntity = DocumentParser.parseDocument(runtimeEntityDoc, runtimeEntityClass);

        if (entityType == EntityType.GATHERING_ENTITY) {
            handleGatheringEntity(entity, (RuntimeGatheringEntity) runtimeBaseEntity, player);
        }
    }

    private void handleGatheringEntity(Entity entity, RuntimeGatheringEntity runtimeGatheringEntity, Player player) {
        Long hardness = runtimeGatheringEntity.getHardness();

        AtomicInteger secondsCounter = new AtomicInteger();

        int gatherIntervalId = scheduler.runInterval(() -> {
            player.playSound(player.getLocation(), Sound.ENDERMAN_HIT, 5, 5);

            int seconds = secondsCounter.incrementAndGet();
            float percentage = (float) (seconds * 20) / hardness;

            EntityUtils.setGatheringEntityTitle(entity, percentage);
        });

        scheduler.runTaskLater(() -> {
            player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 5, 5);
            scheduler.cancelTask(gatherIntervalId);

            entityTerminateManager.terminateEntity(entity, runtimeGatheringEntity, player);
        }, hardness);
    }

}
