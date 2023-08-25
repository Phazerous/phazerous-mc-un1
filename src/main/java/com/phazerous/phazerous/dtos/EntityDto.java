package com.phazerous.phazerous.dtos;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter @Setter
public class EntityDto {
    private ObjectId id;

    private String title;
    private byte type;
    private long respawnTime;

    private long hardness;

    private List<ObjectId> drops;
}
