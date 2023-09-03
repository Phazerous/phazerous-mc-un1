package com.phazerous.phazerous.utils;

import java.text.DecimalFormat;

public class Formatter {
    public static String formatFunds(Long funds) {
        DecimalFormat df = new DecimalFormat("###.##");
        String[] suffix = new String[]{"", "K", "M", "B", "T", "Q"};

        int suffixIndex = 0;
        double left = funds;

        for (; left >= 1000; left /= 1000.0, suffixIndex++) {
        }


        String formattedString = df.format(left);

        if (formattedString.length() >= 4) formattedString = formattedString.substring(0, 4);
        if (formattedString.endsWith(".")) formattedString = formattedString.substring(0, 3);


        return formattedString + suffix[suffixIndex];
    }
}
