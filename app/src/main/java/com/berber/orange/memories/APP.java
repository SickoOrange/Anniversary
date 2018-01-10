package com.berber.orange.memories;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

public class APP extends MultiDexApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesHelper.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

}
