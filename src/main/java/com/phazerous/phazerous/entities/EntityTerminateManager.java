package com.phazerous.phazerous.entities;

import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.db.enums.CollectionType;
import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.economy.EconomyManager;
import com.phazerous.phazerous.entities.enums.EntityType;
import com.phazerous.phazerous.entities.models.entities.BaseEntity;
import com.phazerous.phazerous.entities.models.entities.LocationedEntity;
import com.phazerous.phazerous.entities.models.entities.MobEntity;
import com.phazerous.phazerous.entities.models.misc.EntityDrop;
import com.phazerous.phazerous.entities.models.misc.MoneyReward;
import com.phazerous.phazerous.entities.runtime.models.RuntimeBaseEntity;
import com.phazerous.phazerous.entities.utils.EntityUtils;
import com.phazerous.phazerous.items.ItemManager;
import org.bson.Document;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EntityTerminateManager {
    private final DBManager dbManager;
    private final EntityManager entityManager;
    private final ItemManager itemManager;
    private final EconomyManager economyManager;
    private final EntitySpawnManager entitySpawnManager;

    public EntityTerminateManager(DBManager dbManager, EntityManager entityManager, ItemManager itemManager, EntitySpawnManager entitySpawnManager, EconomyManager economyManager) {
        this.dbManager = dbManager;
        this.entityManager = entityManager;
        this.itemManager = itemManager;
        this.entitySpawnManager = entitySpawnManager;
        this.economyManager = economyManager;
    }

    /**
     * Terminates entity, schedule respawn and give drops to player
     *
     * @param entity
     * @param runtimeBaseEntity
     * @param player
     */
    public void terminateEntity(Entity entity, RuntimeBaseEntity runtimeBaseEntity, Player player) {
        dbManager.deleteDocument(runtimeBaseEntity.get_id(), CollectionType.RUNTIME_ENTITIES);
        entity.remove();

        LocationedEntity locationedEntity = entityManager.getLocationedEntityById(runtimeBaseEntity.getLocationedEntityId());

        Document entityDoc = entityManager.getEntityDoc(locationedEntity.getEntityId());
        EntityType entityType = EntityUtils.getEntityType(entityDoc);
        BaseEntity entityModel = DocumentParser.parse(entityDoc, entityType.getEntityClass());
        List<EntityDrop> dropsIds = entityModel.getDrops();

        handleDropItems(dropsIds, player);

        if (entityModel instanceof MobEntity) handleMoneyReward(player, (MobEntity) entityModel);

        scheduleEntityRespawn(locationedEntity, entityModel.getRespawnTime());
    }

    private void handleDropItems(List<EntityDrop> drops, Player player) {
        drops.forEach(drop -> {
            Double dropChance = drop.getDropChance();

            if (dropChance == null || dropChance <= Math.random()) {
                ItemStack itemStack = itemManager.getItemById(drop.getItemId());
                player.getInventory().addItem(itemStack);
            }
        });
    }

    private void handleMoneyReward(Player player, MobEntity mobEntity) {
        MoneyReward moneyReward = mobEntity.getMoneyReward();

        if (moneyReward == null) return;

        Long moneyMin = moneyReward.getMin();
        Long moneyMax = mobEntity.getMoneyReward().getMax();

        if (moneyMin == null || moneyMax == null) return;

        Long money = moneyMin + Math.round(Math.random() * (moneyMax - moneyMin));

        economyManager.deposit(player.getUniqueId(), money);
    }

    private void scheduleEntityRespawn(LocationedEntity locationedEntity, Long respawnTime) {
//        Scheduler scheduler = Scheduler.getInstance();
//        scheduler.runTaskLater(() -> {
//            entitySpawnManager.spawnEntity(locationedEntity);
//        }, respawnTime);
    }
}
