package com.phazerous.phazerous.gui.commands;

import com.phazerous.phazerous.commands.AbstractCommand;
import com.phazerous.phazerous.gui.GUIManager;
import org.bson.types.ObjectId;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class TestCommand extends AbstractCommand {
    private final GUIManager guiManager;

    public TestCommand(GUIManager GUIManager) {
        guiManager = GUIManager;
        name = "test";
    }

    @Override
    public boolean onCommand(Player player, Command command, String s, String[] args) {
        guiManager.openInventory(player, new ObjectId("64eba7ce21280a946df59bd5"));

        return true;
    }
}
