package com.berber.orange.memories.login.service;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by yinya
 * on 02.10.2017.
 */

public interface GoogleSignInCallBack extends BaseSignInCallBack{

    void onGoogleSignInSuccess(GoogleSignInAccount acct, FirebaseUser firebaseUser);

    void onGoogleSignInFailure(Status exception);

    void onGoogleWithFireBaseAuthFailure(Exception exception);
}
