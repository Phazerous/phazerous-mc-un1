package com.phazerous.phazerous.gathering.manager;

import com.phazerous.phazerous.gathering.interfaces.IGatheringStartObserver;
import com.phazerous.phazerous.gathering.models.Vein;
import com.phazerous.phazerous.gathering.models.VeinTool;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class GatheringManager implements IGatheringStartObserver {
    private final VeinManager veinManager;
    private final VeinToolsManager veinToolsManager;
    private final VeinGUIManager veinGUIManager;


    public GatheringManager(VeinToolsManager veinToolsManager, VeinManager veinManager, VeinGUIManager veinGUIManager) {
        this.veinToolsManager = veinToolsManager;
        this.veinManager = veinManager;
        this.veinGUIManager = veinGUIManager;
    }

    //TODO ON GATHERING FINIHS/ABORT
    @Override
    public void onGatheringStart(Player player, int entityId) {
        Vein vein = veinManager.getVein(player, entityId);
        List<VeinTool> tools = veinToolsManager.getPlayerVeinTools(player, vein);

        veinGUIManager.assignVeinToolsToSlot(player, tools);
        Inventory inventory = veinGUIManager.buildInventory(player);
        player.openInventory(inventory);
    }
}
