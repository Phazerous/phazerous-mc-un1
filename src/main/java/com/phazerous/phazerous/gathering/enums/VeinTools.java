package com.phazerous.phazerous.gathering.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum VeinTools {
    // 0 = PICKAXE, 1 = HAMMER, 2 = CROWBAR, 3 = CHISEL
    MINING(new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3)));

    private final List<Integer> toolIds;

    VeinTools(List<Integer> toolIds) {
        this.toolIds = toolIds;
    }
}
