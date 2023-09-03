package com.phazerous.phazerous.gui.actions;

import com.phazerous.phazerous.gui.actions.models.AbstractGUIAction;
import com.phazerous.phazerous.gui.actions.models.PurchaseItemWithMoneyAction;
import lombok.Getter;

@Getter
public enum GUIActionType {
    PURCHASE_ITEM_WITH_MONEY(1, PurchaseItemWithMoneyAction.class);

    private final Integer type;
    private final Class<? extends AbstractGUIAction> actionSchemaModel;

    GUIActionType(Integer type, Class<? extends AbstractGUIAction> actionSchemaModel) {
        this.type = type;
        this.actionSchemaModel = actionSchemaModel;
    }

    public Class<? extends AbstractGUIAction> getActionSchema() {
        return actionSchemaModel;
    }

    public static GUIActionType fromInteger(Integer type) {
        for (GUIActionType actionType : GUIActionType.values()) {
            if (actionType.type.equals(type)) {
                return actionType;
            }
        }
        return null;
    }


//    PURCHA
//    PURCHASE("purchase");
//
//    private final String type;
//
//    GUIActionType(String type) {
//        this.type = type;
//    }
//
//    public static GUIActionType fromString(String type) {
//        for (GUIActionType actionType : GUIActionType.values()) {
//            if (actionType.type.equalsIgnoreCase(type)) {
//                return actionType;
//            }
//        }
//        return null;
//    }
}
