package com.phazerous.phazerous.gathering.manager;

import com.phazerous.phazerous.gathering.interfaces.IGatheringStartObserver;
import com.phazerous.phazerous.gathering.models.Vein;
import com.phazerous.phazerous.gathering.models.VeinResource;
import com.phazerous.phazerous.gathering.models.VeinTool;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GatheringManager implements IGatheringStartObserver {
    private final VeinManager veinManager;
    private final VeinToolsManager veinToolsManager;
    private final VeinResourceManager veinResourceManager;
    private final HashMap<UUID, Vein> playerCurrentVein = new HashMap<>();
    private VeinGUIManager veinGUIManager;

    public GatheringManager(VeinToolsManager veinToolsManager, VeinManager veinManager, VeinResourceManager veinResourceManager) {
        this.veinToolsManager = veinToolsManager;
        this.veinManager = veinManager;
        this.veinResourceManager = veinResourceManager;
    }

    public void setVeinGUIManager(VeinGUIManager veinGUIManager) {
        this.veinGUIManager = veinGUIManager;
    }

    private void handleResourceBreak(Player player) {
        Vein vein = playerCurrentVein.get(player.getUniqueId());

        player.sendMessage("You broke the vein!");
    }

    // TODO ON HANDLE CLICK
    public void handleGather(Player player, VeinTool veinTool) {
        boolean isBroken = veinResourceManager.damageVeinResource(player, veinTool.getStrength());

        if (isBroken) {
            handleResourceBreak(player);
        }
    }

    //TODO ON GATHERING FINIHS/ABORT
    @Override
    public void onGatheringStart(Player player, int entityId) {
        Vein vein = veinManager.getVein(player, entityId);
        List<VeinTool> tools = veinToolsManager.getPlayerVeinTools(player, vein);

        playerCurrentVein.put(player.getUniqueId(), vein);
        VeinResource veinResource = veinResourceManager.prepareVeinResource(player, vein);

        veinGUIManager.assignResourceItem(player, veinResource.getLayerItem());
        veinGUIManager.assignVeinToolsToSlot(player, tools);
        Inventory inventory = veinGUIManager.buildInventory(player);
        player.openInventory(inventory);
    }
}
