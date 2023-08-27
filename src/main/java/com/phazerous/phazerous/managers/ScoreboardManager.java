package com.phazerous.phazerous.managers;

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

public class ScoreboardManager {
    private final EconomyManager economyManager;
    private final HashMap<UUID, Consumer<Double>> balanceSetters = new HashMap<>();

    public ScoreboardManager(EconomyManager economyManager) {
        this.economyManager = economyManager;
    }

    public void createBoard(Player player) {
        final int SCOREBOARD_BALANCE_POSITION = 2;
        final String SCOREBOARD_TITLE = ChatColor.GOLD + "PhazerousMC";

        double balance = economyManager.getPlayerBalanceByUUID(player.getUniqueId());

        org.bukkit.scoreboard.ScoreboardManager scoreboardManager = org.bukkit.Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective(SCOREBOARD_TITLE, "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        setSidebarLine(scoreboard, objective, 1);
        Team money = setSidebarLine(scoreboard, objective, SCOREBOARD_BALANCE_POSITION);
        setSidebarLine(scoreboard, objective, 3);

        Consumer<Double> setBalance = getBalanceSetter(money);
        balanceSetters.put(player.getUniqueId(), setBalance);

        setBalance.accept(balance);

        player.setScoreboard(scoreboard);
    }

    private Team setSidebarLine(Scoreboard scoreboard, Objective objective, int slot) {
        Team team = scoreboard.registerNewTeam("sidebar" + slot);
        String entry = "§" + slot;

        team.addEntry(entry);
        objective.getScore(entry).setScore(slot);

        return team;
    }

    private Consumer<Double> getBalanceSetter(Team team) {
        final String SCOREBOARD_BALANCE_TITLE = "Balance: ";
        final String SCOREBOARD_BALANCE_COLOR = "§6";
        final DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

        return newBalance -> {
            String formattedBalance = decimalFormat.format(newBalance);

            team.setPrefix(SCOREBOARD_BALANCE_TITLE + SCOREBOARD_BALANCE_COLOR + formattedBalance);
        };
    }

    public void updateBalance(UUID playerUUID, double newBalance) {
        Consumer<Double> setBalance = balanceSetters.get(playerUUID);
        setBalance.accept(newBalance);
    }
}
