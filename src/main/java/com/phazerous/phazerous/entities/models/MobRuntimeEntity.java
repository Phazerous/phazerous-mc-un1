package com.phazerous.phazerous.entities.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MobRuntimeEntity extends BaseRuntimeEntity {
    private String title;
    private Long health;
    private Long maxHealth;
    private Double attack;
}
