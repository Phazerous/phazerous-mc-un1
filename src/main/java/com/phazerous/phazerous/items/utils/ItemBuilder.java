package com.phazerous.phazerous.items.utils;

import com.phazerous.phazerous.items.enums.RarityType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {
    private final ItemStack instance;
    private final ItemMeta meta;
    private LoreBuilder loreBuilder;
    private RarityType rarityType;

    public ItemBuilder(ItemStack instance) {
        this.instance = instance;
        meta = instance.getItemMeta();
        loreBuilder = meta.hasLore() ? new LoreBuilder(meta.getLore()) : new LoreBuilder();
    }

    public ItemBuilder(int materialId) {
        this(new ItemStack(materialId));
    }

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(int materialId, int additionalType) {
        this(new ItemStack(materialId, 1, (short) additionalType));
    }

    public ItemBuilder(Material material, int additionalType) {
        this(new ItemStack(material, 1, (short) additionalType));
    }

    public ItemBuilder setDisplayName(String name) {
        meta.setDisplayName(name);
        return this;
    }

    public ItemBuilder addLore(String line) {
        loreBuilder.add(line);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        instance.setAmount(amount);
        return this;
    }

    public ItemBuilder setLore(LoreBuilder loreBuilder) {
        this.loreBuilder = loreBuilder;
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        loreBuilder = new LoreBuilder(lore);
        return this;
    }

    public ItemBuilder setRarity(RarityType rarityType) {
        this.rarityType = rarityType;
        return this;
    }

    private ItemBuilder hideAttributes() {
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        return this;
    }

    private boolean applyRarityStyles() {
        if (rarityType == null) return false;

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('§', rarityType.getColor() + meta.getDisplayName()));

        loreBuilder.add("");
        loreBuilder.add(rarityType.getColor() + rarityType.getTitle());

        return true;
    }

    public ItemStack build() {
        hideAttributes();

        if (!applyRarityStyles()) // IF STYLES DIDN'T APPLY, THEN REMOVE ITALIC FROM ITEM NAME
            meta.setDisplayName(ChatColor.RESET + meta.getDisplayName());

        meta.setLore(loreBuilder.build());
        instance.setItemMeta(meta);
        return instance;
    }
}
