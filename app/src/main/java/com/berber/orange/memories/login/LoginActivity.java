package com.berber.orange.memories.login;

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
import com.berber.orange.memories.ScrollingActivity;
import com.berber.orange.memories.login.command.GoogleSignInMethod;
import com.berber.orange.memories.login.service.DefaultSignInCallBack;
import com.berber.orange.memories.login.service.GoogleSignInCallBack;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener, YYLoginListener {

    private static final String TAG = "LoginActivity";

    private static final String WEB_ID = "1007045828483-limqn92o7logo67ddrqejj3hn7fcurnr.apps.googleusercontent.com";


    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //init YY Smart Login
        YYLoginServer.INSTANCE.Init();
        YYLoginServer.INSTANCE.setyyLoginListener(this);
        // Set up the login form.
        initView();
    }

    private void initView() {
        //init ui reference
        mEmailView = findViewById(R.id.email_text_view);
        mPasswordView = findViewById(R.id.password_text_view);
        Button signInButton = findViewById(R.id.sign_in_button);
        Button signOutButton = findViewById(R.id.sign_up_button);

        //add button event listener
        signInButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);

        //google sign in button
        ImageButton googleSignInButton = findViewById(R.id.google_sign_in);
        googleSignInButton.setOnClickListener(this);

        //facebook sign in button
        ImageButton facebookSignInButton = findViewById(R.id.facebook_login_in);
        facebookSignInButton.setOnClickListener(this);

        //twitter sign in button
        ImageButton twitterSignInButton = findViewById(R.id.twitter_login_in);
        twitterSignInButton.setOnClickListener(this);

        ImageView loginImageView = findViewById(R.id.login_bg_image_view);

        Glide.with(this).load(R.drawable.couple_love_silhouettes_happiness_116879_1080x1920).into(loginImageView);
        ImageView appLogImageView = findViewById(R.id.app_logo_image_view);
        Glide.with(this).load(R.drawable.logo).into(appLogImageView);


    }

    @Override
    protected void onStart() {
        super.onStart();

        YYLoginServer.INSTANCE.checkUserAlreadySigned();
        YYLoginServer.INSTANCE.addAuthStateListener();

    }

    @Override
    protected void onStop() {
        super.onStop();

        YYLoginServer.INSTANCE.removeAuthStateListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                String email = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();
                YYLoginServer.INSTANCE.signIn(LoginType.DEFAULT, LoginActivity.this, email, password, defaultSignInCallBack);
                break;
            case R.id.sign_up_button:
                startActivity(new Intent(this, SignUpActivity.class));
                break;

            case R.id.google_sign_in:
                YYLoginServer.INSTANCE.signIn(LoginType.GOOGLE, LoginActivity.this, null, null, null);
                break;
//
//            case R.id.sign_out_button:
//                //FirebaseAuth.getInstance().signOut();
//                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(@NonNull Status status) {
//                        Log.d(TAG, "status " + status);
//                    }
//                });
//                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GoogleSignInMethod.RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            YYLoginServer.INSTANCE.handleSignInResult(LoginType.GOOGLE, googleSignInResult, googleSignInCallBack);
        }
    }


    DefaultSignInCallBack defaultSignInCallBack = new DefaultSignInCallBack() {
        @Override
        public void loginSucceeds(FirebaseUser currentUser) {
            startActivity(new Intent(LoginActivity.this, ScrollingActivity.class));
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

    GoogleSignInCallBack googleSignInCallBack = new GoogleSignInCallBack() {
        @Override
        public void onGoogleSignInSuccess(GoogleSignInAccount acct, FirebaseUser firebaseUser) {
            startActivity(new Intent(LoginActivity.this, ScrollingActivity.class));
            LoginActivity.this.finish();
        }

        @Override
        public void onGoogleSignInFailure(Status exception) {

        }

        @Override
        public void onGoogleWithFireBaseAuthFailure(Exception exception) {

        }
    };


    //status login manage call back interface
    @Override
    public void UserSignIn(FirebaseUser user) {

    }

    @Override
    public void UserSignOut() {

    }

    @Override
    public void onLoginSuccess(FirebaseUser currentUser) {

    }

    @Override
    public void onLoginFailure() {

    }

    @Override
    public void userAlreadySigned(FirebaseUser currentUser) {
        //skip login activity
        startActivity(new Intent(LoginActivity.this, ScrollingActivity.class));
    }


    @Override
    public void userNotSigned() {
        //user not signed, do something...
    }
}






