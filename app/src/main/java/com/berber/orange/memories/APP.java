package com.berber.orange.memories;

import android.app.Application;


import com.berber.orange.memories.model.db.DaoMaster;
import com.berber.orange.memories.model.db.DaoSession;

import org.greenrobot.greendao.database.Database;


public class APP extends Application {


    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "anniversary-db", null);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
