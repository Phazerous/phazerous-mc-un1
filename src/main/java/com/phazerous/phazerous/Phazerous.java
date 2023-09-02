package com.phazerous.phazerous;

import com.phazerous.phazerous.commands.CommandExecutor;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.economy.EconomyManager;
import com.phazerous.phazerous.entities.EntityManager;
import com.phazerous.phazerous.items.ItemManager;
import com.phazerous.phazerous.utils.ScoreboardManager;
import com.phazerous.phazerous.entities.EntityModule;
import com.phazerous.phazerous.gui.CustomInventoryManager;
import com.phazerous.phazerous.gui.actions.CustomInventoryActionManager;
import com.phazerous.phazerous.listeners.InventoryClickListener;
import com.phazerous.phazerous.listeners.PlayerJoinListener;
import com.phazerous.phazerous.utils.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Phazerous extends JavaPlugin implements Listener {
    private EntityModule entityModule;

    @Override
    public void onEnable() {
        getServer()
                .getConsoleSender()
                .sendMessage("[Phazerous]: enabled.");

        initialize();

        entityModule.enable();
    }

    private void initialize() {
        DBManager dbManager = new DBManager();
        Scheduler scheduler = Scheduler.init(Bukkit.getScheduler(), this);
        ItemManager itemManager = new ItemManager(dbManager); // REFACTOR
        this.entityModule = new EntityModule(this, new DBManager(), getServer().getWorld("world"), itemManager, scheduler);

        EconomyManager economyManager = new EconomyManager(dbManager);
        ScoreboardManager scoreboardManager = new ScoreboardManager(economyManager);
        economyManager.subscribe(scoreboardManager);

        //REFACTOR
        CustomInventoryManager customInventoryManager = new CustomInventoryManager(dbManager, itemManager);
        CustomInventoryActionManager customInventoryActionManager = new CustomInventoryActionManager(dbManager, economyManager, itemManager);

        initializeListeners(economyManager, scoreboardManager, customInventoryManager, customInventoryActionManager);

        EntityManager test = entityModule.getEntityManager();

        getCommand("bal").setExecutor(new CommandExecutor(test));
    }

    private void initializeListeners(EconomyManager economyManager, ScoreboardManager scoreboardManager, CustomInventoryManager customInventoryManager, CustomInventoryActionManager customInventoryActionManager) {
        PlayerJoinListener playerJoinListener = new PlayerJoinListener(economyManager, scoreboardManager);
        InventoryClickListener inventoryClickListener = new InventoryClickListener(customInventoryManager, customInventoryActionManager);

        ArrayList<Listener> listeners = new ArrayList<>();
        listeners.add(playerJoinListener);
        listeners.add(inventoryClickListener);

        PluginManager pluginManager = getServer().getPluginManager();

        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, this);
        }
    }

    @Override
    public void onDisable() {
        entityModule.disable();
    }

}