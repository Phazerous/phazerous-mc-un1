package com.phazerous.phazerous.listeners;

import com.phazerous.phazerous.managers.EntityManager;
import com.phazerous.phazerous.managers.GatheringManager;
import com.phazerous.phazerous.dtos.EntityDto;
import com.phazerous.phazerous.dtos.LocationedEntityDto;
import com.phazerous.phazerous.dtos.RuntimeEntityDto;
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
        RuntimeEntityDto runtimeEntityDto = entityManager.getRuntimeEntityDtoByUUID(entity.getUniqueId());
        LocationedEntityDto locationedEntityDto = entityManager.getLocationedEntityDtoById(runtimeEntityDto.getLocationedEntityId());
        EntityDto entityDto = entityManager.getEntityDtoById(locationedEntityDto.getEntityId());

        if (entityDto.getType() == 0) {
            gatheringManager.gather(locationedEntityDto, entityDto, player, entity, runtimeEntityDto);
        }

    }
}
