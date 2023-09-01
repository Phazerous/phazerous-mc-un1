package com.phazerous.phazerous.entities.models.runtime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RuntimeMobEntity extends RuntimeBaseEntity {
    private String title;
    private Long health;
    private Long maxHealth;
    private Double attack;
}
