package com.berber.orange.memories.loginservice.command;

import android.app.Activity;

import com.berber.orange.memories.loginservice.service.BaseLoginInCallBack;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by yinya
 * on 02.10.2017.
 */

public class BaseLoginInMethod implements BaseLoginInMethodInterface {


    private final FirebaseAuth mAuth;
    private final BaseLoginInCallBack baseLoginInCallBack;
    private Activity activity;

    public BaseLoginInMethod(FirebaseAuth mAuth, Activity activity, BaseLoginInCallBack baseLoginInCallBack) {
        this.mAuth = mAuth;
        this.activity = activity;
        this.baseLoginInCallBack = baseLoginInCallBack;
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

    public BaseLoginInCallBack getCallBack() {
        return baseLoginInCallBack;
    }


    @Override
    public void login(String email, String password, BaseLoginInCallBack callBack) {

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
