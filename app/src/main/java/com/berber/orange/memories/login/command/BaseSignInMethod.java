package com.berber.orange.memories.login.command;

import android.app.Activity;
import android.util.Log;

import com.berber.orange.memories.login.YYLoginListener;
import com.berber.orange.memories.login.service.BaseSignInCallBack;
import com.berber.orange.memories.login.service.GoogleSignInCallBack;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by yinya
 * on 02.10.2017.
 */

public class BaseSignInMethod implements BaseSignInMethodInterface {


    private final FirebaseAuth mAuth;
    private final YYLoginListener yyLoginListener;
    private Activity activity;

    public BaseSignInMethod(FirebaseAuth mAuth, Activity activity, YYLoginListener yyLoginListener) {
        this.mAuth = mAuth;
        this.activity = activity;
        this.yyLoginListener = yyLoginListener;
    }


    public String getStringFromResource(int id) {
        return activity.getApplicationContext().getResources().getString(id);
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public YYLoginListener getYyLoginListener() {
        return yyLoginListener;
    }


    @Override
    public void login(String email, String password, BaseSignInCallBack callBack) {

    }

    @Override
    public void login() {

    }

    @Override
    public void logout() {

    }

    @Override
    public void signUp() {

    }
}
