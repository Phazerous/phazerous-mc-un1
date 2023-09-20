package com.phazerous.phazerous.resources;

import com.phazerous.phazerous.archtecture.AbstractModule;
import com.phazerous.phazerous.resources.listeners.GatherStartListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class ResourceModule extends AbstractModule {
    private final ResourceManager resourceManager;

    public ResourceModule(JavaPlugin plugin) {
        this.resourceManager = new ResourceManager();

        GatherStartListener gatherStartListener = new GatherStartListener(plugin, resourceManager);
        gatherStartListener.enable();

    }
}
