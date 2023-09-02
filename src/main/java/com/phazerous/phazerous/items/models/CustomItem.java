package com.phazerous.phazerous.items.models;

import com.phazerous.phazerous.items.enums.RarityType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class CustomItem {
    private ObjectId _id;

    private String title;
    private Integer type;
    private Integer materialType;
    private Integer additionalMaterialType;

    @Getter(AccessLevel.NONE)
    private Integer rarity;

    public RarityType getRarityType() {
        return RarityType.getRarityTypeByValue(rarity);
    }
}