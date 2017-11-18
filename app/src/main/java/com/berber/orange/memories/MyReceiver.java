package com.berber.orange.memories;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.berber.orange.memories.activity.additem.AddItemActivity;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("TAG", "recive broadcast " + intent.getAction());
        if (action == AddItemActivity.INTENT_ALARM_LOG) {
            Log.e("AlarmReceiver", "log log log");
        }

    }
}
