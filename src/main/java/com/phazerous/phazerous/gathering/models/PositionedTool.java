package com.phazerous.phazerous.gathering.models;

import lombok.Getter;

@Getter
public class PositionedTool {
    private final int slot;
    private final MiningPickaxe pickaxe;

    public PositionedTool(int slot, MiningPickaxe pickaxe) {
        this.slot = slot;
        this.pickaxe = pickaxe;
    }
}
