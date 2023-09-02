package com.phazerous.phazerous.economy;

import com.phazerous.phazerous.archtecture.AbstractModule;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.economy.commands.BalCommand;
import com.phazerous.phazerous.economy.listeners.PlayerJoinListener;
import com.phazerous.phazerous.utils.ScoreboardManager;
import lombok.Getter;

@Getter
public class EconomyModule extends AbstractModule {
    private final EconomyManager economyManager;

    public EconomyModule(DBManager dbManager, ScoreboardManager scoreboardManager) {
        economyManager = new EconomyManager(dbManager);

        addCommand(new BalCommand(economyManager));

        scoreboardManager.setEconomyManager(economyManager);
        addListener(new PlayerJoinListener(economyManager, scoreboardManager));
    }
}
