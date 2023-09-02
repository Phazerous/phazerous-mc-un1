package com.phazerous.phazerous.economy.commands;

import com.phazerous.phazerous.commands.AbstractCommand;
import com.phazerous.phazerous.economy.EconomyManager;
import com.phazerous.phazerous.exceptions.PlayerNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class BalCommand extends AbstractCommand {
    private final EconomyManager economyManager;

    public BalCommand(EconomyManager economyManager) {
        this.economyManager = economyManager;
        name = "bal";
    }

    @Override
    public boolean onCommand(Player player, Command command, String s, String[] args) {
        if (args.length == 0) return handleGetBalance(player);

        if (args[0].equalsIgnoreCase("set")) return handleSetBalance(player, Arrays.copyOfRange(args, 1, args.length));


        return true;
    }

    private boolean handleGetBalance(Player player) {
        double balance = economyManager.getPlayerBalanceByUUID(player.getUniqueId());
        player.sendMessage("Your balance is " + balance + ".");
        return true;
    }

    private boolean handleSetBalance(Player player, String[] args) {
        try {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) throw new PlayerNotFoundException();

            Double amount = Double.parseDouble(args[1]);

            economyManager.setPlayerBalance(target.getUniqueId(), amount);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid amount.");
        } catch (PlayerNotFoundException e) {
            player.sendMessage(e.getMessage());
        } catch (Exception e) {
            player.sendMessage("/bal set <player> <amount>");
        }

        return true;
    }
}
