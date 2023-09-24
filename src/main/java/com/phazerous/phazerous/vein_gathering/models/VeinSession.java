package com.phazerous.phazerous.vein_gathering.models;

import com.phazerous.phazerous.items.utils.ItemBuilder;
import com.phazerous.phazerous.items.utils.LoreBuilder;
import com.phazerous.phazerous.vein_gathering.enums.VeinToolType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class VeinSession {
    private final VeinResource veinResource;
    private Vein vein;
    private int totalResourceGathered = 0;
    private VeinResourceLayer currentLayer;
    private int turnsLeft;

    public VeinSession(Vein vein) {
        final int DEFAULT_TURNS_LEFT = 10;

        this.vein = vein;
        veinResource = new VeinResource(vein.getResourceDurability());
        turnsLeft = DEFAULT_TURNS_LEFT;
    }

    public void incrementGatheredResources() {
        totalResourceGathered++;
    }

    public void decreaseTurnsLeft(int amount) {
        turnsLeft -= amount;
    }

    public ItemStack buildVeinResourceItemStack() {
        int progress = (int) ((double) veinResource.getCurrentDurability() / (double) veinResource.getMaxDurability() * 100);

        LoreBuilder loreBuilder = new LoreBuilder()
                .insert(0, "Effective tool: " + VeinToolType
                        .getTool(currentLayer.getEffectiveToolType())
                        .getName())
                .insert(2, "Durability: " + veinResource.getCurrentDurability() + "/" + veinResource.getMaxDurability())
                .insert(3, "Completion: " + progress + "%")
                .insert(5, "Turns left: " + turnsLeft)
                .insert(6, "Total gathered: " + totalResourceGathered);


        return new ItemBuilder(currentLayer.getMaterialId())
                .setLore(loreBuilder.build())
                .setAmount(progress / 10)
                .build();
    }

    public ItemStack buildTurnsOutItemStack() {
        final Material TURNS_OUT_MATERIAL = Material.STAINED_GLASS_PANE;
        final int TURNS_OUT_MATERIAL_ADDITIONAL_TYPE = 13;

        LoreBuilder loreBuilder = new LoreBuilder().insert(1, "Total gathered: " + totalResourceGathered);

        return new ItemBuilder(TURNS_OUT_MATERIAL, TURNS_OUT_MATERIAL_ADDITIONAL_TYPE)
                .setDisplayName("Turns out")
                .setLore(loreBuilder)
                .build();
    }
}
