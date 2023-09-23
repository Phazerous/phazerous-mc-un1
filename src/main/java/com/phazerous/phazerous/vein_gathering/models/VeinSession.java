package com.phazerous.phazerous.vein_gathering.models;

import com.phazerous.phazerous.items.utils.ItemBuilder;
import com.phazerous.phazerous.items.utils.LoreBuilder;
import com.phazerous.phazerous.vein_gathering.enums.VeinToolType;
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

    public void incrementGatheredResources() {
        totalResourceGathered++;
    }

    public ItemStack buildVeinResourceItemStack() {
        int progress = (int) ((double) veinResource.getCurrentDurability() / (double) veinResource.getMaxDurability() * 100);

        LoreBuilder loreBuilder = new LoreBuilder()
                .insert(0, "Effective tool: " + VeinToolType
                        .getTool(currentLayer.getEffectiveToolType())
                        .getName())
                .insert(2, "Durability: " + veinResource.getCurrentDurability() + "/" + veinResource.getMaxDurability())
                .insert(3, "Completion: " + progress + "%")
                .insert(5, "Total gathered: " + totalResourceGathered);


        return new ItemBuilder(currentLayer.getMaterialId())
                .setLore(loreBuilder.build())
                .setAmount(progress / 10)
                .build();
    }
}
