package com.phazerous.phazerous.gathering.repository;

import com.phazerous.phazerous.gathering.enums.VeinToolType;
import com.phazerous.phazerous.gathering.enums.VeinType;
import com.phazerous.phazerous.gathering.models.VeinResourceLayer;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VeinLayersRepository {
    public final HashMap<VeinType, List<VeinResourceLayer>> layersByVeinType = new HashMap<>();

    public VeinLayersRepository() {
        fillMiningVeinLayers();
    }

    public List<VeinResourceLayer> getVeinLayers(VeinType veinType) {
        return layersByVeinType.get(veinType);
    }

    private void fillMiningVeinLayers() {
        layersByVeinType.put(VeinType.MINING, new ArrayList<VeinResourceLayer>() {{
            add(new VeinResourceLayer(Material.STONE, VeinToolType.PICKAXE));
            add(new VeinResourceLayer(Material.COBBLESTONE, VeinToolType.HAMMER));
            add(new VeinResourceLayer(Material.CLAY, VeinToolType.CHISEL));
            add(new VeinResourceLayer(Material.SMOOTH_BRICK, VeinToolType.HAMMER));
        }});
    }
}
