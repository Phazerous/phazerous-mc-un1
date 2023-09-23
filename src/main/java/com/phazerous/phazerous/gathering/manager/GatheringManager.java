package com.phazerous.phazerous.gathering.manager;

import com.phazerous.phazerous.gathering.interfaces.IGatheringStartObserver;
import com.phazerous.phazerous.gathering.models.Vein;
import com.phazerous.phazerous.gathering.models.VeinResourceLayer;
import com.phazerous.phazerous.gathering.models.VeinSession;
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

    private final HashMap<UUID, VeinSession> playerVeinSession = new HashMap<>();
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
        VeinSession veinSession = playerVeinSession.get(player.getUniqueId());

        player.sendMessage("You broke the vein!");
    }

    public void handleGather(Player player, VeinTool veinTool) {
        VeinSession veinSession = playerVeinSession.get(player.getUniqueId());

        boolean isBroken = veinResourceManager.damageVeinResource(veinSession.getVeinResource(), veinTool.getStrength());

        if (isBroken) {
            handleResourceBreak(player);
        }

        //TODO ADD LAYER CHANGING
        veinGUIManager.setResourceLayerItemStack(player, veinSession.buildVeinResourceItemStack());
    }

    //TODO ON GATHERING FINIHS/ABORT
    @Override
    public void onGatheringStart(Player player, int entityId) {
        Vein vein = veinManager.getVein(player, entityId);
        List<VeinTool> tools = veinToolsManager.getPlayerVeinTools(player, vein);

        veinGUIManager.assignVeinToolsToSlot(player, tools);

        VeinSession veinSession = new VeinSession(vein);
        veinSession.setCurrentLayer(new VeinResourceLayer()); //  !!!!

        playerVeinSession.put(player.getUniqueId(), veinSession);

        Inventory inventory = veinGUIManager.buildInventory(player, veinSession.buildVeinResourceItemStack());
        player.openInventory(inventory);
    }
}
