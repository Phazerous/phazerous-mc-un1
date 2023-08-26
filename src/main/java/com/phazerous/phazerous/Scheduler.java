package com.phazerous.phazerous;

import com.phazerous.phazerous.dtos.LocationedEntityDto;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Scheduler {
    private final BukkitScheduler bukkitScheduler;
    private final JavaPlugin plugin;
    private final long TICKS_IN_SECOND = 20L;

    public Scheduler(BukkitScheduler bukkitScheduler, JavaPlugin plugin) {
        this.bukkitScheduler = bukkitScheduler;
        this.plugin = plugin;
    }

    public int runInterval(Runnable functionToRun) {
        return bukkitScheduler.runTaskTimer(plugin, functionToRun, 20L, TICKS_IN_SECOND).getTaskId();
    }

    public int runInterval(Runnable functionToRun, long interval) {
        return bukkitScheduler.runTaskTimer(plugin, functionToRun, interval, interval).getTaskId();
    }

    public int runTaskLater(Runnable functionToRun, long timeLater) {
        return bukkitScheduler.runTaskLater(plugin, functionToRun, timeLater).getTaskId();
    }

    public void scheduleEntityRespawn(LocationedEntityDto locationedEntityDto, long respawnTime) {
        runTaskLater(() -> EntityManager.getInstance().respawnEntity(locationedEntityDto), respawnTime);
    }

    public void cancelTask(int taskId) {
        bukkitScheduler.cancelTask(taskId);
    }
}
