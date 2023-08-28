package com.phazerous.phazerous.gui.actions.dtos;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter @Setter
public class CustomInventoryActionDto {
    private String type;
    private ObjectId itemId;
    private double price;
}
