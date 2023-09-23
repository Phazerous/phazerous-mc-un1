package com.phazerous.phazerous.vein_gathering.manager;

import com.phazerous.phazerous.shared.Scheduler;
import com.phazerous.phazerous.vein_gathering.models.VeinTool;
import com.phazerous.phazerous.vein_gathering.repository.VeinGUIRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class VeinGUIManager {
    private final VeinToolsManager veinToolsManager;
    private final VeinGatheringManager veinGatheringManager;
    private final Scheduler scheduler;
    private final VeinGUIRepository veinGUIRepository;


    private final String INVENTORY_NAME = "Gathering";
    private final int INVENTORY_CLOSE_SLOT = 8;
    private final int INVENTORY_RESOURCE_ITEM_SLOT = 22;
    private final int COOL_DOWN_SECONDS = 1;

    private final HashMap<UUID, Boolean> playerInventoryCloseAllowance = new HashMap<>();

    private final Inventory patternInventory;
    private final HashMap<UUID, HashMap<Integer, VeinTool>> playerVeinToolsInSlots = new HashMap<>();
    private final Set<UUID> playersOnCoolDown = new HashSet<>();
    private final HashMap<UUID, Inventory> playersVeinInventory = new HashMap<>();

    public VeinGUIManager(VeinGUIRepository veinGUIRepository, VeinToolsManager veinToolsManager, VeinGatheringManager veinGatheringManager, Scheduler scheduler) {
        this.veinGUIRepository = veinGUIRepository;
        this.veinToolsManager = veinToolsManager;
        this.veinGatheringManager = veinGatheringManager;
        this.scheduler = scheduler;

        patternInventory = createBackgroundInventory();
    }

    /**
     * @param player                The player to build the inventory for
     * @param veinResourceLayerItem The first layer that should be set
     * @return The inventory that was built
     */
    public Inventory buildInventory(Player player, ItemStack veinResourceLayerItem) {
        Inventory inventory = Bukkit.createInventory(null, patternInventory.getSize(), INVENTORY_NAME);

        inventory.setContents(patternInventory.getContents());

        HashMap<Integer, VeinTool> veinToolsInSlots = playerVeinToolsInSlots.get(player.getUniqueId());

        for (Map.Entry<Integer, VeinTool> veinToolInSlot : veinToolsInSlots.entrySet()) {
            inventory.setItem(veinToolInSlot.getKey(), veinToolsManager.buildTool(veinToolInSlot.getValue()));
        }

        inventory.setItem(INVENTORY_RESOURCE_ITEM_SLOT, veinResourceLayerItem);

        playerInventoryCloseAllowance.put(player.getUniqueId(), false);

        playersVeinInventory.put(player.getUniqueId(), inventory);

        return inventory;
    }

    public void setResourceLayerItemStack(Player player, ItemStack veinResourceLayerItem) {
        Inventory inventory = playersVeinInventory.get(player.getUniqueId());

        inventory.setItem(INVENTORY_RESOURCE_ITEM_SLOT, veinResourceLayerItem);
    }

    /**
     * Assigns the vein tools to the slots in the inventory.
     * In order to check the position of inventory click, not by extracting the item from the inventory
     *
     * @param player    The player to assign the vein tools to
     * @param veinTools The vein tools to assign
     */
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
        return inventory
                .getTitle()
                .equalsIgnoreCase(INVENTORY_NAME);
    }

    private List<Integer> getSlotsToPlaceVeinTools() {
        return new ArrayList<>(Arrays.asList(38, 39, 40, 41, 42));
    }

    private Inventory createBackgroundInventory() {
        final int INVENTORY_LINES = 5;

        Inventory inventory = Bukkit.createInventory(null, INVENTORY_LINES * 9, "Gathering Pattern");

        ItemStack backgroundItemInventory = veinGUIRepository.getBackgroundInventoryItem();
        int[] backgroundInventorySlots = veinGUIRepository.getBackgroundInventorySlots();

        for (int slot : backgroundInventorySlots) {
            inventory.setItem(slot, backgroundItemInventory);
        }

        inventory.setItem(INVENTORY_CLOSE_SLOT, veinGUIRepository.getCloseInventoryItem());

        return inventory;
    }

    public boolean isAllowedToCloseInventory(Player player) {
        return playerInventoryCloseAllowance.get(player.getUniqueId());
    }

    public void handleInventoryClose(Player player) {
        playerInventoryCloseAllowance.put(player.getUniqueId(), false);
        playerVeinToolsInSlots.remove(player.getUniqueId());
    }

    public void addDrop(Player player, ItemStack[] itemStacks) {
        Inventory inventory = playersVeinInventory.get(player.getUniqueId());

        for (ItemStack itemStack : itemStacks) {
            inventory.addItem(itemStack);
        }
    }

    private void setCoolDown(Player player) {
        Inventory inventory = playersVeinInventory.get(player.getUniqueId());

        int[] coolDownSlots = veinGUIRepository.getCoolDownSlots();
        ItemStack coolDownItem = veinGUIRepository.getCoolDownItem();

        ItemStack prevItem = inventory.getItem(coolDownSlots[0]);

        for (int slot : coolDownSlots) {
            inventory.setItem(slot, coolDownItem);
        }


        playersOnCoolDown.add(player.getUniqueId());

        scheduler.runTaskLater(() -> {
            for (int slot : coolDownSlots) {
                inventory.setItem(slot, prevItem);
            }

            playersOnCoolDown.remove(player.getUniqueId());
        }, COOL_DOWN_SECONDS * 20);
    }

    private ItemStack[] getDrops(Player player) {
        Inventory inventory = playersVeinInventory.get(player.getUniqueId());

        int[] dropSlots = veinGUIRepository.getDropsSlots();

        ItemStack[] drops = new ItemStack[dropSlots.length];

        for (int i = 0; i < dropSlots.length; i++) {
            drops[i] = inventory.getItem(dropSlots[i]);
        }

        return Arrays
                .stream(drops)
                .filter(Objects::nonNull)
                .toArray(ItemStack[]::new);
    }

    public void handleClick(Player player, int slot) {
        if (slot == INVENTORY_CLOSE_SLOT) {
            playerInventoryCloseAllowance.put(player.getUniqueId(), true);
            player.closeInventory();

            player
                    .getInventory()
                    .addItem(getDrops(player));
            return;
        }

        boolean onCoolDown = playersOnCoolDown.contains(player.getUniqueId());

        if (onCoolDown) return;

        VeinTool veinTool = playerVeinToolsInSlots
                .get(player.getUniqueId())
                .get(slot);

        if (veinTool == null) return;

        veinGatheringManager.handleGather(player, veinTool);
        setCoolDown(player);

    }
}
