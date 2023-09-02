package com.phazerous.phazerous.db.enums;

import lombok.Getter;

@Getter
public enum CollectionType {
    LOCATIONED_ENTITIES("locationed_entities"), ENTITIES("entities"), ITEMS("items"), RUNTIME_ENTITIES("runtime_entities"), PLAYERS_BALANCES("players_balances"), CUSTOM_INVENTORIES("custom_inventories"), CUSTOM_INVENTORIES_ACTIONS("custom_inventory_actions");

    private final String collectionName;

    CollectionType(String collectionName) {
        this.collectionName = collectionName;
    }
}
