package com.phazerous.phazerous.shared;

import com.phazerous.phazerous.archtecture.AbstractModule;
import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.player.PlayerRepository;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class SharedModule extends AbstractModule {
    private final SpawnPacketManager spawnPacketManager;
    private final JavaPlugin plugin;
    private final PlayerRepository playerRepository;
    private final DBManager dbManager;

    public SharedModule(JavaPlugin plugin, DBManager dbManager) {
        this.plugin = plugin;
        this.dbManager = dbManager;

        this.spawnPacketManager = new SpawnPacketManager();
        this.playerRepository = new PlayerRepository(dbManager);
    }
}
