package com.phazerous.phazerous.gathering.manager;

import com.phazerous.phazerous.gathering.models.Vein;
import com.phazerous.phazerous.gathering.models.VeinResource;
import com.phazerous.phazerous.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class VeinResourceManager {
    private final HashMap<UUID, VeinResource> playersVeinResource = new HashMap<>();

    /**
     * Damages the vein resource, setting the ItemStack amount for the player representing the durability
     * If the resource is broken, then `true` returns and the resource updates. Otherwise, `false` returns
     *
     * @param player
     * @param damage
     */
    public boolean damageVeinResource(Player player, int damage) {
        VeinResource veinResource = playersVeinResource.get(player.getUniqueId());

        if (veinResource.getCurrentDurability() - damage <= 0) {
            veinResource.restoreDurability();
            return true;
        } else {
            veinResource.setCurrentDurability(veinResource.getCurrentDurability() - damage);
            return false;
        }
    }

    public VeinResource prepareVeinResource(Player player, Vein vein) {
        VeinResource veinResource = new VeinResource(vein.getResourceDurability(), new ItemBuilder(Material.DIAMOND).build());
        playersVeinResource.put(player.getUniqueId(), veinResource);
        
        return veinResource;
    }
}
