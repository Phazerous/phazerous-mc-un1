package com.phazerous.phazerous.commands;

import com.phazerous.phazerous.entities.EntityManager;
import com.phazerous.phazerous.entities.EntityModule;
import com.phazerous.phazerous.entities.bosses.AbstractBoss;
import com.phazerous.phazerous.entities.bosses.BossZombieKing;
import com.phazerous.phazerous.gui.CustomInventoryManager;
import com.phazerous.phazerous.economy.EconomyManager;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    private EntityManager entityManager;

    public CommandExecutor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private BossZombieKing bossZombieKing;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("[Phazerous]: You must be a player to execute this command.");
            return true;
        }

        Player sender = (Player) commandSender;

        String commandName = command.getName();

        if (commandName.equalsIgnoreCase("bal")) {
            if (bossZombieKing == null) {
                this.bossZombieKing = (BossZombieKing) entityManager.getBoss();
                bossZombieKing.spawnBoss();
            } else {
                bossZombieKing.activateSkill();
            }


//            if (args.length == 0) {
//                double balance = economyManager.getPlayerBalanceByUUID(sender.getUniqueId());
//                sender.sendMessage("Your balance is " + balance + ".");
//                return true;
//            }
//
//            if (args.length == 1 && args[0].equalsIgnoreCase("test")) {
//                customInventoryManager.openInventory(sender, new ObjectId("64eba7ce21280a946df59bd5"));
//            }
//
//            if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
//                try {
//                    String playerName = args[1];
//                    Player player = Bukkit.getPlayer(playerName);
//
//                    if (player == null) {
//                        commandSender.sendMessage("Player not found.");
//                        return true;
//                    }
//
//                    double amount = Double.parseDouble(args[2]);
//                    if (economyManager.setPlayerBalance(player.getUniqueId(), amount)) {
//                        commandSender.sendMessage("Successfully set " + playerName + "'s balance to " + amount + ".");
//                        player.sendMessage("Your balance has been set to " + amount + ".");
//                    }
//                } catch (Exception e) {
//                    commandSender.sendMessage("Invalid arguments.");
//                    commandSender.sendMessage("Usage: /bal set <player> <amount>");
//                }
//
//                return true;
//            }
        }

        return true;
    }
}
