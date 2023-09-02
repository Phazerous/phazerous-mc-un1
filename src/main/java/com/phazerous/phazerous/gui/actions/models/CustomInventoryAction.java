package com.phazerous.phazerous.gui.actions.models;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class CustomInventoryAction {
    private String type;
    private ObjectId itemId;
    private Double price;
}
