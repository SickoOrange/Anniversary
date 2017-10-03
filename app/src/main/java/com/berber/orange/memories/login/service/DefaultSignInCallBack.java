package com.berber.orange.memories.login.service;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by yinya
 * on 03.10.2017.
 */

public interface DefaultSignInCallBack extends BaseSignInCallBack{
    void loginSucceeds(FirebaseUser currentUser);

    void loginFailure(Task<AuthResult> task);
}
