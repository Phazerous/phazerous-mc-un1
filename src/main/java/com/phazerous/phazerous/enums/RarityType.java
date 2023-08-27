package com.phazerous.phazerous.enums;

import lombok.Getter;

@Getter
public enum RarityType {
    COMMON(0, "COMMON", "§f"),
    UNCOMMON(1, "UNCOMMON", "§a"),
    RARE(2, "RARE", "§9"),
    EPIC(3, "EPIC", "§5"),
    LEGENDARY(4, "LEGENDARY", "§6");

    private final int value;
    private final String title;
    private final String color;

    RarityType(int value, String title, String color) {
        this.value = value;
        this.title = title;
        this.color = color;
    }


    public static RarityType getRarityTypeByValue(int value) {
        for (RarityType rarityType : RarityType.values()) {
            if (rarityType.getValue() == value) {
                return rarityType;
            }
        }
        return null;
    }
}
