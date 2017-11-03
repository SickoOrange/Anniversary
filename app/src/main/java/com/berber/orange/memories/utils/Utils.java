package com.berber.orange.memories.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.net.ContentHandler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yinya
 * on 01.10.2017.
 */

public class Utils {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }


    public static void main(String[] args) {
        System.out.println(validate("heylbl@gmail.com"));
    }

    public static String validateEmail(String im) {
        return im;
    }

    public static void showToast(Context context, String msg, Integer duratoin) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
