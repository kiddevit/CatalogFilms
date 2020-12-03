package com.example.catalogfilms.utils;

import android.graphics.Color;

public class ColorUtils {
    public static int mapColorByRating(double rating) {
        if (rating > 8) {
            return Color.parseColor("#BF8230");
        }

        if (rating > 7) {
            return Color.parseColor("#FFC600");
        }

        return Color.parseColor("#FF1300");
    }
}
