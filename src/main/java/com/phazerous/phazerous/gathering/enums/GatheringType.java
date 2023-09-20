package com.phazerous.phazerous.gathering.enums;

import lombok.Getter;

@Getter
public enum GatheringType {
    MINING("mining"), DIGGING("digging"), CHOPPING("chopping");

    private final String dbPropertyName;

    GatheringType(String dbPropertyName) {
        this.dbPropertyName = dbPropertyName;
    }
}
