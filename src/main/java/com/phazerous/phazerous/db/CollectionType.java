package com.phazerous.phazerous.db;

import com.phazerous.phazerous.dtos.*;
import com.phazerous.phazerous.gui.actions.dtos.CustomInventoryActionDto;
import com.phazerous.phazerous.gui.dtos.CustomInventoryDto;
import com.phazerous.phazerous.gui.dtos.CustomInventoryItemDto;
import lombok.Getter;

@Getter
public enum CollectionType {
    LOCATIONED_ENTITY("locationed_entities"),
    ENTITY("entities"),
    ITEM("items"),
    RUNTIME_ENTITY("runtime_entities"),
    PLAYER_BALANCE("player_balances"),
    CUSTOM_INVENTORY("custom_inventories"),
    CUSTOM_INVENTORY_ACTIONS("custom_inventory_actions");

    private final String collectionName;

    CollectionType(String collectionName) {
        this.collectionName = collectionName;
    }

    public static CollectionType getCollectionTypeByClass(Class<?> clazz) {
        if (clazz == LocationedEntityDto.class) {
            return LOCATIONED_ENTITY;
        } else if (clazz == EntityDto.class) {
            return ENTITY;
        } else if (clazz == ItemDto.class) {
            return ITEM;
        } else if (clazz == RuntimeEntityDto.class) {
            return RUNTIME_ENTITY;
        } else if (clazz == PlayerBalanceDto.class) {
            return PLAYER_BALANCE;
        } else if (clazz == CustomInventoryDto.class) {
            return CUSTOM_INVENTORY;
        } else if (clazz == CustomInventoryActionDto.class) {
            return CUSTOM_INVENTORY_ACTIONS;
        }

        return null;
    }
}
