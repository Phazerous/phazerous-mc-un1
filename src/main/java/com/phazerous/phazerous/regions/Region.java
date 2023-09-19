package com.phazerous.phazerous.regions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Region {
    private String name;

    private int x1Pos;
    private int z1Pos;
    private int x2Pos;
    private int z2Pos;

    public Region() {
    }

    public static boolean isInRegion(double x, double z, Region region) {
        final int x1 = region.getX1Pos();
        final int x2 = region.getX2Pos();
        final int z1 = region.getZ1Pos();
        final int z2 = region.getZ2Pos();

        return (x1 <= x && x <= x2 || x2 <= x && x <= x1) && (z1 <= z && z <= z2 || z2 <= z && z <= z1);
    }
}
