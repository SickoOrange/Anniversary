package com.berber.orange.memories.activity.login;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.berber.orange.memories.R;
import com.berber.orange.memories.SharedPreferencesHelper;
import com.berber.orange.memories.activity.BaseActivity;
import com.berber.orange.memories.activity.main.CoordinatorActivity;
import com.berber.orange.memories.database.FirebaseDatabaseHelper;
import com.berber.orange.memories.loginservice.command.LoginType;
import com.berber.orange.memories.loginservice.YYLoginServer;
import com.berber.orange.memories.loginservice.command.GoogleLoginInMethod;
import com.berber.orange.memories.loginservice.service.DefaultLoginInCallBack;
import com.berber.orange.memories.loginservice.service.FacebookLoginInCallBack;
import com.berber.orange.memories.loginservice.service.GoogleLoginInCallBack;
import com.berber.orange.memories.loginservice.service.UserExistingListener;
import com.berber.orange.memories.loginservice.user.MyFireBaseUser;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements OnClickListener {


    @Override
    protected void initView() {
        super.initView();
        //init YY Smart Login
        YYLoginServer.INSTANCE.Init();
        // Set up the login form.
        //init ui reference
        Button signInGoogle = findViewById(R.id.sign_in_google);
        Button signInFacebook = findViewById(R.id.sign_in_facebook);

        //add button event listener
        signInGoogle.setOnClickListener(this);
        signInFacebook.setOnClickListener(this);


        //  ImageView loginImageView = findViewById(R.id.login_bg_image_view);


    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_simple_login;
    }

    @Override
    protected void doTaskAfterPermissionsGranted(int requestCode) {

    }


    @Override
    protected void onStart() {
        super.onStart();
        YYLoginServer.INSTANCE.addAuthStateListener();
        YYLoginServer.INSTANCE.checkUserAlreadySigned(userExistingListener);

    }

    @Override
    protected void onStop() {
        super.onStop();

        YYLoginServer.INSTANCE.removeAuthStateListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_google:
                YYLoginServer.INSTANCE.loginWithGoogle(LoginActivity.this, mGoogleApiClient);
                break;
            case R.id.sign_in_facebook:
                YYLoginServer.INSTANCE.loginWithFacebook(LoginActivity.this, facebookLoginInCallBack);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GoogleLoginInMethod.RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            YYLoginServer.INSTANCE.handleSignInResult(LoginType.GOOGLE, googleSignInResult, googleSignInCallBack);
        }

        //handle facebook result
        YYLoginServer.INSTANCE.handleFacebookResult(requestCode, resultCode, data);
    }


    DefaultLoginInCallBack defaultSignInCallBack = new DefaultLoginInCallBack() {
        @Override
        public void loginSucceeds(FirebaseUser currentUser) {
            startActivity(new Intent(LoginActivity.this, CoordinatorActivity.class));
            LoginActivity.this.finish();
        }

        @Override
        public void loginFailure(Task<AuthResult> task) {
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle(getString(R.string.login_error_dialog_title))
                    .setMessage("Sign in failed:" + "\n" + task.getException())
                    .setPositiveButton(getString(R.string.dialog_ok), null)
                    .show();
        }
    };

    GoogleLoginInCallBack googleSignInCallBack = new GoogleLoginInCallBack() {
        @Override
        public void onGoogleSignInSuccess(GoogleSignInAccount acct, FirebaseUser firebaseUser) {
            SharedPreferencesHelper.getInstance().saveData("user_uuid", firebaseUser.getUid());
            FirebaseDatabaseHelper.getInstance().buildRootUser(firebaseUser);
            startActivity(new Intent(LoginActivity.this, CoordinatorActivity.class));
            LoginActivity.this.finish();
        }

        @Override
        public void onGoogleSignInFailure(Status exception) {

        }

        @Override
        public void onGoogleWithFireBaseAuthFailure(Exception exception) {

        }
    };

    FacebookLoginInCallBack facebookLoginInCallBack = new FacebookLoginInCallBack() {


        @Override
        public void facebookLoginSucceed(LoginResult loginResult) {

        }

        @Override
        public void facebookLoginCancel() {

        }

        @Override
        public void facebookLoginOnError() {

        }

        @Override
        public void facebookLoginWithFireBaseSucceed(FirebaseUser user, JSONObject object) {

            MyFireBaseUser myUser = new MyFireBaseUser();
            try {
                myUser.setEmail(object.getString("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SharedPreferencesHelper.getInstance().saveData("user_uuid", user.getUid());
            FirebaseDatabaseHelper.getInstance().buildRootUser(user);

            myUser.setDisplayName(user.getDisplayName());
            myUser.setPhotoUri(user.getPhotoUrl().toString());
            Intent intent = new Intent(LoginActivity.this, CoordinatorActivity.class);
            intent.putExtra("user", myUser);

            //start to go to new activity
            LoginActivity.this.startActivity(intent);
            //finish current activity
            LoginActivity.this.finish();

        }

        @Override
        public void facebookLoginWithFireBaseFailure(Task<AuthResult> task) {

        }
    };

    UserExistingListener userExistingListener = new UserExistingListener() {
        @Override
        public void isUserExisting(boolean b, FirebaseUser currentUser) {
            if (b) {

                MyFireBaseUser user = new MyFireBaseUser();
                user.setEmail(currentUser.getEmail());
                user.setDisplayName(currentUser.getDisplayName());
                user.setPhotoUri(currentUser.getPhotoUrl().toString());
                Intent intent = new Intent(LoginActivity.this, CoordinatorActivity.class);
                intent.putExtra("user", user);
                //start to go to new activity
                LoginActivity.this.startActivity(intent);
                //finish current activity
                LoginActivity.this.finish();
            }
        }
    };

}






