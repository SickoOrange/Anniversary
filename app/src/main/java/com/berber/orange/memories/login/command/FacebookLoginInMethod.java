package com.berber.orange.memories.login.command;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.berber.orange.memories.login.service.BaseLoginInCallBack;
import com.berber.orange.memories.login.service.FacebookLoginInCallBack;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

/**
 * Created by yinya
 * on 03.10.2017.
 */

public class FacebookLoginInMethod extends BaseLoginInMethod {

    private static final String TAG = "FacebookLoginInMethod";
    private CallbackManager callbackManager;

    private FacebookLoginInCallBack facebookLoginInCallBack;

    public FacebookLoginInMethod(FirebaseAuth mAuth, Activity activity, BaseLoginInCallBack baseLoginInCallBack) {
        super(mAuth, activity, baseLoginInCallBack);

        BaseLoginInCallBack callBack = getCallBack();
        if (callBack instanceof FacebookLoginInCallBack) {
            facebookLoginInCallBack = (FacebookLoginInCallBack) callBack;
        }

        prepareFacebookLoginIn();
    }

    private void prepareFacebookLoginIn() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess111:" + loginResult);
                facebookLoginInCallBack.facebookLoginSucceed(loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                facebookLoginInCallBack.facebookLoginCancel();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                facebookLoginInCallBack.facebookLoginOnError();
            }
        });

    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);


        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            facebookLoginInCallBack.facebookLoginWithFireBaseSucceed(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getActivity().getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            facebookLoginInCallBack.facebookLoginWithFireBaseFailure(task);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void login() {

        LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile"));

    }

    public void handleFacebookResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
