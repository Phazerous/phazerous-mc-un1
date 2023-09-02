package com.phazerous.phazerous.commands;

import lombok.Getter;

@Getter
public abstract class AbstractCommand implements ICommand {
    protected String name;
}
