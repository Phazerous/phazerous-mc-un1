package com.phazerous.phazerous.gui.dtos;

import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

@Getter @Setter
public class CustomInventoryItemDto {
    private ObjectId itemId;
    private List<Integer> slots;
    private ObjectId actionId;
}
