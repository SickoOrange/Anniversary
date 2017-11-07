package com.berber.orange.memories;

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
    public void calculateLeftDays() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 11, 5, 0, 0, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d2 = sdf.parse("2017-11-10 00:00:00");
        long time = calendar.getTime().getTime();
        System.out.println((time - System.currentTimeMillis()) / (1000 * 3600 * 24));

        //long destination = calendar.getTime().getTime();
        //long current = System.currentTimeMillis();

//        if (current < destination) {
//            long timeStamp = destination - current;
//            long l1 = timeStamp / (1000 * 3600 * 24);
//            System.out.println(l1);
//        }
    }

    @Test
    public void getCurrentDate() {
        String dateString = "2017-5-7" + " " + "24:00";
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
        long timeInMillis = currentDate.getTime() - (long) (24 * 3600 * 1000);
        System.out.println(new Date(timeInMillis));
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
    public void test(){
        double l = calculateHourIndex("20 minute before");
        System.out.println(l);
    }

}