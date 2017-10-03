package com.berber.orange.memories.login.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.berber.orange.memories.Manifest;
import com.berber.orange.memories.R;
import com.berber.orange.memories.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.filter.Filter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.EasyPermissions;

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "SignUpActivity";

    private static final int REQUEST_CODE_CHOOSE = 1000;
    private EditText mUserNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button signUp;
    private FirebaseAuth mAuth;
    List<Uri> mSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_sign_up);

        CircleImageView pick = findViewById(R.id.pick_photo);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        initView();
        mAuth = FirebaseAuth.getInstance();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            Log.d("Matisse", "mSelected: " + mSelected);
        }
    }

    private void initView() {
        mUserNameView = findViewById(R.id.user_name);
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);

        signUp = findViewById(R.id.sign_up_start_button);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        final String password = mPasswordView.getText().toString();
        final String validateEmail = Utils.validateEmail(mEmailView.getText().toString());
        Log.d(TAG, "createUserWithEmail:onComplete:" + validateEmail + password);
        mAuth.createUserWithEmailAndPassword(validateEmail, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Log.d(TAG, "onComplete: Failed=" + task.getException().getMessage());
                    Toast.makeText(SignUpActivity.this, "Create User Account failed",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "Create User Account succeeds",
                            Toast.LENGTH_SHORT).show();
                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    //update user profile
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(mUserNameView.getText().toString())
                            // TODO: 01.10.2017 convert local image to uri
                            //.setPhotoUri()
                            .build();
                    currentUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "user profile updated");
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            }
                        }
                    });

                }

            }
        });
    }

}
