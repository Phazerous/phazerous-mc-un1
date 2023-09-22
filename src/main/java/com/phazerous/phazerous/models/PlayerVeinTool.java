package com.phazerous.phazerous.models;

import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class PlayerVeinTool {
    private ObjectId toolId;
    private Integer toolType;
}