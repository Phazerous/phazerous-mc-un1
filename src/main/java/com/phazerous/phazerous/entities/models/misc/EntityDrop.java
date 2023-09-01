package com.phazerous.phazerous.entities.models.misc;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class EntityDrop {
    private ObjectId _id;

    private Double dropChance;
    private ObjectId itemId;
}
