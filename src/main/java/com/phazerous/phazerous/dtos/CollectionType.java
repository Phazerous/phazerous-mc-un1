package com.phazerous.phazerous.dtos;

import lombok.Getter;

@Getter
public enum CollectionType {
    LOCATIONED_ENTITY("locationed_entities"),
    ENTITY("entities"),
    ITEM("items"),
    RUNTIME_ENTITY("runtime_entities");

    private final String collectionName;

    CollectionType(String collectionName) {
        this.collectionName = collectionName;
    }

    public static CollectionType getCollectionTypeByClass(Class<?> clazz) {
        if (clazz == LocationedEntityDto.class) {
            return LOCATIONED_ENTITY;
        } else if (clazz == EntityDto.class) {
            return ENTITY;
        } else if (clazz == ItemDto.class) {
            return ITEM;
        } else if (clazz == RuntimeEntityDto.class) {
            return RUNTIME_ENTITY;
        }

        return null;
    }
}
