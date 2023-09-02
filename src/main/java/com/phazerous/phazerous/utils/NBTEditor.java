package com.phazerous.phazerous.utils;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class NBTEditor {
    public static ItemStack setString(ItemStack item, String key, String value) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        compound.setString(key, value);
        nmsItem.setTag(compound);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public static boolean hasString(ItemStack item, String key) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        return compound.hasKey(key);

    }

    public static String getString(ItemStack item, String key) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        return compound.getString(key);
    }

    public static void setEntityPersistenceRequired(Entity entity) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        NBTTagCompound tag = new NBTTagCompound();
        nmsEntity.c(tag);

        tag.setByte("PersistenceRequired", (byte) 1);

        nmsEntity.f(tag);
    }

    public static void setEntitySpeed(LivingEntity entity, double speed) {
        EntityLiving nmsEntity = (EntityLiving) ((CraftEntity) entity).getHandle();
        AttributeInstance attributeInstance = nmsEntity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        AttributeModifier speedModifier = new AttributeModifier("Speed Modifier", speed, 1);
        attributeInstance.b(speedModifier);
    }

    public static void setAI(Entity entity, boolean enabled) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        NBTTagCompound tag = new NBTTagCompound();
        nmsEntity.c(tag);

        tag.setByte("NoAI", (byte) (enabled ? 0 : 1));

        nmsEntity.f(tag);
    }

    public static void setNoGravity(Entity entity, boolean enabled) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        NBTTagCompound tag = new NBTTagCompound();
        nmsEntity.c(tag);

        tag.setByte("NoGravity", (byte) (enabled ? 1 : 0));

        nmsEntity.f(tag);
    }

    public static void setInvulnerable(Entity entity, boolean enabled) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        NBTTagCompound tag = new NBTTagCompound();
        nmsEntity.c(tag);

        tag.setByte("Invulnerable", (byte) (enabled ? 1 : 0));

        nmsEntity.f(tag);
    }
}
