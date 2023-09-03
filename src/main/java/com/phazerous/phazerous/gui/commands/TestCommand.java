package com.phazerous.phazerous.gui.commands;

import com.phazerous.phazerous.commands.AbstractCommand;
import com.phazerous.phazerous.gui.managers.GUIManager;
import org.bson.types.ObjectId;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TestCommand extends AbstractCommand {
    private final GUIManager guiManager;

    public TestCommand(GUIManager GUIManager) {
        guiManager = GUIManager;
        name = "test";
    }

    @Override
    public boolean onCommand(Player player, Command command, String s, String[] args) {
        guiManager.openInventory(player, new ObjectId("64f4286ab4fd53dbfaf4466a"));

        return true;
    }
}
