package com.berber.orange.memories.login;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.berber.orange.memories.login.activity.LoginActivity;
import com.berber.orange.memories.login.activity.SignUpActivity;
import com.berber.orange.memories.login.command.DefaultLoginInMethod;
import com.berber.orange.memories.login.command.FacebookLoginInMethod;
import com.berber.orange.memories.login.command.GoogleLoginInMethod;
import com.berber.orange.memories.login.service.BaseLoginInCallBack;
import com.berber.orange.memories.login.service.DefaultCreateAccountListener;
import com.berber.orange.memories.login.service.DefaultLoginInCallBack;
import com.berber.orange.memories.login.service.GoogleLoginInCallBack;
import com.berber.orange.memories.login.service.UserExistingListener;
import com.berber.orange.memories.login.user.MyFireBaseUser;
import com.berber.orange.memories.utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

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

    public void createAccount(Activity activity, final MyFireBaseUser user, final DefaultCreateAccountListener defaultCreateAccountListener) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassworld()).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    defaultCreateAccountListener.onCreateAccountFailure(task);
                } else {

                    defaultCreateAccountListener.onCreateAccountSucceed(mAuth.getCurrentUser());

                    //update user profile
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(user.getDisplayName())
                            // TODO: 01.10.2017 convert local image to uri
                            .setPhotoUri(user.getPhotoUri())
                            .build();
                    mAuth.getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                defaultCreateAccountListener.onUploadProfileSucceed(task);
                            } else {
                                defaultCreateAccountListener.onUploadProfileFailure(task);
                            }
                        }
                    });

                }

            }
        });
    }
}
