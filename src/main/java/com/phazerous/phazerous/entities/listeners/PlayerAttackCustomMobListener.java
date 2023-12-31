package com.phazerous.phazerous.entities.listeners;

import com.phazerous.phazerous.db.utils.DocumentParser;
import com.phazerous.phazerous.entities.EntityTerminateManager;
import com.phazerous.phazerous.entities.enums.EntityType;
import com.phazerous.phazerous.entities.interfaces.IEntityDamageSubscriber;
import com.phazerous.phazerous.entities.utils.EntityUtils;
import com.phazerous.phazerous.entities.runtime.models.RuntimeBaseEntity;
import com.phazerous.phazerous.entities.runtime.models.RuntimeMobEntity;
import com.phazerous.phazerous.entities.runtime.EntityRuntimeManager;
import com.phazerous.phazerous.items.utils.ItemUtils;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerAttackCustomMobListener implements Listener {

    private final EntityRuntimeManager entityRuntimeManager;
    private final EntityTerminateManager entityTerminateManager;
    private final List<IEntityDamageSubscriber> subscribers = new ArrayList<>();

    public PlayerAttackCustomMobListener(EntityTerminateManager entityTerminateManager, EntityRuntimeManager entityRuntimeManager) {
        this.entityTerminateManager = entityTerminateManager;
        this.entityRuntimeManager = entityRuntimeManager;
    }

    @EventHandler
    public void onPlayerAttackCustomMob(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();

        if (!(damager instanceof Player)) return;

        Entity entity = event.getEntity();
        UUID entityUUID = entity.getUniqueId();


        Document runtimeEntityDoc = entityRuntimeManager.getRuntimeEntityDoc(entityUUID);
        if (runtimeEntityDoc == null) return;

        event.setDamage(0);

        EntityType entityType = EntityUtils.getEntityType(runtimeEntityDoc);
        if (!(entityType == EntityType.MOB_ENTITY || entityType == EntityType.BOSS_ENTITY))
            return;

        Class<? extends RuntimeBaseEntity> runtimeEntityClass = entityType.getRuntimeEntityClass();
        RuntimeBaseEntity runtimeBaseEntity = DocumentParser.parse(runtimeEntityDoc, runtimeEntityClass);

        Player player = (Player) damager;

        handleAttack(entity, (RuntimeMobEntity) runtimeBaseEntity, player);
    }

    private void handleAttack(Entity entity, RuntimeMobEntity mobRuntimeEntity, Player player) {
        Long health = mobRuntimeEntity.getHealth();
        Long maxHealth = mobRuntimeEntity.getMaxHealth();
        Long damage = getDamage(player);

        health -= damage;

        if (health <= 0)
            entityTerminateManager.terminateEntity(entity, mobRuntimeEntity, player);
        else {
            entityRuntimeManager.handleHealthChange(mobRuntimeEntity.get_id(), health);
            EntityUtils.setMobEntityHPTitle(entity, mobRuntimeEntity.getTitle(), health, maxHealth);
        }

        handleDamage(entity.getUniqueId(), 2D, health);
    }

    private Long getDamage(Player player) {
        final long BASE_DAMAGE = 1L;

        ItemStack itemInHand = player.getInventory().getItemInHand();

        if (itemInHand == null || itemInHand.getType() == Material.AIR)
            return BASE_DAMAGE;

        if (!ItemUtils.hasItemDamage(itemInHand)) return BASE_DAMAGE;

        return ItemUtils.getItemDamage(itemInHand);
    }

    public void subscribe(IEntityDamageSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    private void handleDamage(UUID entityUUID, Double damage, Long health) {
        for (IEntityDamageSubscriber subscriber : subscribers) {
            subscriber.handleDamage(entityUUID, damage, health);
        }
    }
}
