package com.phazerous.phazerous.utils;

import java.util.List;

public class ArrayUtils {

    public static int[] getIntArrayFromList(List<Integer> list) {
        int[] array = new int[list.size()];

        for (int i = 0; i < list.size(); i++) array[i] = list.get(i);

        return array;
    }
}
