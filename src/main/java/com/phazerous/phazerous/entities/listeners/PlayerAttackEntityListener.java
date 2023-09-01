package com.phazerous.phazerous.entities.listeners;

import com.phazerous.phazerous.db.DocumentParser;
import com.phazerous.phazerous.entities.EntityManager;
import com.phazerous.phazerous.entities.EntityType;
import com.phazerous.phazerous.entities.EntityUtils;
import com.phazerous.phazerous.entities.models.BaseEntity;
import com.phazerous.phazerous.entities.models.BaseRuntimeEntity;
import com.phazerous.phazerous.entities.models.MobEntity;
import com.phazerous.phazerous.entities.models.MobRuntimeEntity;
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

    public PlayerAttackEntityListener(EntityManager entityManager) {
        this.entityManager = entityManager;
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
        Class<? extends BaseRuntimeEntity> runtimeEntityClass = entityType.getRuntimeEntityClass();
        BaseRuntimeEntity baseRuntimeEntity = DocumentParser.parseDocument(runtimeEntityDoc, runtimeEntityClass);

        if (entityType == EntityType.MOB_ENTITY) {
            MobRuntimeEntity mobRuntimeEntity = (MobRuntimeEntity) baseRuntimeEntity;
            Long health = mobRuntimeEntity.getHealth();
            Long maxHealth = mobRuntimeEntity.getMaxHealth();

            health -= 2;
            EntityUtils.setEntityHPTitle(entity, mobRuntimeEntity.getTitle(), health, maxHealth);
            ((LivingEntity) entity).damage(0);

            entityManager.updateRuntimeEntityHealth(mobRuntimeEntity.get_id(), health);
        }
    }
}
