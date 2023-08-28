package com.phazerous.phazerous.dtos;

import com.phazerous.phazerous.enums.RarityType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter @Setter
public class ItemDto {
    private ObjectId id;

    private String title;
    private byte type;
    private int materialType;
    private byte additionalMaterialType;

    @Getter(AccessLevel.NONE)
    private int rarity;

    public RarityType getRarityType() {
        return RarityType.getRarityTypeByValue(rarity);
    }
}