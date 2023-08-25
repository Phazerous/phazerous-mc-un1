package com.phazerous.phazerous.dtos;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;


@Getter @Setter
public class LocationedEntityDto {
    private ObjectId id;
    private ObjectId entityId;

    private double x;
    private double y;
    private double z;
}
