package com.berber.orange.memories.activity;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

/**
 * base utils
 * Created by orange on 2018/1/6.
 */

public class BaseUtils {


    public static DateTime parseUTCDateString(String date) {
        return DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime(date).withZone(DateTimeZone.getDefault());
    }

    public static String formattDate(DateTime dateTime) {
        return dateTime.toString(DateTimeFormat.longDate());
    }

}
