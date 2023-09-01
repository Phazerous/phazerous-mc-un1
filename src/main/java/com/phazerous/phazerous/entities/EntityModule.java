package com.phazerous.phazerous.entities;

import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.entities.listeners.PlayerAttackEntityListener;
import com.phazerous.phazerous.entities.listeners.PlayerInteractAtEntityListener;
import com.phazerous.phazerous.managers.ItemManager;
import com.phazerous.phazerous.utils.Scheduler;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityModule {
    private final JavaPlugin plugin;
    private final EntityManager entityManager;
    private final EntityTerminateManager entityTerminateManager;
    private final Scheduler scheduler;

    public EntityModule(JavaPlugin javaPlugin, DBManager dbManager, World world, ItemManager itemManager, Scheduler scheduler) {
        this.plugin = javaPlugin;
        this.scheduler = scheduler;

        this.entityManager = new EntityManager(dbManager, world);
        entityTerminateManager = new EntityTerminateManager(dbManager, entityManager, itemManager, scheduler);

        registerListeners();
    }

    private void registerListeners() {
        PluginManager pluginManager = plugin
                .getServer()
                .getPluginManager();

        pluginManager.registerEvents(new PlayerInteractAtEntityListener(entityManager, entityTerminateManager, scheduler), plugin);
        pluginManager.registerEvents(new PlayerAttackEntityListener(entityManager, entityTerminateManager), plugin);
    }

    public void enable() {
        entityManager.loadEntities();
    }

    public void disable() {
        entityManager.unloadEntities();
    }
}
