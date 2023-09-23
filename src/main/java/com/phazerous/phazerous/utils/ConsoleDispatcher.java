package com.phazerous.phazerous.utils;

public class ConsoleDispatcher {

    public static void error(String message) {
        System.out.println(ConsoleColors.RED + message + ConsoleColors.RESET);
    }
}
