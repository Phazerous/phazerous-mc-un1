package com.phazerous.phazerous.gathering.manager;

import com.phazerous.phazerous.gathering.models.VeinTool;
import com.phazerous.phazerous.utils.InventoryUtils;
import com.phazerous.phazerous.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class VeinGUIManager {
    private final VeinToolsManager veinToolsManager;

    private final Inventory patternInventory = createBackgroundInventory();
    private final HashMap<UUID, HashMap<Integer, VeinTool>> playerVeinToolsInSlots = new HashMap<>();

    public VeinGUIManager(VeinToolsManager veinToolsManager) {
        this.veinToolsManager = veinToolsManager;
    }

    public Inventory buildInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, patternInventory.getSize(), "Gathering");

        inventory.setContents(patternInventory.getContents());

        HashMap<Integer, VeinTool> veinToolsInSlots = playerVeinToolsInSlots.get(player.getUniqueId());

        for (Map.Entry<Integer, VeinTool> veinToolInSlot : veinToolsInSlots.entrySet()) {
            inventory.setItem(veinToolInSlot.getKey(), veinToolsManager.buildTool(veinToolInSlot.getValue()));
        }

        return inventory;
    }

    public void assignVeinToolsToSlot(Player player, List<VeinTool> veinTools) {
        if (!playerVeinToolsInSlots.containsKey(player.getUniqueId())) {
            playerVeinToolsInSlots.put(player.getUniqueId(), new HashMap<>());
        }

        HashMap<Integer, VeinTool> veinToolsInSlots = playerVeinToolsInSlots.get(player.getUniqueId());

        List<Integer> slotsToPlaceTools = getSlotsToPlaceVeinTools();
        Collections.shuffle(slotsToPlaceTools);

        for (int i = 0; i < veinTools.size(); i++) {
            veinToolsInSlots.put(slotsToPlaceTools.get(i), veinTools.get(i));
        }
    }

    private List<Integer> getSlotsToPlaceVeinTools() {
        return new ArrayList<>(Arrays.asList(38, 39, 40, 41, 42));
    }

    private Inventory createBackgroundInventory() {
        final int INVENTORY_LINES = 5;

        Inventory inventory = Bukkit.createInventory(null, INVENTORY_LINES * 9, "Gathering Pattern");

        ItemStack backgroundItemInventory = getBackgroundInventoryItem();
        int[] backgroundInventorySlots = getBackgroundInventorySlots();

        for (int slot : backgroundInventorySlots) {
            inventory.setItem(slot, backgroundItemInventory);
        }

        return inventory;
    }

    private ItemStack getBackgroundInventoryItem() {
        final Material BACKROUND_MATERIAL = Material.STAINED_GLASS_PANE;
        final int BACKGROUND_MATERIAL_ADDITIONAL_TYPE = 7;

        return new ItemBuilder(BACKROUND_MATERIAL, BACKGROUND_MATERIAL_ADDITIONAL_TYPE).setDisplayName(" ")
                .build();
    }

    private int[] getBackgroundInventorySlots() {
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
}
