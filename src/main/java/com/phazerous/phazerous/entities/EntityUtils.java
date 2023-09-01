package com.phazerous.phazerous.entities;

import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;

public class EntityUtils {
    public static void setMobEntityHPTitle(Entity entity, String entityTitle, Long currentHP, Long maxHP) {
        String ENTITY_TITLE_TEMPLATE = "%s %s/%s❤";
        String formattedTitle = String.format(ENTITY_TITLE_TEMPLATE, entityTitle, currentHP.toString(), maxHP.toString());

        entity.setCustomName(formattedTitle);
    }

    public static void setGatheringEntityTitle(Entity entity, float percentage) {
        final String TITLE = "|||Gather|||";
        final String PREFIX = "§6[§r";
        final String POSTFIX = "§6]§r";

        final String[] COLORS = {"§c", "§e", "§a"};
        final int[] THRESHOLDS = {33, 66};

        int symbols = Math.round(TITLE.length() * percentage);

        String titleColor = COLORS[2];

        for (int i = 0; i < THRESHOLDS.length; i++) {
            int threshold = THRESHOLDS[i];

            if (percentage <= (float) threshold / 100) {
                titleColor = COLORS[i];
                break;
            }
        }

        String preparedTitle = TITLE.substring(0, symbols) + "§r" + TITLE.substring(symbols);
        String label = PREFIX + titleColor + preparedTitle + POSTFIX;

        String title = ChatColor.translateAlternateColorCodes('§', label);

        entity.setCustomName(title);
    }

    public static EntityType getRuntimeEntityType(Document runtimeEntityDoc) {
        String ENTITY_TYPE_NAME = "entityType";
        Integer entityTypeCode = runtimeEntityDoc.getInteger(ENTITY_TYPE_NAME);
        return EntityType.fromInteger(entityTypeCode);
    }
}
