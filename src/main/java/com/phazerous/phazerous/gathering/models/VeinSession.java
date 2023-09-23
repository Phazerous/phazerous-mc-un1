package com.phazerous.phazerous.gathering.models;

import com.phazerous.phazerous.utils.ItemBuilder;
import com.phazerous.phazerous.utils.LoreBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class VeinSession {
    private final VeinResource veinResource;
    private Vein vein;
    private int totalResourceGathered = 0;
    private VeinResourceLayer currentLayer;

    public VeinSession(Vein vein) {
        this.vein = vein;
        veinResource = new VeinResource(vein.getResourceDurability());
    }

    public void addGatheredResource() {
        totalResourceGathered++;
    }

    public ItemStack buildVeinResourceItemStack() {
        LoreBuilder loreBuilder = new LoreBuilder()
                .insert(0, "Effective tool")
                .insert(2, "Durability: " + veinResource.getCurrentDurability() + "/" + veinResource.getMaxDurability())
                .insert(3, "Completion: " + (int) ((double) veinResource.getCurrentDurability() / (double) veinResource.getMaxDurability() * 100) + "%")
                .insert(5, "Total gathered: " + totalResourceGathered);

        return new ItemBuilder(currentLayer.getMaterialType())
                .setLore(loreBuilder.build())
                .build();
    }
}
