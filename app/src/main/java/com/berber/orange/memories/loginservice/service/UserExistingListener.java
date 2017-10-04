package com.berber.orange.memories.loginservice.service;


import com.google.firebase.auth.FirebaseUser;

/**
 * Created by yinya
 * on 03.10.2017.
 */

public interface UserExistingListener extends BaseLoginInCallBack {

    void isUserExisting(boolean b, FirebaseUser currentUser);

}
