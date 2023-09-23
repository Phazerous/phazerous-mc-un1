package com.phazerous.phazerous.gathering.models;

import com.phazerous.phazerous.gathering.enums.VeinType;
import lombok.AccessLevel;
import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class Vein {
    private ObjectId _id;

    private String name;
    @Getter(AccessLevel.NONE)
    private Integer veinType;
    private Integer resourceDurability;
    private VeinDrop[] drops;
    private Integer respawnTime;

    public VeinType getVeinType() {
        return VeinType.getVeinType(veinType);
    }
}