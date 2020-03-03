package com.graduationproject.invoforultimate.utils;

import java.text.DecimalFormat;

/**
 * Created by INvo
 * on 2019-12-23.
 */
public class NavigationUtils {
    private static DecimalFormat decimalFormat = new DecimalFormat("##0.0");

    public static String getFriendlyDistance(int m) {
        if (m < 1000) {
            return m + "米";
        }
        float dis = m / 1000f;
        String distance = decimalFormat.format(dis) + "公里";
        return distance;
    }
}
