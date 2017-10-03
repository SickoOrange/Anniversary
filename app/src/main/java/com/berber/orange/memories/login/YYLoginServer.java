package com.berber.orange.memories.login;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.berber.orange.memories.login.command.DefaultLoginInMethod;
import com.berber.orange.memories.login.command.FacebookLoginInMethod;
import com.berber.orange.memories.login.command.GoogleLoginInMethod;
import com.berber.orange.memories.login.service.BaseLoginInCallBack;
import com.berber.orange.memories.login.service.DefaultLoginInCallBack;
import com.berber.orange.memories.login.service.GoogleLoginInCallBack;
import com.berber.orange.memories.login.service.UserExistingListener;
import com.berber.orange.memories.utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by yinya
 * on 02.10.2017.
 */

public enum YYLoginServer {
    INSTANCE;


    private static final String TAG = "YYLoginServer";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DefaultLoginInMethod defaultLoginIn;
    private GoogleLoginInMethod googleLoginIn;


    public YYLoginListener yyLoginListener;
    private FacebookLoginInMethod facebookLoginIn;

    YYLoginServer() {
        Log.d(TAG, "YYLoginServer Constructor");
    }

    public void Init() {


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    //user is signed in
                    Log.d(TAG, "onAuthStateCHanged:signed in: " + currentUser.getEmail());
                    //yyLoginListener.UserSignIn(currentUser);
                } else {
                    // user is signed out
                    Log.d(TAG, "onAuthStateChanged:signed out");
                    // yyLoginListener.UserSignOut();
                }

            }
        };
    }


    public void loginWithDefault(Activity activity, String email, String password, BaseLoginInCallBack baseLoginInCallBack) {
        if (TextUtils.isEmpty(email)) {
            throw new IllegalArgumentException("email can't be null or empty");
        }

        if (!Utils.validate(email)) {
            throw new IllegalArgumentException("email format is incorrect");
        }

        if (TextUtils.isEmpty(password)) {
            throw new IllegalArgumentException("password can't be null or empty");
        }

        defaultLoginIn = new DefaultLoginInMethod(mAuth, activity, baseLoginInCallBack);
        defaultLoginIn.login(email, password, baseLoginInCallBack);
    }

    public void loginWithGoogle(Activity activity) {
        googleLoginIn = new GoogleLoginInMethod(mAuth, activity, null);
        googleLoginIn.login();
    }

    public void loginWithFacebook(Activity activity, BaseLoginInCallBack baseLoginInCallBack) {
        facebookLoginIn = new FacebookLoginInMethod(mAuth, activity, baseLoginInCallBack);
        facebookLoginIn.login();
    }


    public void addAuthStateListener() {

        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }

    }

    public void removeAuthStateListener() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void setyyLoginListener(YYLoginListener yyLoginListener) {
        if (yyLoginListener != null) {
            this.yyLoginListener = yyLoginListener;
        }
    }

    public void checkUserAlreadySigned(UserExistingListener userExistingListener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userExistingListener.isUserExisting(true, currentUser);
        } else {
            userExistingListener.isUserExisting(false, null);
        }

    }


    public void handleSignInResult(LoginType type, GoogleSignInResult googleSignInResult, GoogleLoginInCallBack callback) {
        switch (type) {
            case GOOGLE:
                googleLoginIn.handleGoogleSignResult(googleSignInResult, callback);
                break;
        }
    }


    public void handleFacebookResult(int requestCode, int resultCode, Intent data) {
        if (facebookLoginIn != null) {
            facebookLoginIn.handleFacebookResult(requestCode, resultCode, data);
        }
    }
}
