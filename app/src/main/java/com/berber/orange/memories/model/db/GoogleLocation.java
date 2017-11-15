package com.berber.orange.memories.model.db;

import org.greenrobot.greendao.annotation.Id;

/**
 * Created by orange on 2017/11/15.
 */

public class GoogleLocation {
    @Id(autoincrement = true)
    private Long id;

    private String locationName;

    private String locationAdress;

    private String locationPhoneNumber;
}
