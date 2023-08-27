package com.phazerous.phazerous.listeners;

import com.phazerous.phazerous.managers.EconomyManager;
import com.phazerous.phazerous.managers.ScoreboardManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoinListener implements Listener {
    private final EconomyManager economyManager;
    private final ScoreboardManager scoreboardManager;

    public PlayerJoinListener(EconomyManager economyManager, ScoreboardManager scoreboardManager) {
        this.economyManager = economyManager;
        this.scoreboardManager = scoreboardManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();

        economyManager.validatePlayerBalance(playerUUID);
        scoreboardManager.createBoard(event.getPlayer());
    }
}
