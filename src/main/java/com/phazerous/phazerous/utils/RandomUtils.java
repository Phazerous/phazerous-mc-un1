package com.phazerous.phazerous.utils;

import com.phazerous.phazerous.items.models.DynamicAmount;

import java.util.List;
import java.util.Random;

public class RandomUtils {
    private static final Random random = new Random();

    public static int getAmount(DynamicAmount amount) {
        int min = amount.getMin();
        int max = amount.getMax();

        int total = min;

        for (int i = 0; i < max - min; i++) {
            double randomValue = random.nextDouble();

            if (randomValue < 0.5) {
                total += 1;
            }
        }

        return total;
    }

    public static <T> T getRandomElement(List<T> array) {
        return array.get(random.nextInt(array.size()));
    }

    public static int getRandomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
