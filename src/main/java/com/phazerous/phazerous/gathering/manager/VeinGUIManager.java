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
    private final GatheringManager gatheringManager;
    private final String INVENTORY_NAME = "Gathering";
    private final int INVENTORY_CLOSE_SLOT = 8;

    private final HashMap<UUID, Boolean> playerInventoryCloseAllowance = new HashMap<>();

    private final Inventory patternInventory = createBackgroundInventory();
    private final HashMap<UUID, HashMap<Integer, VeinTool>> playerVeinToolsInSlots = new HashMap<>();
    private final HashMap<UUID, ItemStack> playerResource = new HashMap<>();

    public VeinGUIManager(VeinToolsManager veinToolsManager, GatheringManager gatheringManager) {
        this.veinToolsManager = veinToolsManager;
        this.gatheringManager = gatheringManager;
    }

    public Inventory buildInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, patternInventory.getSize(), INVENTORY_NAME);

        inventory.setContents(patternInventory.getContents());

        HashMap<Integer, VeinTool> veinToolsInSlots = playerVeinToolsInSlots.get(player.getUniqueId());

        for (Map.Entry<Integer, VeinTool> veinToolInSlot : veinToolsInSlots.entrySet()) {
            inventory.setItem(veinToolInSlot.getKey(), veinToolsManager.buildTool(veinToolInSlot.getValue()));
        }

        inventory.setItem(22, playerResource.get(player.getUniqueId()));

        playerInventoryCloseAllowance.put(player.getUniqueId(), false);

        return inventory;
    }

    public void assignResourceItem(Player player, ItemStack itemStack) {
        playerResource.put(player.getUniqueId(), itemStack);
    }

    public ItemStack getResourceItem(Player player) {
        return playerResource.get(player.getUniqueId());
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

    public boolean isVeinInventory(Inventory inventory) {
        return inventory.getTitle().equalsIgnoreCase(INVENTORY_NAME);
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

        inventory.setItem(INVENTORY_CLOSE_SLOT, getCloseInventoryItem());

        return inventory;
    }

    private ItemStack getBackgroundInventoryItem() {
        final Material BACKROUND_MATERIAL = Material.STAINED_GLASS_PANE;
        final int BACKGROUND_MATERIAL_ADDITIONAL_TYPE = 7;

        return new ItemBuilder(BACKROUND_MATERIAL, BACKGROUND_MATERIAL_ADDITIONAL_TYPE).setDisplayName(" ")
                .build();
    }

    private ItemStack getCloseInventoryItem() {
        final Material CLOSE_MATERIAL = Material.STAINED_GLASS_PANE;
        final int CLOSE_MATERIAL_ADDITIONAL_TYPE = 14;

        return new ItemBuilder(CLOSE_MATERIAL, CLOSE_MATERIAL_ADDITIONAL_TYPE).setDisplayName("Close")
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

    public boolean isAllowedToCloseInventory(Player player) {
        return playerInventoryCloseAllowance.get(player.getUniqueId());
    }

    public void handleInventoryClose(Player player) {
        playerInventoryCloseAllowance.put(player.getUniqueId(), false);
        playerVeinToolsInSlots.remove(player.getUniqueId());
        playerResource.remove(player.getUniqueId());
    }

    public void handleClick(Player player, int slot) {
        if (slot == INVENTORY_CLOSE_SLOT) {
            playerInventoryCloseAllowance.put(player.getUniqueId(), true);
            player.closeInventory();
            return;
        }

        VeinTool veinTool = playerVeinToolsInSlots.get(player.getUniqueId()).get(slot);

        if (veinTool == null) return;

        gatheringManager.handleGather(player, veinTool);
    }
}
