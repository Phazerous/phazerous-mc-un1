package com.phazerous.phazerous.vein_gathering.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum VeinType {
    MINING(0, new ArrayList<>(Arrays.asList(VeinToolType.PICKAXE, VeinToolType.HAMMER, VeinToolType.CROWBAR, VeinToolType.CHISEL)));

    private final int typeId;
    private final List<VeinToolType> tools;

    VeinType(int typeId, List<VeinToolType> tools) {
        this.typeId = typeId;
        this.tools = tools;
    }

    public static VeinType getVeinType(int typeId) {
        for (VeinType veinType : values()) {
            if (veinType.getTypeId() == typeId) {
                return veinType;
            }
        }

        return null;
    }
}
