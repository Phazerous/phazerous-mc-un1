package com.phazerous.phazerous.dtos;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class PlayerBalanceDto {
    private UUID playerUUID;
    private float balance;
}
