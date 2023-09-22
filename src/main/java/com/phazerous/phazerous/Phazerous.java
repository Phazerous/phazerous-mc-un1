package com.phazerous.phazerous;

import com.phazerous.phazerous.archtecture.AbstractModule;
import com.phazerous.phazerous.commands.AbstractCommand;
import com.phazerous.phazerous.commands.CommandExecutor;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.economy.EconomyManager;
import com.phazerous.phazerous.economy.EconomyModule;
import com.phazerous.phazerous.entities.EntityModule;
import com.phazerous.phazerous.gathering.GatheringModule;
import com.phazerous.phazerous.gui.GUIModule;
import com.phazerous.phazerous.items.ItemManager;
import com.phazerous.phazerous.items.ItemsModule;
import com.phazerous.phazerous.message_dispatcher.MessageDispatcher;
import com.phazerous.phazerous.player.PlayerModule;
import com.phazerous.phazerous.regions.RegionModule;
import com.phazerous.phazerous.shared.SharedModule;
import com.phazerous.phazerous.utils.Scheduler;
import com.phazerous.phazerous.utils.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Phazerous extends JavaPlugin implements Listener {
    private EntityModule entityModule;

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage("[Phazerous]: enabled.");

        initialize();

        entityModule.enable();
    }

    private void initialize() {
        DBManager dbManager = new DBManager();

        SharedModule sharedModule = new SharedModule(this, dbManager);

        // MOVE TO SHARED MODULE
        Scheduler scheduler = Scheduler.init(Bukkit.getScheduler(), this);

        ItemsModule itemsModule = new ItemsModule(dbManager);
        ItemManager itemManager = itemsModule.getItemManager();

        ScoreboardManager scoreboardManager = new ScoreboardManager();

        EconomyModule economyModule = new EconomyModule(dbManager, scoreboardManager);
        EconomyManager economyManager = economyModule.getEconomyManager();

        this.entityModule = new EntityModule(dbManager, itemManager, economyManager);

        PlayerModule playerModule = new PlayerModule(sharedModule);

        GatheringModule gatheringModule = new GatheringModule(sharedModule, playerModule.getPlayerRepository());


        GUIModule guiModule = new GUIModule(dbManager, itemManager, economyManager);

        MessageDispatcher messageDispatcher = new MessageDispatcher();

        RegionModule regionModule = new RegionModule(sharedModule);
        regionModule.subscribeToRegionChange(gatheringModule.getVeinManager());
        regionModule.subscribeToRegionChange(messageDispatcher);

        registerCommands(entityModule, economyModule, itemsModule, guiModule, gatheringModule);
        registerListeners(entityModule, economyModule, itemsModule, guiModule, regionModule, gatheringModule);
    }

    private void registerCommands(AbstractModule... modules) {
        CommandExecutor commandExecutor = new CommandExecutor(this);

        for (AbstractModule module : modules) {
            for (AbstractCommand command : module.getCommands()) {
                commandExecutor.registerCommand(command);
            }
        }
    }

    private void registerListeners(AbstractModule... modules) {
        PluginManager pluginManager = getServer().getPluginManager();

        for (AbstractModule module : modules) {
            for (Listener listener : module.getListeners()) {
                pluginManager.registerEvents(listener, this);
            }
        }
    }

    @Override
    public void onDisable() {
        entityModule.disable();
    }

}