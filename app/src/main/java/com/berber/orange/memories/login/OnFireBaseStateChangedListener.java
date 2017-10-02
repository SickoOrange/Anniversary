package com.berber.orange.memories.login;

import com.berber.orange.memories.login.user.MyFireBaseUser;

/**
 * Created by yinya
 * on 02.10.2017.
 */

interface OnFireBaseStateChangedListener {
    void UserSignIn(MyFireBaseUser user);

    void UserSignOut();

}
