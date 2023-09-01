package com.phazerous.phazerous.economy.interfaces;

import java.util.UUID;

public interface IEconomySubscriber {
    void update(UUID playerUUID, double balance);
}
