package com.berber.orange.memories;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.junit.Test;


/**
 * Created by orange on 2017/12/2.
 */

public class JodaTimeTest {

    @Test
    public void calculateTest() {
        //   DateTime dateTime = new DateTime(DateTimeZone.UTC);

        //   DateTime dt = new DateTime(2017, 12, 13, 0, 0, 0);// 年,月,日,时,分,秒,毫秒
        // System.out.println(dt.withZone(DateTimeZone.forID("America/New_York")));
        //    System.out.println(dt.withZone(DateTimeZone.UTC));\

        DateTime dt = new DateTime(2017, 12, 13, 0, 0, 0);// 年,月,日,时,分,秒,毫秒
        DateTime dt2 = new DateTime(2017, 12, 12, 13, 0, 0);// 年,月,日,时,分,秒,毫秒

        DateTime current = DateTime.now();

        int days = Days.daysBetween(dt2, dt).getDays();
        System.out.println(days);

    }
}
