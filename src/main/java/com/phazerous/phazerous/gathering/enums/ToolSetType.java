package com.phazerous.phazerous.gathering.enums;

import lombok.Getter;

@Getter
public enum ToolSetType {
    MINING("mining"), DIGGING("digging"), CHOPPING("chopping");

    private final String dbPropertyName;

    ToolSetType(String dbPropertyName) {
        this.dbPropertyName = dbPropertyName;
    }
}
