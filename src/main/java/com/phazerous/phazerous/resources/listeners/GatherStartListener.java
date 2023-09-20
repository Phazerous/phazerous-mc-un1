package com.phazerous.phazerous.resources.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.phazerous.phazerous.resources._resourceManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class GatherStartListener {

    private final JavaPlugin plugin;
    private final _resourceManager resourceManager;

    public GatherStartListener(JavaPlugin plugin, _resourceManager resourceManager) {
        this.plugin = plugin;
        this.resourceManager = resourceManager;
    }


    public void enable() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        protocolManager.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();

                PacketContainer packet = event.getPacket();
                PacketType packetType = event.getPacketType();

                int targetId = packet.getIntegers().read(0);

                if (resourceManager.isPlayerResource(player, targetId)) {
                    player.sendMessage("Resource detected");
                    resourceManager.despawnResource(player, targetId);
                }

                player.sendMessage(String.valueOf(packet.getEntityUseActions().read(0)));
            }
        });
    }
}
