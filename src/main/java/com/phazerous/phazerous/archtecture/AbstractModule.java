package com.phazerous.phazerous.archtecture;

import com.phazerous.phazerous.commands.AbstractCommand;
import lombok.Getter;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class AbstractModule {
    protected final List<AbstractCommand> commands = new ArrayList<>();
    protected final List<Listener> listeners = new ArrayList<>();

    protected void addCommand(AbstractCommand command) {
        commands.add(command);
    }

    protected void addListener(Listener listener) {
        listeners.add(listener);
    }
}
