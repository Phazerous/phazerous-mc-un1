package com.phazerous.phazerous.vein_gathering.manager;

import com.phazerous.phazerous.items.models.Drop;
import com.phazerous.phazerous.items.utils.DropManager;
import com.phazerous.phazerous.utils.RandomUtils;
import com.phazerous.phazerous.vein_gathering.interfaces.IGatheringStartObserver;
import com.phazerous.phazerous.vein_gathering.models.Vein;
import com.phazerous.phazerous.vein_gathering.models.VeinResourceLayer;
import com.phazerous.phazerous.vein_gathering.models.VeinSession;
import com.phazerous.phazerous.vein_gathering.models.VeinTool;
import com.phazerous.phazerous.vein_gathering.repository.VeinLayersRepository;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class VeinGatheringManager implements IGatheringStartObserver {
    private final VeinManager veinManager;
    private final VeinToolsManager veinToolsManager;
    private final VeinResourceManager veinResourceManager;
    private final VeinLayersRepository veinLayersRepository;
    private final DropManager dropManager;

    private final HashMap<UUID, VeinSession> playerVeinSession = new HashMap<>();
    private VeinGUIManager veinGUIManager;

    public VeinGatheringManager(VeinLayersRepository veinLayersRepository, VeinToolsManager veinToolsManager, VeinManager veinManager, VeinResourceManager veinResourceManager, DropManager dropManager) {
        this.veinLayersRepository = veinLayersRepository;
        this.veinToolsManager = veinToolsManager;
        this.veinManager = veinManager;
        this.veinResourceManager = veinResourceManager;
        this.dropManager = dropManager;
    }

    public void setVeinGUIManager(VeinGUIManager veinGUIManager) {
        this.veinGUIManager = veinGUIManager;
    }

    private void handleResourceBreak(Player player, VeinSession veinSession) {
        veinSession.incrementGatheredResources();
        List<Drop> drops = veinSession
                .getVein()
                .getDrops();

        List<ItemStack> items = dropManager.generateDrop(drops);
        veinGUIManager.addDrop(player, items.toArray(new ItemStack[0]));
    }

    public void handleGather(Player player, VeinTool veinTool) {
        VeinSession veinSession = playerVeinSession.get(player.getUniqueId());

        boolean isBroken = veinResourceManager.damageVeinResource(veinSession.getVeinResource(), veinTool.getStrength());

        if (isBroken) {
            handleResourceBreak(player, veinSession);
        }

        veinSession.setCurrentLayer(getRandomResourceLayer(veinSession.getVein()));
        veinGUIManager.setResourceLayerItemStack(player, veinSession.buildVeinResourceItemStack());
    }

    private VeinResourceLayer getRandomResourceLayer(Vein vein) {
        List<VeinResourceLayer> layers = veinLayersRepository.getVeinLayers(vein.getVeinType());

        return RandomUtils.getRandomElement(layers);
    }

    //TODO ON GATHERING FINIHS/ABORT
    @Override
    public void onGatheringStart(Player player, int entityId) {
        Vein vein = veinManager.getVein(player, entityId);
        List<VeinTool> tools = veinToolsManager.getPlayerVeinTools(player, vein);

        veinGUIManager.assignVeinToolsToSlot(player, tools);

        VeinSession veinSession = new VeinSession(vein);
        veinSession.setCurrentLayer(getRandomResourceLayer(vein));

        playerVeinSession.put(player.getUniqueId(), veinSession);

        Inventory inventory = veinGUIManager.buildInventory(player, veinSession.buildVeinResourceItemStack());
        player.openInventory(inventory);
    }
}
