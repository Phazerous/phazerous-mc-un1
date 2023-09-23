package com.phazerous.phazerous.gathering.manager;

import com.phazerous.phazerous.gathering.models.VeinResource;

public class VeinResourceManager {

    /**
     * Damages the vein resource, setting the ItemStack amount for the player representing the durability
     * If the resource is broken, then `true` returns and the resource updates. Otherwise, `false` returns
     *
     * @param veinResource The vein resource to damage
     * @param damage       The amount of damage to deal
     */
    public boolean damageVeinResource(VeinResource veinResource, int damage) {
        if (veinResource.getCurrentDurability() - damage <= 0) {
            veinResource.restoreDurability();
            return true;
        } else {
            veinResource.setCurrentDurability(veinResource.getCurrentDurability() - damage);
            return false;
        }
    }
}
