package com.phazerous.phazerous.vein_gathering.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.phazerous.phazerous.vein_gathering.interfaces.IGatheringStartObserver;
import com.phazerous.phazerous.vein_gathering.manager.VeinManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class GatherStartListener {

    private final JavaPlugin plugin;
    private final VeinManager veinManager;
    private final List<IGatheringStartObserver> observers = new ArrayList<>();

    public GatherStartListener(JavaPlugin plugin, VeinManager veinManager) {
        this.plugin = plugin;
        this.veinManager = veinManager;

        enable();
    }


    private void enable() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        protocolManager.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                PacketContainer packet = event.getPacket();
                int entityId = packet
                        .getIntegers()
                        .read(0);

                if (veinManager.isVein(player, entityId)) notifyObservers(player, entityId);
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
