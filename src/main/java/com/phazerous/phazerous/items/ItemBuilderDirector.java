package com.phazerous.phazerous.items;

import com.phazerous.phazerous.items.enums.ItemType;
import com.phazerous.phazerous.items.enums.RarityType;
import com.phazerous.phazerous.items.models.items.CustomItem;
import com.phazerous.phazerous.items.models.items.ResourceItem;
import com.phazerous.phazerous.items.utils.ItemBuilder;
import org.bukkit.inventory.ItemStack;

public class ItemBuilderDirector {
    public ItemStack buildItem(CustomItem itemMode, ItemType itemType) throws Exception {
        switch (itemType) {
            case RESOURCE:
                return createResource((ResourceItem) itemMode);
            default:
                throw new Exception("Invalid item type");
        }
    }

    private ItemStack createResource(ResourceItem resourceItem) {
        ItemBuilder itemBuilder = createBaseItem(resourceItem);
        return itemBuilder.build();
    }

    private ItemBuilder createBaseItem(CustomItem customItem) {
        ItemBuilder itemBuilder = null;

        if (customItem.getAdditionalMaterialType() == null)
            itemBuilder = new ItemBuilder(customItem.getMaterialType());
        else
            itemBuilder = new ItemBuilder(customItem.getMaterialType(), customItem.getAdditionalMaterialType());

        RarityType rarityType = customItem.getRarityType();

        return itemBuilder
                .setDisplayName(customItem.getTitle())
                .setRarity(rarityType);
    }
}
