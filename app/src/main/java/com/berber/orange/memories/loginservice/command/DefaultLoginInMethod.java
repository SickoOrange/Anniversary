package com.berber.orange.memories.loginservice.command;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.berber.orange.memories.loginservice.service.BaseLoginInCallBack;
import com.berber.orange.memories.loginservice.service.DefaultLoginInCallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by yinya
 * on 02.10.2017.
 */

public class DefaultLoginInMethod extends BaseLoginInMethod {
    private static final String TAG = "DefaultLoginInMethod";
    private DefaultLoginInCallBack defaultSignInCallBack;

    public DefaultLoginInMethod(FirebaseAuth mAuth, Activity activity, BaseLoginInCallBack baseLoginInCallBack) {
        super(mAuth, activity, baseLoginInCallBack);
    }

    @Override
    public void login(String email, String password, final BaseLoginInCallBack callBack) {

        if (callBack instanceof DefaultLoginInCallBack) {
            defaultSignInCallBack = (DefaultLoginInCallBack) callBack;
        }


        getmAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.

                if (!task.isSuccessful()) {
                    Log.e(TAG, "Sign in:failed", task.getException());



                    defaultSignInCallBack.loginFailure(task);
                    //getYyLoginListener().onLoginFailure();
                } else {
                    FirebaseUser currentUser = getmAuth().getCurrentUser();
                    Log.e(TAG, "Sign in succeeds, user name: " + currentUser.getDisplayName());
                    defaultSignInCallBack.loginSucceeds(currentUser);


                }
            }
        });
    }

}
