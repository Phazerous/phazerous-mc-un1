package com.phazerous.phazerous.resources;

import com.phazerous.phazerous.regions.Region;
import com.phazerous.phazerous.regions.listeners.IRegionChangeObserver;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class _resourceManager implements IRegionChangeObserver {
    private final HashMap<UUID, List<Integer>> playersResourcesIds = new HashMap<>();

    private List<Integer> getPlayerResourcesIds(Player player) {
        if (!playersResourcesIds.containsKey(player.getUniqueId())) {
            playersResourcesIds.put(player.getUniqueId(), new ArrayList<>());
        }

        return playersResourcesIds.get(player.getUniqueId());
    }

    public boolean isPlayerResource(Player player, int entityId) {
        return getPlayerResourcesIds(player).contains(entityId);
    }

    private void spawnResource(Player player, Location location) {
        final int ARMOR_STAND_ENTITY_ID = 78;

        World nmsWorld = ((CraftWorld) player.getWorld()).getHandle();
        EntityArmorStand armorStand = new EntityArmorStand(nmsWorld, location.getX(), location.getY(), location.getZ());

        getPlayerResourcesIds(player).add(armorStand.getId());

        PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(armorStand, ARMOR_STAND_ENTITY_ID);

        sendPacket(player, packet);
    }

    public void despawnResource(Player player, int entityId) {
        List<Integer> playerResourcesIds = getPlayerResourcesIds(player);

        for (int i = 0; i < playerResourcesIds.size(); i++) {
            if (playerResourcesIds.get(i) == entityId) {
                playerResourcesIds.remove(i);
                break;
            }
        }


        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[]{entityId});

        sendPacket(player, packet);
    }

    private void sendPacket(Player player, Packet<PacketListenerPlayOut> packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public void onRegionChange(Player player, Region region) {
        spawnResource(player, new Location(player.getWorld(), -13, 4, 7));
    }
}
