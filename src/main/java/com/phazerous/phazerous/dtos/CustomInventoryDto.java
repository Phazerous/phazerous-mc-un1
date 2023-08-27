package com.phazerous.phazerous.dtos;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter @Setter
public class CustomInventoryDto {
    private ObjectId id;
    private String title;
    private int size;
    private List<CustomInventoryItemDto> contents;
}
