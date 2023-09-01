package com.phazerous.phazerous.listeners;

import com.phazerous.phazerous.entities.EntityManager;
import com.phazerous.phazerous.entities.models.BaseEntity;
import com.phazerous.phazerous.entities.models.GatheringEntity;
import com.phazerous.phazerous.entities.models.LocationedEntity;
import com.phazerous.phazerous.entities.models.BaseRuntimeEntity;
import com.phazerous.phazerous.managers.GatheringManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

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
        BaseRuntimeEntity baseRuntimeEntity = entityManager.getRuntimeEntityByUUID(entity.getUniqueId());
        LocationedEntity locationedEntity = entityManager.getLocationedEntityById(baseRuntimeEntity.getLocationedEntityId());

        BaseEntity entityDto = entityManager.getEntity(locationedEntity.getEntityId(), BaseEntity.class);

        if (entityDto.getEntityType() == 0) {
            gatheringManager.gather(locationedEntity, (GatheringEntity) entityDto, player, entity, baseRuntimeEntity);
        }

    }
}
