package com.phazerous.phazerous.entities;

import com.phazerous.phazerous.archtecture.AbstractModule;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.db.enums.CollectionType;
import com.phazerous.phazerous.economy.EconomyManager;
import com.phazerous.phazerous.entities.bosses.BossManager;
import com.phazerous.phazerous.entities.commands.SpawnEntityCommand;
import com.phazerous.phazerous.entities.listeners.MobDeathListener;
import com.phazerous.phazerous.entities.listeners.PlayerAttackCustomMobListener;
import com.phazerous.phazerous.entities.listeners.PlayerInteractAtGatheringEntityListener;
import com.phazerous.phazerous.entities.runtime.EntityRuntimeManager;
import com.phazerous.phazerous.items.ItemManager;

public class EntityModule extends AbstractModule {

    private final EntitySpawnManager entitySpawnManager;
    private final DBManager dbManager;

    public EntityModule(DBManager dbManager, ItemManager itemManager, EconomyManager economyManager) {
        this.dbManager = dbManager;

        EntityManager entityManager = new EntityManager(dbManager);

        EntityRuntimeManager entityRuntimeManager = new EntityRuntimeManager(dbManager);

        this.entitySpawnManager = new EntitySpawnManager(entityManager, entityRuntimeManager);
        entitySpawnManager.subscribe(entityRuntimeManager);

        BossManager bossManager = new BossManager(entitySpawnManager);
        entitySpawnManager.setBossManager(bossManager);


        EntityTerminateManager entityTerminateManager = new EntityTerminateManager(dbManager, entityManager, itemManager, entitySpawnManager, economyManager);


        PlayerAttackCustomMobListener playerAttackCustomMobListener = new PlayerAttackCustomMobListener(entityTerminateManager, entityRuntimeManager);
        playerAttackCustomMobListener.subscribe(bossManager);

        addListener(playerAttackCustomMobListener);
        addListener(new PlayerInteractAtGatheringEntityListener(entityTerminateManager, entityRuntimeManager));
        addListener(new MobDeathListener());

        addCommand(new SpawnEntityCommand(bossManager));
    }

    public void enable() {
        entitySpawnManager.despawnEntities();
        dbManager.clearCollection(CollectionType.RUNTIME_ENTITIES);
        
        entitySpawnManager.spawnEntities();
    }

    public void disable() {
        entitySpawnManager.despawnEntities();
        dbManager.clearCollection(CollectionType.RUNTIME_ENTITIES);
    }
}
