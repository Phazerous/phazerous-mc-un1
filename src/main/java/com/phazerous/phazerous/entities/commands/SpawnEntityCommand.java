package com.phazerous.phazerous.entities.commands;

import com.phazerous.phazerous.commands.AbstractCommand;
import com.phazerous.phazerous.entities.bosses.BossManager;
import com.phazerous.phazerous.entities.bosses.enums.BossType;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class SpawnEntityCommand extends AbstractCommand {
    private final BossManager bossManager;

    public SpawnEntityCommand(BossManager bossManager) {
        this.bossManager = bossManager;
        name = "spawnentity";
    }

    @Override
    public boolean onCommand(Player player, Command command, String s, String[] args) {
        try {
            String bossTitle = String.join(" ", args);

            System.out.println(bossTitle);

            BossType bossType = BossType.getByTitle(bossTitle);

            bossManager.spawnBoss(bossType);
        } catch (Exception e) {
            player.sendMessage("/spawnentity <entity>");
        }

        return true;
    }
}
