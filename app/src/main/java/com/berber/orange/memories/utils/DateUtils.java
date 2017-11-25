package com.berber.orange.memories.utils;

import com.berber.orange.memories.model.db.Anniversary;

import java.util.Date;

/**
 * ya yin
 * Created by orange on 2017/11/25.
 */

public class DateUtils {

    public static long calculateTotalRestMillis(Anniversary anniversary) {
        Date anniversaryDate = anniversary.getDate();
        Date createDate = anniversary.getCreateDate();
        return anniversaryDate.getTime() - createDate.getTime();
    }
}
