package com.phazerous.phazerous.models;

import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.UUID;

@Getter
public class PlayerModel {
    private ObjectId _id;
    private UUID uuid;
    private List<PlayerVeinToolMeta> tools;
}
