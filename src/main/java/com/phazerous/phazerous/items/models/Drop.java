package com.phazerous.phazerous.items.models;

import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class Drop {
    private ObjectId _id;

    private Integer chance;
    private ObjectId itemId;
    private DynamicAmount amount;
}