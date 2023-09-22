package com.phazerous.phazerous.gathering.models;

import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class VeinTool {
    private ObjectId _id;
    private String title;
    private Integer material;
    private Integer toolType;
    private Integer strength;
}