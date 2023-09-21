package com.phazerous.phazerous.gathering;

import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.db.enums.CollectionType;
import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.gathering.enums.PlayerMetaTools;
import com.phazerous.phazerous.gathering.enums.Tool;
import com.phazerous.phazerous.gathering.enums.ToolSetType;
import com.phazerous.phazerous.utils.ItemBuilder;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;

public class ToolsManager {
    private final DBManager dbManager;

    public ToolsManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public List<ItemStack> getPlayerTools(Player player, ToolSetType toolSetType) {
        List<Tool> playerToolsModels = getPlayerToolsModels(player, toolSetType);

        if (playerToolsModels == null) return null;

        return playerToolsModels.stream()
                .map(tool -> new ItemBuilder(tool.getMaterial()).setDisplayName(tool.getTitle())
                        .build()).collect(Collectors.toList());
    }

    private List<Tool> getPlayerToolsModels(Player player, ToolSetType toolSetType) {
        List<ObjectId> playerToolsIds = getPlayerToolsIds(player, toolSetType);

        if (playerToolsIds == null) return null;

        Bson filter = in("_id", playerToolsIds);

        List<Document> toolsDocs = dbManager.getDocuments(filter, CollectionType.TOOLS);

        return toolsDocs.stream()
                .map(toolDoc -> DocumentParser.parse(toolDoc, Tool.class))
                .collect(Collectors.toList());
    }

    private List<ObjectId> getPlayerToolsIds(Player player, ToolSetType toolSetType) {
        Bson filter = eq("uuid", player.getUniqueId());
        Bson projection = eq("tools", 1);

        Document tools = dbManager.getDocument(filter, CollectionType.PLAYER, projection)
                .get("tools", Document.class);

        PlayerMetaTools playerMetaTools = DocumentParser.parse(tools, PlayerMetaTools.class);

        switch (toolSetType) {
            case MINING:
                return playerMetaTools.getMining();
            case DIGGING:
                return playerMetaTools.getDigging();
            case CHOPPING:
                return playerMetaTools.getChopping();
            default:
                return null;
        }
    }
}
