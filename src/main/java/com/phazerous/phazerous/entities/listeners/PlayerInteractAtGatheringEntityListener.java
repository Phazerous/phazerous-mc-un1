package com.phazerous.phazerous.entities.listeners;

import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.entities.EntityTerminateManager;
import com.phazerous.phazerous.entities.enums.EntityType;
import com.phazerous.phazerous.entities.utils.EntityUtils;
import com.phazerous.phazerous.entities.runtime.models.RuntimeBaseEntity;
import com.phazerous.phazerous.entities.runtime.models.RuntimeGatheringEntity;
import com.phazerous.phazerous.entities.runtime.EntityRuntimeManager;
import com.phazerous.phazerous.utils.Scheduler;
import org.bson.Document;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.concurrent.atomic.AtomicInteger;


public class PlayerInteractAtGatheringEntityListener implements Listener {
    private final EntityTerminateManager entityTerminateManager;
    private final EntityRuntimeManager entityRuntimeManager;

    public PlayerInteractAtGatheringEntityListener(EntityTerminateManager entityTerminateManager, EntityRuntimeManager entityRuntimeManager) {
        this.entityTerminateManager = entityTerminateManager;
        this.entityRuntimeManager = entityRuntimeManager;
    }

    @EventHandler
    public void onPlayerInteractAtGatheringEntity(PlayerInteractAtEntityEvent event) {
        Entity entity = event.getRightClicked();

        Document runtimeEntityDoc = entityRuntimeManager.getRuntimeEntityDoc(entity.getUniqueId());
        if (runtimeEntityDoc == null) return;

        EntityType entityType = EntityUtils.getEntityType(runtimeEntityDoc);
        if (entityType != EntityType.GATHERING_ENTITY) return;

        Class<? extends RuntimeBaseEntity> runtimeEntityClass = entityType.getRuntimeEntityClass();
        RuntimeBaseEntity runtimeBaseEntity = DocumentParser.parseDocument(runtimeEntityDoc, runtimeEntityClass);

        Player player = event.getPlayer();

        handleGatheringEntity(entity, (RuntimeGatheringEntity) runtimeBaseEntity, player);
    }

    private void handleGatheringEntity(Entity entity, RuntimeGatheringEntity runtimeGatheringEntity, Player player) {
        Long hardness = runtimeGatheringEntity.getHardness();

        Scheduler scheduler = Scheduler.getInstance();
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
