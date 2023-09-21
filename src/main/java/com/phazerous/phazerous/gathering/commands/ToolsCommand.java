package com.phazerous.phazerous.gathering.commands;

import com.phazerous.phazerous.commands.AbstractCommand;
import com.phazerous.phazerous.gathering.GatheringManager;
import com.phazerous.phazerous.gathering.enums.ToolSetType;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class ToolsCommand extends AbstractCommand {
    private final GatheringManager gatheringManager;

    public ToolsCommand(GatheringManager gatheringManager) {
        name = "tools";
        this.gatheringManager = gatheringManager;
    }

    @Override
    public boolean onCommand(Player player, Command command, String s, String[] args) {
        gatheringManager.handleStart(player, ToolSetType.MINING);

        return true;
    }
}
