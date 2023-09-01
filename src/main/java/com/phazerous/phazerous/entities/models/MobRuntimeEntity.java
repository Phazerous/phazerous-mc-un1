package com.phazerous.phazerous.entities.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MobRuntimeEntity extends BaseRuntimeEntity {
    private Double health;
    private Double maxHealth;
    private Double attack;
}
