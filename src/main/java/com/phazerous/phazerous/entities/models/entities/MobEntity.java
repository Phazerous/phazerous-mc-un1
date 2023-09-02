package com.phazerous.phazerous.entities.models.entities;

import com.phazerous.phazerous.entities.models.misc.MoneyReward;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MobEntity extends BaseEntity {
    private Integer mobType;
    private Long maxHealth;
    private Double attack;
    private MoneyReward moneyReward;
}
