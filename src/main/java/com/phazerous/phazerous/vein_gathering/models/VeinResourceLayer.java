package com.phazerous.phazerous.vein_gathering.models;

import com.phazerous.phazerous.vein_gathering.enums.VeinToolType;
import lombok.Getter;
import org.bukkit.Material;


@Getter
public class VeinResourceLayer {
    private final Material material;
    private final VeinToolType effectiveToolType;

    public VeinResourceLayer(Material material, VeinToolType effectiveToolType) {
        this.material = material;
        this.effectiveToolType = effectiveToolType;
    }
}
