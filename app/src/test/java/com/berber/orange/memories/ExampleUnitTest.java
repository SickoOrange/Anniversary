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

}