package com.phazerous.phazerous;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Phazerous extends JavaPlugin implements Listener {
    private EntityManager entityManager;

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage("[Phazerous]: enabled.");

        getServer().getPluginManager().registerEvents(this, this);

        DBManager dbManager = new DBManager();
        entityManager = new EntityManager(dbManager, getServer().getWorld("world"));


        entityManager.loadEntities();
    }

    @Override
    public void onDisable() {
        entityManager.unloadEntities();
    }
    
}