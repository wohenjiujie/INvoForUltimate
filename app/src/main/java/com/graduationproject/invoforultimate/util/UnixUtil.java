package com.graduationproject.invoforultimate.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by INvo
 * on 2019-11-03.
 */
public class UnixUtil {
    long time = System.currentTimeMillis();
    String nowTimeStamp = String.valueOf(time);
    String formats = "yyyy-MM-dd HH:mm:ss";
    Long timeStamp = Long.parseLong(nowTimeStamp) ;
    String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timeStamp));
    public String getDate() {
        return date;
    }
}
