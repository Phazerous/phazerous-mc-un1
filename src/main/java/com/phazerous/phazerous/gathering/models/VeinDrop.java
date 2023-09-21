package com.phazerous.phazerous.gathering.models;

import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class VeinDrop {
    private ObjectId _id;

    private Integer chance;
    private ObjectId itemId;
    private VeinDropAmount amount;
}