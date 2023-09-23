package com.phazerous.phazerous.vein_gathering.models;

import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class VeinTool {
    private Integer toolType;
    private Integer strength;
    private String title;
    private Integer material;
    private ObjectId _id;

    public VeinTool() {
    }

    public VeinTool(Integer toolType, Integer strength) {
        this.toolType = toolType;
        this.strength = strength;
    }
}