package com.phazerous.phazerous.gui.actions.models;

import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public abstract class PurchaseItemAction extends AbstractGUIAction {
    private ObjectId itemIdToPurchase;
}
