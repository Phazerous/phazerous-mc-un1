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
    private final List<String> lore;

    public ItemBuilder(int materialId) {
        this(Material.getMaterial(materialId));
    }

    public ItemBuilder(Material material) {
        instance = new ItemStack(material);
        meta = instance.getItemMeta();
        lore = new ArrayList<>();
    }


    public ItemBuilder setDisplayName(String name) {
        meta.setDisplayName(name);
        return this;
    }

    public ItemBuilder addLore(String line) {
        lore.add(ChatColor.RESET + line);
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
