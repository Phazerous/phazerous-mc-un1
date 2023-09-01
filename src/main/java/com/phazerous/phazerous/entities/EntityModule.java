package com.phazerous.phazerous.entities;

import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.entities.listeners.PlayerAttackEntityListener;
import com.phazerous.phazerous.entities.listeners.PlayerInteractAtEntityListener;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityModule {
    private final JavaPlugin plugin;
    private final EntityManager entityManager;

    public EntityModule(JavaPlugin javaPlugin, DBManager dbManager, World world) {
        this.plugin = javaPlugin;

        this.entityManager = new EntityManager(dbManager, world);

        registerListeners();
    }

    private void registerListeners() {
        PluginManager pluginManager = plugin
                .getServer()
                .getPluginManager();

//        pluginManager.registerEvents(new PlayerInteractAtEntityListener(entityManager, ), plugin);
        pluginManager.registerEvents(new PlayerAttackEntityListener(entityManager), plugin);
    }

    public void enable() {
        entityManager.loadEntities();
    }

    public void disable() {
        entityManager.unloadEntities();
    }
}
