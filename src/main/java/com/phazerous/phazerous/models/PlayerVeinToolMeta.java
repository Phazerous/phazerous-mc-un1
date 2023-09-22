package com.phazerous.phazerous.models;

import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class PlayerVeinToolMeta {
    private ObjectId toolId;
    private Integer toolType;
}