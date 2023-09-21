package com.phazerous.phazerous.gathering.models;

import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class Tool {
    private ObjectId _id;
    private Integer toolType;
    private String title;
    private Integer material;
    private Integer strength;
}
