package com.phazerous.phazerous;

import com.phazerous.phazerous.commands.CommandExecutor;
import com.phazerous.phazerous.gui.CustomInventoryManager;
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
        ScoreboardManager scoreboardManager = new ScoreboardManager(economyManager);
        CustomInventoryManager customInventoryManager = new CustomInventoryManager(dbManager, itemManager);

        initializeListeners(gatheringManager, economyManager, scoreboardManager);
        registerCommands(economyManager, scoreboardManager, customInventoryManager);
    }

    private void initializeListeners(GatheringManager gatheringManager, EconomyManager economyManager, ScoreboardManager scoreboardManager) {
        PlayerInteractAtEntityListener playerInteractAtEntityListener = new PlayerInteractAtEntityListener(entityManager, gatheringManager);
        PlayerJoinListener playerJoinListener = new PlayerJoinListener(economyManager, scoreboardManager);

        ArrayList<Listener> listeners = new ArrayList<>();
        listeners.add(playerInteractAtEntityListener);
        listeners.add(playerJoinListener);

        PluginManager pluginManager = getServer().getPluginManager();

        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, this);
        }
    }

    private void registerCommands(EconomyManager economyManager, ScoreboardManager scoreboardManager, CustomInventoryManager customInventoryManager) {
        getCommand("bal").setExecutor(new CommandExecutor(economyManager, scoreboardManager, customInventoryManager));
    }

    @Override
    public void onDisable() {
        entityManager.unloadEntities();
    }
    
}