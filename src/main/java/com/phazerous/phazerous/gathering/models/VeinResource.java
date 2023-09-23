package com.phazerous.phazerous.gathering.models;

import lombok.Getter;

@Getter
public class VeinResource {
    private final int maxDurability;
    private int currentDurability;

    public VeinResource(int maxDurability) {
        this.maxDurability = maxDurability;
        this.currentDurability = maxDurability;
    }

    public void restoreDurability() {
        currentDurability = maxDurability;
    }

    public void setCurrentDurability(int currentDurability) {
        this.currentDurability = currentDurability;
    }
}
