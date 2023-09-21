package com.phazerous.phazerous.gathering.models;

import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class VeinLocation {
    private Double x;
    private Double y;
    private Double z;
    private ObjectId veinId;
}