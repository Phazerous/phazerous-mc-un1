package com.phazerous.phazerous.entities.utils;

import com.phazerous.phazerous.entities.enums.EntityType;
import com.phazerous.phazerous.entities.models.entities.LocationedEntity;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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

    public static EntityType getEntityType(Document document) {
        String ENTITY_TYPE_NAME = "entityType";

        Integer entityTypeCode = document.getInteger(ENTITY_TYPE_NAME);
        return EntityType.fromInteger(entityTypeCode);
    }

    public static Location getEntityLocation(LocationedEntity locationedEntity) {
        double x = locationedEntity.getX();
        double y = locationedEntity.getY();
        double z = locationedEntity.getZ();

        return new Location(Bukkit.getWorld("world"), x, y, z);
    }
}
