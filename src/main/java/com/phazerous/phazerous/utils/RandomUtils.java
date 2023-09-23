package com.phazerous.phazerous.utils;

import java.util.List;
import java.util.Random;

public class RandomUtils {
    private static final Random random = new Random();

    public static <T> T getRandomElement(List<T> array) {
        return array.get(random.nextInt(array.size()));
    }
}
