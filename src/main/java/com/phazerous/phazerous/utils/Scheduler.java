package com.phazerous.phazerous.utils;

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
