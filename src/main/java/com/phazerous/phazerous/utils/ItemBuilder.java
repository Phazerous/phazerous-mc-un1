package com.phazerous.phazerous.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    private final ItemStack instance;
    private final ItemMeta meta;
    private List<String> lore;

    public ItemBuilder(ItemStack instance) {
        this.instance = instance;
        meta = instance.getItemMeta();
        lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
    }

    public ItemBuilder(int materialId) {
        this(new ItemStack(materialId));
    }

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(Material material, int additionalType) {
        this(new ItemStack(material, 1, (short) additionalType));
    }

    public ItemBuilder setDisplayName(String name) {
        meta.setDisplayName(name);
        return this;
    }

    public ItemBuilder addLore(String line) {
        lore.add(ChatColor.RESET + line);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    private ItemBuilder hideAttributes() {
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        return this;
    }

    public ItemStack build() {
        hideAttributes();

        meta.setLore(lore);
        instance.setItemMeta(meta);
        return instance;
    }
}
