package com.berber.orange.memories.activity.login;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.main.ScrollingActivity;
import com.berber.orange.memories.loginservice.YYLoginServer;
import com.berber.orange.memories.loginservice.service.DefaultCreateAccountListener;
import com.berber.orange.memories.loginservice.user.MyFireBaseUser;
import com.berber.orange.memories.utils.Utils;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;


import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "SignUpActivity";

    private static final int REQUEST_CODE_CHOOSE = 1000;
    private EditText mUserNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button signUp;
    private EditText mConfirmPassword;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_sign_up);

        CircleImageView pick = findViewById(R.id.pick_photo);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 03.10.2017  image picker
            }
        });

        initView();


    }


    private void initView() {
        mUserNameView = findViewById(R.id.user_name);
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mConfirmPassword = findViewById(R.id.editText);

        signUp = findViewById(R.id.sign_up_start_button);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String password = mPasswordView.getText().toString();

                final boolean validateEmail = Utils.validate(mEmailView.getText().toString());

                String cPassword = mConfirmPassword.getText().toString();

                String userName = mUserNameView.getText().toString();

                if (!validateEmail) {
                    new AlertDialog.Builder(SignUpActivity.this)
                            .setTitle(getString(R.string.sign_up_error_dialog_title))
                            .setMessage("Email Format Error!")
                            .setPositiveButton(getString(R.string.dialog_ok), null)
                            .show();
                    return;
                }


                //user name, password and confirm password can't be empty
                if (TextUtils.isEmpty(password) && TextUtils.isEmpty(cPassword)) {
                    new AlertDialog.Builder(SignUpActivity.this)
                            .setTitle(getString(R.string.sign_up_error_dialog_title))
                            .setMessage("Password and Confirm Password can't be empty")
                            .setPositiveButton(getString(R.string.dialog_ok), null)
                            .show();
                    return;
                }

                if (!password.endsWith(cPassword)) {
                    new AlertDialog.Builder(SignUpActivity.this)
                            .setTitle(getString(R.string.sign_up_error_dialog_title))
                            .setMessage("Password and Confirm Password not match")
                            .setPositiveButton(getString(R.string.dialog_ok), null)
                            .show();
                    return;
                }


                MyFireBaseUser myFireBaseUser = new MyFireBaseUser();

                myFireBaseUser.setEmail(mEmailView.getText().toString());
                myFireBaseUser.setPassword(password);
                myFireBaseUser.setDisplayName(userName);
                myFireBaseUser.setConfirmPassword(cPassword);

                // TODO: 03.10.2017 set photo uri
                //myFireBaseUser.setPhotoUri(//);


                YYLoginServer.INSTANCE.createAccount(SignUpActivity.this, myFireBaseUser, new DefaultCreateAccountListener() {
                    @Override
                    public void onCreateAccountSucceed(FirebaseUser currentUser) {
                        Log.d(TAG, "onCreateAccountSucceed");
                        Toast.makeText(SignUpActivity.this, "Create User Account succeeds",
                                Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCreateAccountFailure(Task<AuthResult> task) {
                        Log.d(TAG, "onCreateAccountFailure" + task.getException().getMessage());
                        // TODO: 04.10.2017 give the error dialog for showing the reason, while user cant create a account.
                        Toast.makeText(SignUpActivity.this, "Create User Account failed",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUploadProfileSucceed(Task<Void> task) {
                        SignUpActivity.this.startActivity(new Intent(SignUpActivity.this, ScrollingActivity.class));
                    }

                    @Override
                    public void onUploadProfileFailure(Task<Void> task) {
                        Log.d(TAG, "onUploadProfileFailure" + task.getException().getMessage());

                    }
                });
            }
        });
    }


}
