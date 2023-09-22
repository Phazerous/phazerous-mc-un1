package com.phazerous.phazerous.gathering;

import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.db.enums.CollectionType;
import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.gathering.models.Vein;
import com.phazerous.phazerous.gathering.models.VeinEntity;
import com.phazerous.phazerous.gathering.models.VeinLocation;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class VeinManager {
    private final DBManager dbManager;
    private final HashMap<ObjectId, Vein> veinsById = new HashMap<>();

    public VeinManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public Vein getVein(VeinEntity veinEntity) {
        if (!veinsById.containsKey(veinEntity.getVeinId())) {
            Bson filter = eq("_id", veinEntity.getVeinId());

            Document document = dbManager.getDocument(filter, CollectionType.VEINS);
            Vein vein = DocumentParser.parse(document, Vein.class);

            veinsById.put(veinEntity.getVeinId(), vein);
        }

        return veinsById.get(veinEntity.getVeinId());
    }
}
