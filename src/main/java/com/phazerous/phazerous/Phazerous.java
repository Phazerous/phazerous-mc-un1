package com.phazerous.phazerous;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Phazerous extends JavaPlugin implements Listener {
    private EntityManager entityManager;

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage("[Phazerous]: enabled.");

        initialize();

        entityManager.loadEntities();
    }

    private void initialize() {
        DBManager dbManager = new DBManager();

        EntityManager entityManager = new EntityManager(dbManager, getServer().getWorld("world"));
        this.entityManager = entityManager;

        ItemManager itemManager = new ItemManager(dbManager);

        Scheduler scheduler = new Scheduler(Bukkit.getScheduler(), this);
        GatheringUtils gatheringUtils = new GatheringUtils(scheduler, entityManager, itemManager);

        PlayerInteractAtEntityListener playerInteractAtEntityListener = new PlayerInteractAtEntityListener(entityManager, gatheringUtils);
        getServer().getPluginManager().registerEvents(playerInteractAtEntityListener, this);
    }

    @Override
    public void onDisable() {
        entityManager.unloadEntities();
    }
    
}