package com.phazerous.phazerous.db;

import com.phazerous.phazerous.economy.models.PlayerBalance;
import com.phazerous.phazerous.entities.models.entities.BaseEntity;
import com.phazerous.phazerous.entities.models.entities.LocationedEntity;
import com.phazerous.phazerous.entities.models.runtime.RuntimeBaseEntity;
import com.phazerous.phazerous.gui.actions.dtos.CustomInventoryActionDto;
import com.phazerous.phazerous.gui.dtos.CustomInventoryDto;
import com.phazerous.phazerous.items.models.ItemDto;
import lombok.Getter;

@Getter
public enum CollectionType {
    LOCATIONED_ENTITIES("locationed_entities"), ENTITIES("entities"), ITEM("items"), RUNTIME_ENTITY("runtime_entities"), PLAYERS_BALANCES("players_balances"), CUSTOM_INVENTORY("custom_inventories"), CUSTOM_INVENTORY_ACTIONS("custom_inventory_actions");

    private final String collectionName;

    CollectionType(String collectionName) {
        this.collectionName = collectionName;
    }

    public static CollectionType getCollectionTypeByClass(Class<?> clazz) {
        if (clazz == LocationedEntity.class) {
            return LOCATIONED_ENTITIES;
        } else if (clazz == BaseEntity.class) {
            return ENTITIES;
        } else if (clazz == ItemDto.class) {
            return ITEM;
        } else if (clazz == RuntimeBaseEntity.class) {
            return RUNTIME_ENTITY;
        } else if (clazz == PlayerBalance.class) {
            return PLAYERS_BALANCES;
        } else if (clazz == CustomInventoryDto.class) {
            return CUSTOM_INVENTORY;
        } else if (clazz == CustomInventoryActionDto.class) {
            return CUSTOM_INVENTORY_ACTIONS;
        }

        return null;
    }
}
