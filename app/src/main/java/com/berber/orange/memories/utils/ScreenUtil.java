package com.berber.orange.memories.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.berber.orange.memories.R;

import java.lang.reflect.Field;

/**
 * Author: Mr.xiao on 2017/5/23
 *
 * @mail:xhb_199409@163.com
 * @github:https://github.com/xiaohaibin
 * @describe:
 */

public class ScreenUtil {

    static double scale;
    static int screenWidth = 0, screenHeight = 0;

    public static void init(Context context) {
        scale = context.getResources().getDisplayMetrics().density;
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int dip2px(float dipValue) {
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    public static boolean immerseStatusBar(Activity activity) {
        boolean success = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            success = true;
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.mystatusbar));
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            success = true;
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        return success;
    }

}
