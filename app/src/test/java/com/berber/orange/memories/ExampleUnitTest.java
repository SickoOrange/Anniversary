package com.berber.orange.memories;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    public void calculateLeftDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 11, 3);
        long destination = calendar.getTimeInMillis();

        long current = System.currentTimeMillis();

        if (current < destination) {
            long l1 = (destination - current )/ (1000 * 3600 * 24);
            System.out.println(l1);
        }


    }

}