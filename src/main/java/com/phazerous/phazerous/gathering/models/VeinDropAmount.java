package com.phazerous.phazerous.gathering.models;

import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class VeinDropAmount {
    private ObjectId _id;
    private Integer min;
    private Integer max;
}
