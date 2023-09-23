package com.phazerous.phazerous.vein_gathering.enums;

import lombok.Getter;

@Getter
public enum VeinToolType {
    HAND(0, "HAND"), PICKAXE(1, "Pickaxe"), HAMMER(2, "Hammer"), CROWBAR(3, "Crowbar"), CHISEL(4, "Chisel");

    private final int typeId;
    private final String name;

    VeinToolType(int typeId, String name) {
        this.typeId = typeId;
        this.name = name;
    }

    public static VeinToolType getTool(int typeId) {
        for (VeinToolType veinToolType : values()) {
            if (veinToolType.getTypeId() == typeId) {
                return veinToolType;
            }
        }

        return null;
    }
}
