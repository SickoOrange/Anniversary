package com.berber.orange.memories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.berber.orange.memories.model.db.DaoMaster;

/**
 * Created by orange on 2017/11/18.
 */

public class UpgradeDatabaseHelper extends DaoMaster.OpenHelper {
    public UpgradeDatabaseHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        switch (newVersion) {
//            case 2:
//                new MigrateV1ToV2().applyMigration(db, oldVersion);
//                break;
//            case 3:
//                new MigrateV2ToV3().applyMigration(db, oldVersion);
//                break;
//            default:
//                return;
//        }
        Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by migrating all tables data");
        //db.execSQL("ALTER TABLE "+MyDao.TABLENAME+" ADD COLUMN 'NEW_COL_1' INTEGER;");
        //onCreate(db);
    }
}
