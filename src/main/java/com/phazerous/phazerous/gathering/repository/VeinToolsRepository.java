package com.phazerous.phazerous.gathering.repository;

import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.db.enums.CollectionType;
import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.gathering.models.VeinTool;
import com.phazerous.phazerous.models.PlayerVeinToolMeta;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.in;

public class VeinToolsRepository {
    private final DBManager dbManager;

    public VeinToolsRepository(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public List<VeinTool> getVeinTools(List<PlayerVeinToolMeta> playerVeinToolsMeta) {
        List<ObjectId> veinToolsIds = playerVeinToolsMeta.stream()
                .map(PlayerVeinToolMeta::getToolId)
                .collect(Collectors.toList());

        Bson filter = in("_id", veinToolsIds);

        List<Document> veinToolsDocs = dbManager.getDocuments(filter, CollectionType.TOOLS);

        return veinToolsDocs.stream()
                .map(veinToolDoc -> DocumentParser.parse(veinToolDoc, VeinTool.class))
                .collect(Collectors.toList());
    }
}
