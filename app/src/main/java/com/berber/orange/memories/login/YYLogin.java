package com.berber.orange.memories.login;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.berber.orange.memories.login.command.BaseSignInMethod;
import com.berber.orange.memories.login.command.DefaultSignInMethod;
import com.berber.orange.memories.login.command.GoogleSignInMethod;
import com.berber.orange.memories.login.service.GoogleSignInCallBack;
import com.berber.orange.memories.utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by yinya
 * on 02.10.2017.
 */

public enum YYLogin {
    INSTANCE;


    private static final String TAG = "YYLogin";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DefaultSignInMethod defaultSignIn;
    private GoogleSignInMethod googleSignIn;


    public YYLoginListener yyLoginListener;

    YYLogin() {
        Log.d(TAG, "YYLogin Constructor");
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
                    yyLoginListener.UserSignIn(currentUser);
                } else {
                    // user is signed out
                    Log.d(TAG, "onAuthStateChanged:signed out");
                    yyLoginListener.UserSignOut();
                }

            }
        };
    }


    public void signIn(LoginType type, @Nullable android.app.Activity activity, String email, String password) {


        switch (type) {
            case DEFAULT:
                if (TextUtils.isEmpty(email)) {
                    throw new IllegalArgumentException("email can't be null or empty");
                }

                if (!Utils.validate(email)) {
                    throw new IllegalArgumentException("email format is incorrect");
                }

                if (TextUtils.isEmpty(password)) {
                    throw new IllegalArgumentException("password can't be null or empty");
                }

                defaultSignIn = new DefaultSignInMethod(mAuth, activity, yyLoginListener);
                defaultSignIn.login(email, password);
                break;
            case GOOGLE:
                googleSignIn = new GoogleSignInMethod(mAuth, activity, yyLoginListener);
                googleSignIn.login();

                break;
            case FACEBOOK:
                break;
            case TWITTER:
                break;
        }

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

    public void checkUserAlreadySigned() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            yyLoginListener.userAlreadySigned(currentUser);
        } else {
            yyLoginListener.userNotSigned();
        }

    }


    public void handleSignInResult(LoginType type, GoogleSignInResult googleSignInResult, GoogleSignInCallBack callback) {
        switch (type) {
            case GOOGLE:
                googleSignIn.handleGoogleSignResult(googleSignInResult, callback);
                break;
        }
    }


}
