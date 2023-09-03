package com.phazerous.phazerous.gui.models.db;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
public class GUIItem {
    private ObjectId itemId;
    private List<Integer> slots;
    private ObjectId actionId;
}
