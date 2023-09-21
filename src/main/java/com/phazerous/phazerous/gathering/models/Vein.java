package com.phazerous.phazerous.gathering.models;

import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class Vein {
    private ObjectId _id;

    private String name;
    private Integer veinType;
    private Integer resourceDurability;
    private VeinDrop[] drops;
    private Integer respawnTime;
}