package com.phazerous.phazerous.gathering.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.phazerous.phazerous.gathering.GatheringSpawnManager;
import com.phazerous.phazerous.gathering.interfaces.IGatheringStartObserver;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class GatherStartListener {

    private final JavaPlugin plugin;
    private final GatheringSpawnManager spawnManager;
    private final List<IGatheringStartObserver> observers = new ArrayList<>();

    public GatherStartListener(JavaPlugin plugin, GatheringSpawnManager spawnManager) {
        this.plugin = plugin;
        this.spawnManager = spawnManager;
    }


    public void enable() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        protocolManager.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();

                PacketContainer packet = event.getPacket();

                int entityId = packet.getIntegers().read(0);

                if (spawnManager.isVeinEntity(player, entityId))
                    notifyObservers(player, entityId);
            }
        });
    }

    private void notifyObservers(Player player, int entityId) {
        for (IGatheringStartObserver observer : observers) {
            observer.onGatheringStart(player, entityId);
        }
    }

    public void addObserver(IGatheringStartObserver observer) {
        observers.add(observer);
    }
}
