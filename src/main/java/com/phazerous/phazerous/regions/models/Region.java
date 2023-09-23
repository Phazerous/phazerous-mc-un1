package com.phazerous.phazerous.regions.models;

import com.phazerous.phazerous.vein_gathering.models.VeinLocation;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
public class Region {
    private ObjectId _id;

    private String name;

    private Integer x1;
    private Integer z1;

    private Integer x2;
    private Integer z2;

    private List<VeinLocation> veinsLocations;

    public static boolean isInRegion(double x, double z, Region region) {
        final int x1 = region.getX1();
        final int x2 = region.getX2();
        final int z1 = region.getZ1();
        final int z2 = region.getZ2();

        return (x1 <= x && x <= x2 || x2 <= x && x <= x1) && (z1 <= z && z <= z2 || z2 <= z && z <= z1);
    }
}