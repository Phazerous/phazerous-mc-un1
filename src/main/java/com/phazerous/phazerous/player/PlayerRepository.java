package com.phazerous.phazerous.player;

import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.db.enums.CollectionType;
import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.models.PlayerModel;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;

import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class PlayerRepository {
    private final DBManager dbManager;

    public PlayerRepository(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public PlayerModel getPlayer(Player player) {
        UUID uuid = player.getUniqueId();

        Bson filter = eq("uuid", uuid);

        Document playerDoc = dbManager.getDocument(filter, CollectionType.PLAYER);

        return DocumentParser.parse(playerDoc, PlayerModel.class);
    }
}
