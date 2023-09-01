package com.phazerous.phazerous.entities.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MobEntity extends BaseEntity {
    private Integer mobType;
    private Double health;
    private Double attack;
}
