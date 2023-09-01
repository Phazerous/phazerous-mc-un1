package com.phazerous.phazerous.entities.listeners;

import com.phazerous.phazerous.db.DocumentParser;
import com.phazerous.phazerous.entities.EntityManager;
import com.phazerous.phazerous.entities.EntityTerminateManager;
import com.phazerous.phazerous.entities.EntityType;
import com.phazerous.phazerous.entities.EntityUtils;
import com.phazerous.phazerous.entities.models.runtime.RuntimeBaseEntity;
import com.phazerous.phazerous.entities.models.runtime.RuntimeMobEntity;
import org.bson.Document;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class PlayerAttackEntityListener implements Listener {

    private final EntityManager entityManager;
    private final EntityTerminateManager entityTerminateManager;

    public PlayerAttackEntityListener(EntityManager entityManager, EntityTerminateManager entityTerminateManager) {
        this.entityManager = entityManager;
        this.entityTerminateManager = entityTerminateManager;
    }

    @EventHandler
    public void onPlayerAttackEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();

        if (!(damager instanceof Player)) return;

        Player player = (Player) damager;

        Entity entity = event.getEntity();
        UUID entityUUID = entity.getUniqueId();

        Document runtimeEntityDoc = entityManager.getRuntimeEntityDocByUUID(entityUUID);

        if (runtimeEntityDoc == null) return;

        EntityType entityType = EntityUtils.getRuntimeEntityType(runtimeEntityDoc);
        Class<? extends RuntimeBaseEntity> runtimeEntityClass = entityType.getRuntimeEntityClass();
        RuntimeBaseEntity runtimeBaseEntity = DocumentParser.parseDocument(runtimeEntityDoc, runtimeEntityClass);

        if (entityType == EntityType.MOB_ENTITY) {
            handleMobEntity(entity, (RuntimeMobEntity) runtimeBaseEntity, player);
        }
    }

    private void handleMobEntity(Entity entity, RuntimeMobEntity mobRuntimeEntity, Player player) {
        Long health = mobRuntimeEntity.getHealth();
        Long maxHealth = mobRuntimeEntity.getMaxHealth();

        health -= 2;
        EntityUtils.setMobEntityHPTitle(entity, mobRuntimeEntity.getTitle(), health, maxHealth);
        ((LivingEntity) entity).damage(0);

        entityManager.updateRuntimeEntityHealth(mobRuntimeEntity.get_id(), health);

        if (health <= 0) entityTerminateManager.terminateEntity(entity, mobRuntimeEntity, player);
    }
}
