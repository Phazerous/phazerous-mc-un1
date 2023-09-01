package com.phazerous.phazerous.entities.listeners;

import com.phazerous.phazerous.db.DocumentParser;
import com.phazerous.phazerous.entities.EntityManager;
import com.phazerous.phazerous.entities.EntityType;
import com.phazerous.phazerous.entities.EntityUtils;
import com.phazerous.phazerous.entities.models.BaseEntity;
import com.phazerous.phazerous.entities.models.GatheringEntity;
import com.phazerous.phazerous.entities.models.LocationedEntity;
import com.phazerous.phazerous.entities.models.BaseRuntimeEntity;
import com.phazerous.phazerous.managers.GatheringManager;
import org.bson.Document;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.UUID;


public class PlayerInteractAtEntityListener implements Listener {

    private final EntityManager entityManager;
    private final GatheringManager gatheringManager;

    public PlayerInteractAtEntityListener(EntityManager entityManager, GatheringManager gatheringManager) {
        this.entityManager = entityManager;
        this.gatheringManager = gatheringManager;
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        Entity entity = event.getRightClicked();
        Document runtimeEntityDoc = entityManager.getRuntimeEntityDocByUUID(entity.getUniqueId());
        EntityType entityType = EntityUtils.getRuntimeEntityType(runtimeEntityDoc);

        if (runtimeEntityDoc == null) return;

        Class<? extends BaseRuntimeEntity> runtimeEntityClass = entityType.getRuntimeEntityClass();
        BaseRuntimeEntity baseRuntimeEntity = DocumentParser.parseDocument(runtimeEntityDoc, runtimeEntityClass);

        LocationedEntity locationedEntity = entityManager.getLocationedEntityById(baseRuntimeEntity.getLocationedEntityId());

        BaseEntity entityDto = entityManager.getEntity(locationedEntity.getEntityId(), BaseEntity.class);

        if (entityType == EntityType.GATHERING_ENTITY) {
            gatheringManager.gather(locationedEntity, (GatheringEntity) entityDto, player, entity, baseRuntimeEntity);
        }

    }
}
