package com.phazerous.phazerous.vein_gathering.models;

import com.phazerous.phazerous.items.models.Drop;
import com.phazerous.phazerous.vein_gathering.enums.VeinType;
import lombok.AccessLevel;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
public class Vein {
    private ObjectId _id;

    private String name;
    @Getter(AccessLevel.NONE)
    private Integer veinType;
    private Integer resourceDurability;
    private List<Drop> drops;
    private Integer respawnTime;
    private List<VeinResourceLayer> resourceLayers;

    public VeinType getVeinType() {
        return VeinType.getVeinType(veinType);
    }
}