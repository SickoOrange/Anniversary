package com.berber.orange.memories.loginservice.service;

import com.berber.orange.memories.loginservice.user.MyFireBaseUser;

/**
 * Created by yinya
 * on 02.10.2017.
 */

interface OnFireBaseStateChangedListener {
    void UserSignIn(MyFireBaseUser user);

    void UserSignOut();

}
