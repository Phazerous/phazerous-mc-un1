package com.phazerous.phazerous.entities.interfaces;

import java.util.UUID;

public interface IEntityDamageSubscriber {
    void handleDamage(UUID entityUUID, Double damage, Long health);
}
