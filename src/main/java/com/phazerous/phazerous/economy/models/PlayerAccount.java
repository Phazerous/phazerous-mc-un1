package com.phazerous.phazerous.economy.models;


import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.UUID;

@Getter
@Setter
public class PlayerAccount {
    private ObjectId _id;
    private UUID playerUUID;
    private Long balance;
}
