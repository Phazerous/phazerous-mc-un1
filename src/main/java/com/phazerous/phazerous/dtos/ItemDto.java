package com.phazerous.phazerous.dtos;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter @Setter
public class ItemDto {
    private ObjectId id;

    private String title;
    private byte type;
    private int materialType;
}
