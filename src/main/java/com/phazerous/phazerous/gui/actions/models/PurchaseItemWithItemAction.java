package com.phazerous.phazerous.gui.actions.models;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
public class PurchaseItemWithItemAction extends PurchaseItemAction {
    private ObjectId requestedItemId;
    private Integer amount;

    /**
     * The value is set via the code, not the database.
     */
    @Setter
    private String requestedItemName;
}
