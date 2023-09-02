package com.phazerous.phazerous.entities;

import com.phazerous.phazerous.archtecture.AbstractModule;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.entities.listeners.MobDeathListener;
import com.phazerous.phazerous.entities.listeners.PlayerAttackCustomMobListener;
import com.phazerous.phazerous.entities.listeners.PlayerInteractAtGatheringEntityListener;
import com.phazerous.phazerous.entities.runtime.EntityRuntimeManager;
import com.phazerous.phazerous.items.ItemManager;

public class EntityModule extends AbstractModule {

    private final EntitySpawnManager entitySpawnManager;

    public EntityModule(DBManager dbManager, ItemManager itemManager) {
        EntityManager entityManager = new EntityManager(dbManager);

        EntityRuntimeManager entityRuntimeManager = new EntityRuntimeManager(dbManager);

        this.entitySpawnManager = new EntitySpawnManager(entityManager, entityRuntimeManager);
        entitySpawnManager.subscribe(entityRuntimeManager);


        EntityTerminateManager entityTerminateManager = new EntityTerminateManager(dbManager, entityManager, itemManager, entitySpawnManager);

        addListener(new PlayerAttackCustomMobListener(entityTerminateManager, entityRuntimeManager));
        addListener(new PlayerInteractAtGatheringEntityListener(entityTerminateManager, entityRuntimeManager));
        addListener(new MobDeathListener());
    }

    public void enable() {
        entitySpawnManager.despawnEntities();
        entitySpawnManager.spawnEntities();
    }

    public void disable() {
        entitySpawnManager.despawnEntities();
    }
}
