package com.phazerous.phazerous.exceptions;

public class PlayerNotFoundException extends Exception {
    public PlayerNotFoundException() {
        super("Player not found.");
    }
}
