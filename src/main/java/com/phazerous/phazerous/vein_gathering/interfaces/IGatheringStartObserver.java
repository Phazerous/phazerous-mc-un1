package com.phazerous.phazerous.vein_gathering.interfaces;

import org.bukkit.entity.Player;

public interface IGatheringStartObserver {
    void onGatheringStart(Player player, int entityId);
}
