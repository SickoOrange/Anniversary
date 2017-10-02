package com.berber.orange.memories.login.command;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.berber.orange.memories.R;
import com.berber.orange.memories.ScrollingActivity;
import com.berber.orange.memories.login.YYLoginListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by yinya
 * on 02.10.2017.
 */

public class DefaultSignInMethod extends BaseSignInMethod {
    private static final String TAG = "DefaultSignInMethod";

    public DefaultSignInMethod(FirebaseAuth mAuth, Activity activity, YYLoginListener yyLoginListener) {
        super(mAuth, activity, yyLoginListener);
    }

    @Override
    public void login(String email, String password) {
        getmAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.

                // TODO: 02.10.2017 expose the callback to the user
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Sign in:failed", task.getException());

                    new AlertDialog.Builder(getActivity())
                            .setTitle(getStringFromResource(R.string.login_error_dialog_title))
                            .setMessage("Sign in failed:" + "\n" + task.getException())
                            .setPositiveButton(getStringFromResource(R.string.dialog_ok), null)
                            .show();

                    getYyLoginListener().onLoginFailure();
                } else {
                    FirebaseUser currentUser = getmAuth().getCurrentUser();
                    Log.e(TAG, "Sign in succeeds, user name: " + currentUser.getDisplayName());
                    getActivity().startActivity(new Intent(getActivity(), ScrollingActivity.class));
                    getYyLoginListener().onLoginSuccess(currentUser);
                }
            }
        });
    }


}
