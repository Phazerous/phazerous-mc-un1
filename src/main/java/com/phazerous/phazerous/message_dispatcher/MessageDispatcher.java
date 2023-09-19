package com.phazerous.phazerous.message_dispatcher;

import com.phazerous.phazerous.regions.Region;
import com.phazerous.phazerous.regions.listeners.IRegionChangeObserver;
import org.bukkit.entity.Player;

public class MessageDispatcher implements IRegionChangeObserver {
    public static void dispatchMessage(Player player, String message) {
        player.sendMessage(message);
    }


    @Override
    public void onRegionChange(Player player, Region region) {
        dispatchMessage(player, "You are now in " + region.getName());
    }
}
