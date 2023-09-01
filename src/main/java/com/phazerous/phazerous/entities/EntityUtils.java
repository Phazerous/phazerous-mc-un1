package com.phazerous.phazerous.entities;

import org.bson.Document;
import org.bukkit.entity.Entity;

public class EntityUtils {
    public static void setEntityHPTitle(Entity entity, String entityTitle, Long currentHP, Long maxHP) {
        String ENTITY_TITLE_TEMPLATE = "%s %s/%s❤";
        String formattedTitle = String.format(ENTITY_TITLE_TEMPLATE, entityTitle, currentHP.toString(), maxHP.toString());

        entity.setCustomName(formattedTitle);
    }

    public static EntityType getRuntimeEntityType(Document runtimeEntityDoc) {
        String ENTITY_TYPE_NAME = "entityType";
        Integer entityTypeCode = runtimeEntityDoc.getInteger(ENTITY_TYPE_NAME);
        return EntityType.fromInteger(entityTypeCode);
    }
}
