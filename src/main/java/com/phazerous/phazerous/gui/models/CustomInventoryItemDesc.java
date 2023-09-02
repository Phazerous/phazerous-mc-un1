package com.phazerous.phazerous.gui.models;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
public class CustomInventoryItemDesc {
    private ObjectId itemId;
    private List<Integer> slots;
    private ObjectId actionId;
    private Double price;
}
