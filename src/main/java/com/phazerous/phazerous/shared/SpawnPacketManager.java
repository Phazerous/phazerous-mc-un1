package com.phazerous.phazerous.shared;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SpawnPacketManager {
    public int spawnArmorStand(Player player, Location location) {
        final int ARMOR_STAND_ENTITY_ID = 78;

        World nmsWorld = ((CraftWorld) player.getWorld()).getHandle();
        EntityArmorStand armorStand = new EntityArmorStand(nmsWorld, location.getX(), location.getY(), location.getZ());

        PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(armorStand, ARMOR_STAND_ENTITY_ID);
        sendPacket(player, packet);

        return armorStand.getId();
    }

    public void despawnVeinEntity(Player player, int entityId) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityId);
        sendPacket(player, packet);
    }

    private void sendPacket(Player player, Packet<PacketListenerPlayOut> packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
