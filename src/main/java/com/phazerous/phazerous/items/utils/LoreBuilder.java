package com.phazerous.phazerous.items.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class LoreBuilder {
    private final List<String> instance = new ArrayList<>();

    public LoreBuilder() {
    }

    public LoreBuilder(List<String> lore) {
        instance.addAll(lore);
    }

    public LoreBuilder add(String line) {
        instance.add(format(line));
        return this;
    }

    public LoreBuilder insert(int index, String line) {
        fillSpacesTill(index);

        instance.add(index, format(line));
        return this;
    }

    private void fillSpacesTill(int index) {
        int loreSize = instance.size();

        while (loreSize < index) {
            instance.add("");
            loreSize++;
        }
    }

    public List<String> build() {
        return instance;
    }

    public int getSize() {
        return instance.size();
    }

    private String format(String line) {
        return ChatColor.translateAlternateColorCodes('§', ChatColor.RESET + line);
    }
}
