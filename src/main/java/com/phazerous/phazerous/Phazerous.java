package com.phazerous.phazerous;

import com.phazerous.phazerous.commands.CommandExecutor;
import com.phazerous.phazerous.listeners.PlayerInteractAtEntityListener;
import com.phazerous.phazerous.listeners.PlayerJoinListener;
import com.phazerous.phazerous.managers.*;
import com.phazerous.phazerous.utils.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

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
        GatheringManager gatheringManager = new GatheringManager(scheduler, entityManager, itemManager);

        EconomyManager economyManager = new EconomyManager(dbManager);

        initializeListeners(gatheringManager, economyManager);
        registerCommands(economyManager);
    }

    private void initializeListeners(GatheringManager gatheringManager, EconomyManager economyManager) {
        PlayerInteractAtEntityListener playerInteractAtEntityListener = new PlayerInteractAtEntityListener(entityManager, gatheringManager);
        PlayerJoinListener playerJoinListener = new PlayerJoinListener(economyManager);

        ArrayList<Listener> listeners = new ArrayList<>();
        listeners.add(playerInteractAtEntityListener);
        listeners.add(playerJoinListener);

        PluginManager pluginManager = getServer().getPluginManager();

        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, this);
        }
    }

    private void registerCommands(EconomyManager economyManager) {
        getCommand("bal").setExecutor(new CommandExecutor(economyManager));
    }

    @Override
    public void onDisable() {
        entityManager.unloadEntities();
    }
    
}