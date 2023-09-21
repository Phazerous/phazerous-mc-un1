package com.phazerous.phazerous.message_dispatcher;

import com.phazerous.phazerous.regions.interfaces.IRegionChangeObserver;
import com.phazerous.phazerous.regions.models.Region;
import org.bukkit.entity.Player;

public class MessageDispatcher implements IRegionChangeObserver {
    public static void dispatchMessage(Player player, String message) {
        player.sendMessage(message);
    }


    @Override
    public void onRegionChange(Player player, Region region) {
        if (region != null) {
            dispatchMessage(player, "You are now in " + region.getName());

        }
    }
}
