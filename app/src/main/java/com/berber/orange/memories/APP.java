package com.berber.orange.memories;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;


import com.berber.orange.memories.model.db.DaoMaster;
import com.berber.orange.memories.model.db.DaoSession;

import org.greenrobot.greendao.database.Database;


public class APP extends MultiDexApplication {


    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "anniversary-db", null);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        SharedPreferencesHelper.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
