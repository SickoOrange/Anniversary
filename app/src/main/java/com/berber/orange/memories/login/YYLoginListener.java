package com.berber.orange.memories.login;

import com.berber.orange.memories.login.user.MyFireBaseUser;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by yinya
 * on 02.10.2017.
 */

public interface YYLoginListener {
    void UserSignIn(FirebaseUser user);

    void UserSignOut();

    void onLoginSuccess(FirebaseUser currentUser);

    void onLoginFailure();

    void userAlreadySigned(FirebaseUser currentUser);

    void userNotSigned();
}
