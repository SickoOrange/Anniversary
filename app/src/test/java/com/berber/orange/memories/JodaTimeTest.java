package com.berber.orange.memories;

import com.berber.orange.memories.database.firebasemodel.AnniversaryModel;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Created by orange on 2017/12/2.
 */

public class JodaTimeTest {

    @Test
    public void calculateTest() {
        String s1 = "25-01-2018 00:00:00";
        String s2 = "10-02-2018 00:00:00";
        String s3 = "25-02-2018 00:00:00";
        String s4 = "25-03-2018 00:00:00";

        DateTime date1 = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime(s1).withZone(DateTimeZone.getDefault());
        DateTime date2 = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime(s2).withZone(DateTimeZone.getDefault());
        DateTime date3 = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime(s3).withZone(DateTimeZone.getDefault());
        DateTime date4 = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime(s4).withZone(DateTimeZone.getDefault());

        AnniversaryModel model1 = new AnniversaryModel();
        model1.setDate(s1);
        AnniversaryModel model2 = new AnniversaryModel();
        model2.setDate(s2);
        AnniversaryModel model3 = new AnniversaryModel();
        model3.setDate(s3);
        AnniversaryModel model4 = new AnniversaryModel();
        model4.setDate(s4);
        List<AnniversaryModel> list = Arrays.asList(model1, model2, model3, model4);

        Map<String, List<AnniversaryModel>> collect = list.stream().collect(Collectors.groupingBy(model -> {
            String date = model.getDate();

            DateTime dateTime = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime(date).withZone(DateTimeZone.getDefault());
            return dateTime.getMonthOfYear() + "-" + dateTime.getYear();
        }));

        LinkedHashMap<String, List<AnniversaryModel>> finalMap = new LinkedHashMap<>();
        collect.entrySet().stream().sorted(
                (o1, o2) -> {
                    String ss1 = o1.getKey();
                    String ss2 = o2.getKey();
                    String[] split1 = ss1.split("-");
                    String[] split2 = ss2.split("-");

                    if (split1[1].equals(split2[1])) {
                        return split1[0].compareTo(split2[0]);
                    } else {
                        return split1[1].compareTo(split2[1]);
                    }

                }
        ).forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));
        //System.out.println(sorted);

        System.out.println(finalMap);

    }

    @Test
    public void calculateTest1() {

        String s1 = "25-01-2019 00:00:00";
        String s2 = "10-02-2019 00:00:00";
        String s3 = "25-02-2019 00:00:00";
        String s4 = "25-03-2018 00:00:00";

        DateTime date1 = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime(s1).withZone(DateTimeZone.getDefault());
        DateTime date2 = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime(s2).withZone(DateTimeZone.getDefault());
        DateTime date3 = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime(s3).withZone(DateTimeZone.getDefault());
        DateTime date4 = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime(s4).withZone(DateTimeZone.getDefault());

        AnniversaryModel model1 = new AnniversaryModel();
        model1.setDate(s1);
        AnniversaryModel model2 = new AnniversaryModel();
        model2.setDate(s2);
        AnniversaryModel model3 = new AnniversaryModel();
        model3.setDate(s3);
        AnniversaryModel model4 = new AnniversaryModel();
        model4.setDate(s4);

        Map<String, List<AnniversaryModel>> sortedMap = new LinkedHashMap<>();
        List<AnniversaryModel> list = Arrays.asList(model1, model2, model3, model4);
        com.annimon.stream.Stream<AnniversaryModel> stream = com.annimon.stream.Stream.of(list);
        stream
                .sortBy(model -> DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime(model.getDate()).withZone(DateTimeZone.getDefault()))
                .chunkBy(model -> {
                    String date = model.getDate();
                    DateTime dateTime = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime(date).withZone(DateTimeZone.getDefault());
                    return dateTime.getMonthOfYear() + "-" + dateTime.getYear();
                })
                .forEach(subList -> {
                    AnniversaryModel model = subList.get(0);
                    DateTime dateTime = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime(model.getDate()).withZone(DateTimeZone.getDefault());
                    sortedMap.put(dateTime.getMonthOfYear() + "-" + dateTime.getYear(), subList);
                });


        System.out.println(sortedMap);

        Map<String, List<AnniversaryModel>> collect = list.stream()
                //  .sorted(model -> DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime(model.getDate()).withZone(DateTimeZone.getDefault()))
                .collect(Collectors.groupingBy(model -> {
                    String date = model.getDate();

                    DateTime dateTime = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withZoneUTC().parseDateTime(date).withZone(DateTimeZone.getDefault());
                    return dateTime.getMonthOfYear() + "-" + dateTime.getYear();
                }));

        LinkedHashMap<String, List<AnniversaryModel>> finalMap = new LinkedHashMap<>();
        collect.entrySet().stream().sorted(
                (o1, o2) -> {
                    String ss1 = o1.getKey();
                    String ss2 = o2.getKey();
                    String[] split1 = ss1.split("-");
                    String[] split2 = ss2.split("-");

                    if (split1[1].equals(split2[1])) {
                        return split1[0].compareTo(split2[0]);
                    } else {
                        return split1[1].compareTo(split2[1]);
                    }

                }
        ).forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));
        //System.out.println(sorted);

        System.out.println(finalMap);

    }
}
