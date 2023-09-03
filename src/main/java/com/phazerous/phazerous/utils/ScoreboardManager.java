package com.phazerous.phazerous.utils;

import com.phazerous.phazerous.economy.EconomyManager;
import com.phazerous.phazerous.economy.interfaces.IEconomySubscriber;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class ScoreboardManager implements IEconomySubscriber {
    private EconomyManager economyManager;
    private final HashMap<UUID, Consumer<String>> balanceSetters = new HashMap<>();
    private final HashMap<UUID, Long> balances = new HashMap<>();

    public void setEconomyManager(EconomyManager economyManager) {
        this.economyManager = economyManager;
        economyManager.subscribe(this);
    }

    public void createBoard(Player player) {
        final int SCOREBOARD_BALANCE_POSITION = 2;
        final String SCOREBOARD_TITLE = ChatColor.GOLD + "PhazerousMC";

        long balance = economyManager.getPlayerBalanceByUUID(player.getUniqueId());
        balances.put(player.getUniqueId(), balance);

        org.bukkit.scoreboard.ScoreboardManager scoreboardManager = org.bukkit.Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective(SCOREBOARD_TITLE, "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        setSidebarLine(scoreboard, objective, 1);
        Team money = setSidebarLine(scoreboard, objective, SCOREBOARD_BALANCE_POSITION);
        setSidebarLine(scoreboard, objective, 3);

        Consumer<String> setBalance = getBalanceSetter(money);
        balanceSetters.put(player.getUniqueId(), setBalance);

        setBalance.accept(Formatter.formatFunds(balance));

        player.setScoreboard(scoreboard);
    }

    private Team setSidebarLine(Scoreboard scoreboard, Objective objective, int slot) {
        Team team = scoreboard.registerNewTeam("sidebar" + slot);
        String entry = "§" + slot;

        team.addEntry(entry);
        objective.getScore(entry).setScore(slot);

        return team;
    }

    private Consumer<String> getBalanceSetter(Team team) {
        final String SCOREBOARD_BALANCE_TITLE = "Funds: ";
        final String SCOREBOARD_BALANCE_COLOR = "§6";

        return newBalance -> team.setPrefix(SCOREBOARD_BALANCE_TITLE + SCOREBOARD_BALANCE_COLOR + newBalance);
    }

    @Override
    public void update(UUID playerUUID, long balance) {
        Consumer<String> setBalance = balanceSetters.get(playerUUID);
        setBalance.accept(Formatter.formatFunds(balance));
    }
}
