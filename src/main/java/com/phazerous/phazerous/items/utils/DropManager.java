package com.phazerous.phazerous.items.utils;

import com.phazerous.phazerous.items.ItemManager;
import com.phazerous.phazerous.items.models.Drop;
import com.phazerous.phazerous.utils.RandomUtils;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DropManager {
    private final ItemManager itemManager;

    public DropManager(ItemManager itemManager) {
        this.itemManager = itemManager;
    }
    
    /**
     * Generates a drop from a list of drops
     *
     * @param drops List of drops
     * @return List of ItemStacks
     */
    public List<ItemStack> generateDrop(List<Drop> drops) {
        List<ItemStack> items = new ArrayList<>();

        for (Drop drop : drops) {
            int chance = drop.getChance();
            int randomValue = RandomUtils.getRandomInt(0, 100);

            if (randomValue <= chance) {
                int amount = RandomUtils.getAmount(drop.getAmount());
                ItemStack item = itemManager.buildItem(drop.getItemId());
                item.setAmount(amount);
                items.add(item);
            }
        }

        return items;
    }
}
