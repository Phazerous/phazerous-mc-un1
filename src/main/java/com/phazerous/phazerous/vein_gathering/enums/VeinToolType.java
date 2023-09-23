package com.phazerous.phazerous.vein_gathering.enums;

import lombok.Getter;

@Getter
public enum VeinToolType {
    PICKAXE(0, "Pickaxe"), HAMMER(1, "Hammer"), CROWBAR(2, "Crowbar"), CHISEL(3, "Chisel");

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
