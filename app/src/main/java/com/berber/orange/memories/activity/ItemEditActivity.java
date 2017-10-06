package com.berber.orange.memories.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.berber.orange.memories.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.lang.reflect.Field;

public class ItemEditActivity extends AppCompatActivity {
    private static final String TAG = "ItemEditActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(" ");

        MaterialCalendarView calendarView = findViewById(R.id.calendarView);
//        try
//        {
//
//            Class<?> cvClass = calendarView.getClass();
//            Field field = cvClass.getDeclaredField("mMonthName");
//            field.setAccessible(true);
//
//            try
//            {
//                TextView tv = (TextView) field.get(calendarView);
//                tv.setTextColor(Color.RED);
//            }
//            catch (IllegalArgumentException e)
//            {
//                e.printStackTrace();
//            }
//            catch (IllegalAccessException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        catch (NoSuchFieldException e)
//        {
//            e.printStackTrace();
//        }

    }
}
