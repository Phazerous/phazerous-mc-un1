package com.phazerous.phazerous.items.repository;

import com.phazerous.phazerous.db.DBManager;
import com.phazerous.phazerous.db.enums.CollectionType;
import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.items.enums.ItemType;
import com.phazerous.phazerous.items.models.items.CustomItem;
import javafx.util.Pair;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.HashMap;

import static com.mongodb.client.model.Filters.eq;


public class ItemRepository {
    private final DBManager dbManager;
    private final HashMap<ObjectId, Pair<CustomItem, ItemType>> itemsStorage = new HashMap<>();

    public ItemRepository(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public Pair<CustomItem, ItemType> getItem(ObjectId itemId) {
        if (!itemsStorage.containsKey(itemId)) {
            Bson filter = eq("_id", itemId);

            Document document = dbManager.getDocument(filter, CollectionType.ITEMS);

            ItemType itemType = getItemType(document);

            CustomItem item = DocumentParser.parse(document, itemType.getItemClass());

            itemsStorage.put(itemId, new Pair<>(item, itemType));
        }

        return itemsStorage.get(itemId);
    }

    private ItemType getItemType(Document document) {
        final String ITEM_TYPE_FIELD = "itemType";

        int itemType = document.getInteger(ITEM_TYPE_FIELD);

        return ItemType.getItemTypeByValue(itemType);
    }
}
