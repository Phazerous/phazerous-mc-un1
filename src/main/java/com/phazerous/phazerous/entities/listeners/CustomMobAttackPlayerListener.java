package com.phazerous.phazerous.entities.listeners;

import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.entities.EntityManager;
import com.phazerous.phazerous.entities.enums.EntityType;
import com.phazerous.phazerous.entities.runtime.EntityRuntimeManager;
import com.phazerous.phazerous.entities.runtime.models.RuntimeBaseEntity;
import com.phazerous.phazerous.entities.runtime.models.RuntimeMobEntity;
import com.phazerous.phazerous.entities.utils.EntityUtils;
import org.bson.Document;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CustomMobAttackPlayerListener implements Listener {

    private final EntityManager entityManager;
    private final EntityRuntimeManager entityRuntimeManager;

    public CustomMobAttackPlayerListener(EntityManager entityManager, EntityRuntimeManager entityRuntimeManager) {
        this.entityManager = entityManager;
        this.entityRuntimeManager = entityRuntimeManager;
    }

    @EventHandler
    public void onCustomMobAttackPlayer(EntityDamageByEntityEvent event) {
        Entity damagedEntity = event.getEntity();

        if (!(damagedEntity instanceof Player)) return;

        Entity damager = event.getDamager();

        Document runtimeEntityDoc = entityRuntimeManager.getRuntimeEntityDoc(damager.getUniqueId());
        if (runtimeEntityDoc == null) return;

        event.setDamage(0);

        EntityType entityType = EntityUtils.getEntityType(runtimeEntityDoc);
        if (!(entityType == EntityType.MOB_ENTITY || entityType == EntityType.BOSS_ENTITY)) return;

        Player player = (Player) damagedEntity;

        Class<? extends RuntimeBaseEntity> runtimeEntityClass = entityType.getRuntimeEntityClass();
        RuntimeBaseEntity runtimeBaseEntity = DocumentParser.parseDocument(runtimeEntityDoc, runtimeEntityClass);

        handleAttack((RuntimeMobEntity) runtimeBaseEntity, player);

    }

    private void handleAttack(RuntimeMobEntity mobRuntimeEntity, Player player) {
        Long attack = mobRuntimeEntity.getAttack();
        Double playerHealth = player.getHealth();
        player.setHealth(playerHealth - attack);
    }
}
