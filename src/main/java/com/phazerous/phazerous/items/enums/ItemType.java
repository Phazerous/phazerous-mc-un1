package com.phazerous.phazerous.items.enums;

import com.phazerous.phazerous.items.models.items.*;
import lombok.Getter;

@Getter
public enum ItemType {
    USUAL(0, CustomItem.class), GATHERING_DIGGING(1, GatheringItem.class), WEAPON_HANDHELD(3, WeaponItem.class), ARMOR(4, ArmorItem.class), RESOURCE(5, ResourceItem.class);

    private final Integer itemType;
    private final Class<? extends CustomItem> itemClass;

    ItemType(Integer itemType, Class<? extends CustomItem> itemClass) {
        this.itemType = itemType;
        this.itemClass = itemClass;
    }

    public static ItemType getItemTypeByValue(Integer itemType) {
        for (ItemType type : ItemType.values()) {
            if (type
                    .getItemType()
                    .equals(itemType)) {
                return type;
            }
        }
        return null;
    }
}
