package com.phazerous.phazerous.utils;

import com.phazerous.phazerous.entities.EntityManager;
import com.phazerous.phazerous.entities.models.entities.LocationedEntity;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Scheduler {
    private final BukkitScheduler bukkitScheduler;
    private final JavaPlugin plugin;
    private final long TICKS_IN_SECOND = 20L;

    @Getter
    private static Scheduler instance;

    private Scheduler(BukkitScheduler bukkitScheduler, JavaPlugin plugin) {
        this.bukkitScheduler = bukkitScheduler;
        this.plugin = plugin;
    }

    public static Scheduler init(BukkitScheduler bukkitScheduler, JavaPlugin plugin) {
        if (instance == null) {
            instance = new Scheduler(bukkitScheduler, plugin);
        }

        return instance;
    }

    public int runInterval(Runnable functionToRun) {
        return bukkitScheduler.runTaskTimer(plugin, functionToRun, TICKS_IN_SECOND, TICKS_IN_SECOND)
                .getTaskId(); // RUN AFTER 1 SECOND, THEN EVERY 1 SECOND
    }

    public int runInterval(Runnable functionToRun, long interval) {
        return bukkitScheduler.runTaskTimer(plugin, functionToRun, interval, interval).getTaskId();
    }

    public void runTaskLater(Runnable functionToRun, long timeLater) {
        bukkitScheduler.runTaskLater(plugin, functionToRun, timeLater).getTaskId();
    }

    public void cancelTask(int taskId) {
        bukkitScheduler.cancelTask(taskId);
    }
}
