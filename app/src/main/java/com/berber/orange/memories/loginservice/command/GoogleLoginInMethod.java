package com.berber.orange.memories.loginservice.command;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.berber.orange.memories.R;
import com.berber.orange.memories.loginservice.service.BaseLoginInCallBack;
import com.berber.orange.memories.loginservice.service.GoogleLoginInCallBack;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
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

public class GoogleLoginInMethod extends BaseLoginInMethod {
    public static final int RC_GOOGLE_SIGN_IN = 9001;
    private static final String TAG = "GoogleLoginInMethod";


    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;

    public GoogleLoginInMethod(FirebaseAuth mAuth, Activity activity, BaseLoginInCallBack baseLoginInCallBack) {
        super(mAuth, activity, baseLoginInCallBack);
        prepareLogin();
    }

    private void prepareLogin() {
        //prepare google sign in
        if (gso == null) {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getStringFromResource(R.string.google_web_id))
                    .requestEmail()
                    .build();
        }


        if (mGoogleApiClient == null) {
            // Build a GoogleApiClient with access to the Google Sign-In API and the
            // options specified by gso.
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage((FragmentActivity) getActivity() /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            // TODO: 02.10.2017  google sign in, on connection failed to something
                        }
                    })
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
    }


    @Override
    public void login() {
        Intent googleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        getActivity().startActivityForResult(googleSignInIntent, RC_GOOGLE_SIGN_IN);
    }

    @Override
    public void logout() {

    }

    @Override
    public void signUp() {

    }

    public void handleGoogleSignResult(Object result, GoogleLoginInCallBack callback) {
        GoogleSignInResult googleSignInResult = (GoogleSignInResult) result;
        Log.d(TAG, "handleGoogleSignInResult:" + googleSignInResult.isSuccess() + " " + googleSignInResult.getStatus());
        if (googleSignInResult.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = googleSignInResult.getSignInAccount();
            Log.d(TAG, "handleGoogleSignInResult:" + acct.getDisplayName() + " " + acct.getPhotoUrl());
            firebaseAuthWithGoogle(acct, callback);
        } else {
            // TODO: 02.10.2017  google sign in failed
            Log.d(TAG, googleSignInResult.getStatus().toString());
            callback.onGoogleSignInFailure(googleSignInResult.getStatus());
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct, final GoogleLoginInCallBack callback) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);


        getmAuth().signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser firebaseUser = getmAuth().getCurrentUser();
                            callback.onGoogleSignInSuccess(acct, firebaseUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            callback.onGoogleWithFireBaseAuthFailure(task.getException());
                        }

                    }
                });
    }

}
