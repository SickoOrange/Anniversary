package com.berber.orange.memories;

import android.support.annotation.RestrictTo;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        int dayOfMonth = Calendar.DAY_OF_MONTH;
        int month = Calendar.MONTH;
        int year = Calendar.YEAR;

        Calendar instance = Calendar.getInstance();
        int i = instance.get(Calendar.DAY_OF_MONTH);
        System.out.println(i);
        assertEquals(4, 2 + 2);
    }

    @Test
    public void dateFormatter() {
        DateFormat instance = SimpleDateFormat.getDateInstance();
        String format = instance.format(new Date());
        String[] split = format.split(",");
        for (String s : split) {
            System.out.println(s.trim());
        }
    }

    @Test
    public void getNewDate() throws ParseException {
        Date date = new Date(2017 - 1900, 12, 5);
        System.out.println(date.toString());
    }

    @Test
    public void getCurrentDate() {
        String dateString = "2017-11-22" + " " + "06:10";
        Date currentDate = null;
        try {
            currentDate = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ENGLISH).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(currentDate.toString());
    }

    @Test
    public void calculateAnniversaryNotificationDate() {
        Date currentDate = new Date();
        Calendar instance = Calendar.getInstance();
        instance.setTime(currentDate);
        instance.add(Calendar.WEEK_OF_YEAR, 5 * -1);
        //long timeInMillis = currentDate.getTime() - (long) (27*24 * 3600 * 1000);
        //System.out.println(new Date(timeInMillis));
        Date time = instance.getTime();
        System.out.println(time.toString());
    }

    private double calculateHourIndex(String msg) {
        String[] strings = msg.split(" ");
        System.out.println(strings);
        int prefixIndex = Integer.valueOf(strings[0]);
        double hourIndex = 0;
        switch (strings[1]) {
            case "minute":
                hourIndex = prefixIndex / 60;
                break;
            case "hour":
                hourIndex = prefixIndex * 1;
                break;
            case "week":
                hourIndex = prefixIndex * 24 * 7;
                break;
            case "day":
                hourIndex = prefixIndex * 24;
                break;
            case "month":
                break;
        }


        return hourIndex;
    }

    @Test
    public void test() {
        double l = calculateHourIndex("20 minute before");
        System.out.println(l);
    }

    @Test
    public void calculateProzent() throws ParseException {
        // String showDateString = "Fri Dec 01 00:00:00 GMT + 01:00 2017";
        //String createdDateString = "Wed Nov 15 16:21:54 GMT + 01:00 2017";
        //String currentDateString = "Wed Nov 15 21:31:42 GMT + 01:00 2017";
        //String notificationDateString = "Thu Nov 30 00:00:00 GMT + 01:00 2017";

        // 11-15 21:41:52.732 4751-4751/com.berber.orange.memories E/TAG: onBindViewHolder0
        //11-15 21:41:52.736 4751-4751/com.berber.orange.memories E/TAG: Anniversary show date 2017-12-01 12:00:00
        //11-15 21:41:52.737 4751-4751/com.berber.orange.memories E/TAG: Anniversary created date 2017-11-15 04:21:54
        //11-15 21:41:52.737 4751-4751/com.berber.orange.memories E/TAG: Anniversary current date 2017-11-15 09:41:52
        //11-15 21:41:52.737 4751-4751/com.berber.orange.memories E/TAG: Anniversary notification date 2017-11-30 12:00:00

        String showDateString = "2017-11-15 12:00:00";
        String createdDateString = "2017-11-15 04:21:54";
        String currentDateString = "2017-11-15 09:41:52";
        String notificationDateString = "2017-11-30 12:00:00";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date showDate = sdf.parse(showDateString);
        Date createdDate = sdf.parse(createdDateString);

        Date currentDate = sdf.parse(currentDateString);
        Date notificationDate = sdf.parse(notificationDateString);

        System.out.println(showDate);
        System.out.println(createdDate);
        System.out.println(currentDate);
        System.out.println(notificationDate);

        long restInMillis = showDate.getTime() - currentDate.getTime();
        // long totalInMillis = showDate.getTime() - createdDate.getTime();
        System.out.println(restInMillis);

        long rest = restInMillis / (1000 * 60 * 60);
        System.out.println(100 - (8588000 * 100.0 / 8900987));
        System.out.println(rest);
    }

}