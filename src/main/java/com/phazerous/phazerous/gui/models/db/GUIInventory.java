package com.phazerous.phazerous.gui.models.db;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
public class GUIInventory {
    private ObjectId _id;
    private String title;
    private Integer size;
    private List<GUIItem> contents;
}
