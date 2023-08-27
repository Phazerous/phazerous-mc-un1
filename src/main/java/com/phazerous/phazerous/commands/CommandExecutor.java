package com.phazerous.phazerous.commands;

import com.phazerous.phazerous.managers.EconomyManager;
import com.phazerous.phazerous.managers.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {
    private final EconomyManager economyManager;
    private final ScoreboardManager scoreboardManager;

    public CommandExecutor(EconomyManager economyManager, ScoreboardManager scoreboardManager) {
        this.economyManager = economyManager;
        this.scoreboardManager = scoreboardManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("[Phazerous]: You must be a player to execute this command.");
            return true;
        }

        Player sender = (Player) commandSender;

        String commandName = command.getName();

        if (commandName.equalsIgnoreCase("bal")) {
            if (args.length == 0) {
                double balance = economyManager.getPlayerBalanceByUUID(sender.getUniqueId());
                sender.sendMessage("Your balance is " + balance + ".");
                return true;
            }

            if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
                try {
                    String playerName = args[1];
                    Player player = Bukkit.getPlayer(playerName);

                    if (player == null) {
                        commandSender.sendMessage("Player not found.");
                        return true;
                    }

                    double amount = Double.parseDouble(args[2]);
                    if (economyManager.setPlayerBalance(player.getUniqueId(), amount)) {
                        commandSender.sendMessage("Successfully set " + playerName + "'s balance to " + amount + ".");
                        player.sendMessage("Your balance has been set to " + amount + ".");
                        scoreboardManager.updateBalance(player.getUniqueId(), amount);
                    }
                } catch (Exception e) {
                    commandSender.sendMessage("Invalid arguments.");
                    commandSender.sendMessage("Usage: /bal set <player> <amount>");
                }

                return true;
            }
        }

        return true;
    }
}