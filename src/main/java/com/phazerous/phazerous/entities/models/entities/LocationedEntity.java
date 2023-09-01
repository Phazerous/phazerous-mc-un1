package com.phazerous.phazerous.entities.models.entities;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class LocationedEntity {
    private ObjectId _id;

    private Double x;
    private Double y;
    private Double z;

    private ObjectId entityId;
}
