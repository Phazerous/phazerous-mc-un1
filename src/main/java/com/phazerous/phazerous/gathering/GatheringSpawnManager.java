package com.phazerous.phazerous.gathering;

import com.phazerous.phazerous.gathering.models.VeinLocation;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GatheringSpawnManager {
    private final HashMap<UUID, List<Integer>> playerVeinsEntitiesIds = new HashMap<>();

    public boolean isVeinEntity(Player player, int entityId) {
        return getPlayerVeinsEntitiesIds(player).contains(entityId);
    }

    public int spawnVeinEntity(Player player, VeinLocation veinLocation) {
        final int ARMOR_STAND_ENTITY_ID = 78;

        World nmsWorld = ((CraftWorld) player.getWorld()).getHandle();
        EntityArmorStand armorStand = new EntityArmorStand(nmsWorld, veinLocation.getX(), veinLocation.getY(), veinLocation.getZ());

        getPlayerVeinsEntitiesIds(player).add(armorStand.getId());

        PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(armorStand, ARMOR_STAND_ENTITY_ID);
        sendPacket(player, packet);

        return armorStand.getId();
    }

    public void despawnVeinEntity(Player player, int entityId) {
        List<Integer> playerVeinsIds = getPlayerVeinsEntitiesIds(player);

        for (int i = 0; i < playerVeinsIds.size(); i++) {
            if (playerVeinsIds.get(i) == entityId) {
                playerVeinsIds.remove(i);
                break;
            }
        }

        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityId);
        sendPacket(player, packet);
    }

    private List<Integer> getPlayerVeinsEntitiesIds(Player player) {
        if (!playerVeinsEntitiesIds.containsKey(player.getUniqueId())) {
            playerVeinsEntitiesIds.put(player.getUniqueId(), new ArrayList<>());
        }

        return playerVeinsEntitiesIds.get(player.getUniqueId());
    }

    private void sendPacket(Player player, Packet<PacketListenerPlayOut> packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
