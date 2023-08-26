package com.phazerous.phazerous;

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
    private final GatheringUtils gatheringUtils;

    public PlayerInteractAtEntityListener(EntityManager entityManager, GatheringUtils gatheringUtils) {
        this.entityManager = entityManager;
        this.gatheringUtils = gatheringUtils;
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        Entity entity = event.getRightClicked();
        RuntimeEntityDto runtimeEntityDto = entityManager.getRuntimeEntityDtoByUUID(entity.getUniqueId());
        LocationedEntityDto locationedEntityDto = entityManager.getLocationedEntityDtoById(runtimeEntityDto.getLocationedEntityId());
        EntityDto entityDto = entityManager.getEntityDtoById(locationedEntityDto.getEntityId());

        if (entityDto.getType() == 0) {
            gatheringUtils.gather(locationedEntityDto, entityDto, player, entity, runtimeEntityDto);
        }

    }
}
