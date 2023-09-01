package com.phazerous.phazerous.economy.models;


import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.UUID;

@Getter
@Setter
public class PlayerBalance {
    private ObjectId _id;
    private UUID playerUUID;
    private Double balance;
}
