package com.phazerous.phazerous;

import com.phazerous.phazerous.dtos.EntityDto;
import com.phazerous.phazerous.dtos.LocationedEntityDto;
import com.phazerous.phazerous.dtos.RuntimeEntityDto;
import org.bson.types.ObjectId;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GatheringUtils {
    private final String PREFIX = "[";
    private final String PREFIX_COLOR = "§6";

    private final String POSTFIX = "]";
    private final String POSTFIX_COLOR = "§6";

    private final String TITLE = "|||Добыча|||";

    private final String COLOR_IF_LESS_33 = "§c";
    private final String COLOR_IF_LESS_66 = "§e";
    private final String COLOR_IF_MORE_66 = "§a";

    private final Scheduler scheduler;
    private final EntityManager entityManager;
    private final ItemManager itemManager;

    public GatheringUtils(Scheduler scheduler, EntityManager entityManager, ItemManager itemManager) {
        this.scheduler = scheduler;
        this.entityManager = entityManager;
        this.itemManager = itemManager;
    }

    public void gather(LocationedEntityDto locationedEntityDto, EntityDto entityDto, Player player, Entity entity, RuntimeEntityDto runtimeEntityDto) {
        long hardness = entityDto.getHardness();
        List<ObjectId> dropIds = entityDto.getDropsIds();

        AtomicInteger secondsCounter = new AtomicInteger();

        int gatherIntervalId = scheduler.runInterval(() -> {
            // TO EXTRACT
            player.playSound(player.getLocation(), Sound.ENDERMAN_HIT, 5, 5);

            int seconds = secondsCounter.incrementAndGet();
            float percentage = (float) (seconds * 20) / hardness;

            String entityTitle = prepareTitle(percentage);
            entity.setCustomName(entityTitle);
        });

        int finishGatherId = scheduler.runTaskLater(() -> {
            player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 5, 5);
            scheduler.cancelTask(gatherIntervalId);
            entityManager.removeEntity(entity, runtimeEntityDto);

            scheduler.scheduleEntityRespawn(locationedEntityDto, entityDto.getRespawnTime());

            if (dropIds == null) return;

            List<ItemStack> items = itemManager.getItemsByIds(dropIds);

            player.getInventory().addItem(items.toArray(new ItemStack[0]));
        }, hardness);
    }
    
    public String prepareTitle(float percentage) {
        float avgSymbols = TITLE.length() * percentage;
        int symbols = Math.round(avgSymbols);

        String titleColor = "";

        if (percentage < 0.33) titleColor = COLOR_IF_LESS_33;
        else if (percentage < 0.66) titleColor = COLOR_IF_LESS_66;
        else titleColor = COLOR_IF_MORE_66;

        String preparedTitle = new StringBuilder(TITLE)
                .insert(symbols, "§r")
                .toString();

        String label = PREFIX_COLOR + PREFIX + titleColor + preparedTitle + POSTFIX_COLOR + POSTFIX;

        return ChatColor.translateAlternateColorCodes('§', label);
    }

}
