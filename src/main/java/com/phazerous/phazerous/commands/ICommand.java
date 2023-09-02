package com.phazerous.phazerous.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public interface ICommand {
    boolean onCommand(Player player, Command command, String s, String[] args);
}
