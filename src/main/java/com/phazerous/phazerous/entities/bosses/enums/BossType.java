package com.phazerous.phazerous.entities.bosses.enums;

import lombok.Getter;

@Getter
public enum BossType {
    ZOMBIE_KING("Zombie King");

    private final String name;

    BossType(String name) {
        this.name = name;
    }

    public static BossType getByTitle(String name) {
        for (BossType bossType : values()) {
            if (bossType.getName().equalsIgnoreCase(name)) {
                return bossType;
            }
        }

        return null;
    }
}
