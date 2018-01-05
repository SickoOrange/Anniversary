package com.berber.orange.memories.database.databaseinterface;

import com.berber.orange.memories.database.firebasemodel.AnniversaryModel;

import java.util.List;

/**
 * Created by orange on 2018/1/5.
 */

public interface QueryResultListener {
    void queryResult(List<AnniversaryModel> list);
}
