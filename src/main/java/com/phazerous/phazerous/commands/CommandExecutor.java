package com.phazerous.phazerous.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    private final List<AbstractCommand> commands = new ArrayList<>();

    private final JavaPlugin plugin;
    
    public CommandExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You must be a player to execute this command.");
            return true;
        }

        Player player = (Player) commandSender;
        String commandName = command.getName();

        Optional<AbstractCommand> matchedCommand = commands.stream()
                .filter(cmd -> cmd.getName().equalsIgnoreCase(commandName)).findFirst();

        return matchedCommand.map(abstractCommand -> abstractCommand.onCommand(player, command, s, args)).orElse(true);
    }

    public void registerCommand(AbstractCommand command) {
        commands.add(command);
        plugin.getCommand(command.getName()).setExecutor(this);

    }
}
