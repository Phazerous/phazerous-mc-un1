package com.phazerous.phazerous.gui.actions;

import lombok.Getter;

@Getter
public enum GUIActionType {
    TRADE("trade");

    private final String type;

    GUIActionType(String type) {
        this.type = type;
    }

    public static GUIActionType fromString(String type) {
        for (GUIActionType actionType : GUIActionType.values()) {
            if (actionType.type.equalsIgnoreCase(type)) {
                return actionType;
            }
        }
        return null;
    }
}
