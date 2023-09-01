package com.phazerous.phazerous.entities.models.entities;

import com.phazerous.phazerous.entities.models.misc.EntityDrop;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
public class BaseEntity {
    private ObjectId _id;
    private String title;
    private Integer entityType;
    private Long respawnTime;
    private List<EntityDrop> drops;
}
