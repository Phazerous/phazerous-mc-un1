package com.phazerous.phazerous.vein_gathering.repository;

import com.phazerous.phazerous.items.utils.ItemBuilder;
import com.phazerous.phazerous.utils.InventoryUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
public class VeinGUIRepository {
    private final ItemStack coolDownItem;
    private final ItemStack backgroundInventoryItem;
    private final ItemStack closeInventoryItem;

    public VeinGUIRepository() {
        coolDownItem = createCoolDownItem();
        backgroundInventoryItem = createBackgroundInventoryItem();
        closeInventoryItem = createCloseInventoryItem();
    }

    public ItemStack createBackgroundInventoryItem() {
        final Material BACKROUND_MATERIAL = Material.STAINED_GLASS_PANE;
        final int BACKGROUND_MATERIAL_ADDITIONAL_TYPE = 7;

        return new ItemBuilder(BACKROUND_MATERIAL, BACKGROUND_MATERIAL_ADDITIONAL_TYPE)
                .setDisplayName(" ")
                .build();
    }

    private ItemStack createCloseInventoryItem() {
        final Material CLOSE_MATERIAL = Material.STAINED_GLASS_PANE;
        final int CLOSE_MATERIAL_ADDITIONAL_TYPE = 14;

        return new ItemBuilder(CLOSE_MATERIAL, CLOSE_MATERIAL_ADDITIONAL_TYPE)
                .setDisplayName("Close")
                .build();
    }

    private ItemStack createCoolDownItem() {
        final Material COOL_DOWN_MATERIAL = Material.STAINED_GLASS_PANE;
        final int COOL_DOWN_MATERIAL_ADDITIONAL_TYPE = 4;

        return new ItemBuilder(COOL_DOWN_MATERIAL, COOL_DOWN_MATERIAL_ADDITIONAL_TYPE)
                .setDisplayName(" ")
                .build();
    }

    public int[] getBackgroundInventorySlots() {
        List<String> pattern = new ArrayList<String>() {
            {
                add("xx-----xx");
                add("xxxxxxxxx");
                add("xxxx-xxxx");
                add("xxxxxxxxx");
                add("xx-----xx");
            }
        };

        return InventoryUtils.getItemsSlotsByPattern(pattern);
    }

    public int[] getDropsSlots() {
        List<String> pattern = new ArrayList<String>() {
            {
                add("  xxxxx  ");
            }
        };

        return InventoryUtils.getItemsSlotsByPattern(pattern);
    }

    public int[] getCoolDownSlots() {
        List<String> pattern = new ArrayList<String>() {
            {
                add("x       x");
            }
        };

        return InventoryUtils.getItemsSlotsByPattern(pattern, 2);
    }
}
