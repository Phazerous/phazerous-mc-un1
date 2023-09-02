package com.phazerous.phazerous;

import com.phazerous.phazerous.archtecture.AbstractModule;
import com.phazerous.phazerous.commands.AbstractCommand;
import com.phazerous.phazerous.commands.CommandExecutor;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.economy.EconomyManager;
import com.phazerous.phazerous.economy.EconomyModule;
import com.phazerous.phazerous.entities.EntityModule;
import com.phazerous.phazerous.gui.GUIModule;
import com.phazerous.phazerous.items.ItemManager;
import com.phazerous.phazerous.items.ItemsModule;
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
        Scheduler scheduler = Scheduler.init(Bukkit.getScheduler(), this);

        ItemsModule itemsModule = new ItemsModule(dbManager);
        ItemManager itemManager = itemsModule.getItemManager();

        ScoreboardManager scoreboardManager = new ScoreboardManager();

        EconomyModule economyModule = new EconomyModule(dbManager, scoreboardManager);
        EconomyManager economyManager = economyModule.getEconomyManager();

        this.entityModule = new EntityModule(dbManager, itemManager, economyManager);

        GUIModule guiModule = new GUIModule(dbManager, itemManager, economyManager);


        registerCommands(entityModule, economyModule, itemsModule, guiModule);
        registerListeners(entityModule, economyModule, itemsModule, guiModule);
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