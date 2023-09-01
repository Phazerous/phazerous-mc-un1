package com.phazerous.phazerous.entities;

import com.phazerous.phazerous.db.CollectionType;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.entities.models.BaseEntity;
import com.phazerous.phazerous.entities.models.runtime.RuntimeBaseEntity;
import com.phazerous.phazerous.entities.models.LocationedEntity;
import com.phazerous.phazerous.items.ItemManager;
import com.phazerous.phazerous.utils.Scheduler;
import org.bson.types.ObjectId;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EntityTerminateManager {
    private final DBManager dbManager;
    private final EntityManager entityManager;
    private final ItemManager itemManager;

    private final Scheduler scheduler;

    public EntityTerminateManager(DBManager dbManager, EntityManager entityManager, ItemManager itemManager, Scheduler scheduler) {
        this.dbManager = dbManager;
        this.entityManager = entityManager;
        this.itemManager = itemManager;
        this.scheduler = scheduler;
    }

    /**
     * Terminates entity, schedule respawn and give drops to player
     *
     * @param entity
     * @param runtimeBaseEntity
     * @param player
     */
    public void terminateEntity(Entity entity, RuntimeBaseEntity runtimeBaseEntity, Player player) {
        dbManager.deleteDocument(runtimeBaseEntity.get_id(), CollectionType.RUNTIME_ENTITY);
//        ((LivingEntity) entity).setHealth(0);
        entity.remove();

        LocationedEntity locationedEntity = entityManager.getLocationedEntityById(runtimeBaseEntity.getLocationedEntityId());
        BaseEntity entityModel = entityManager.getEntity(locationedEntity.getEntityId(), BaseEntity.class);

        List<ObjectId> dropsIds = entityModel.getDropsIds();
        ItemStack[] drops = itemManager
                .getItemsByIds(dropsIds)
                .toArray(new ItemStack[0]);

        player
                .getInventory()
                .addItem(drops);

        scheduleEntityRespawn(locationedEntity, entityModel.getRespawnTime());
    }

    private void scheduleEntityRespawn(LocationedEntity locationedEntity, Long respawnTime) {
        scheduler.runTaskLater(() -> {
            entityManager.spawnEntityAndInsert(locationedEntity);
        }, respawnTime);
    }
}
