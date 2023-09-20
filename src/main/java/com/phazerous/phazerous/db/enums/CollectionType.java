package com.phazerous.phazerous.db.enums;

import lombok.Getter;

@Getter
public enum CollectionType {
    LOCATIONED_ENTITIES("locationed_entities"), ENTITIES("entities"), ITEMS("items"), RUNTIME_ENTITIES("runtime_entities"), PLAYERS_BALANCES("players_balances"), GUI_INVENTORIES("gui_inventories"), GUI_ACTIONS("gui_actions"), PLAYER("players");

    private final String collectionName;

    CollectionType(String collectionName) {
        this.collectionName = collectionName;
    }
}
