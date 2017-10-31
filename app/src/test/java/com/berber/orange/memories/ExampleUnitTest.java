package com.berber.orange.memories;

import org.junit.Test;

import java.util.Calendar;

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
}