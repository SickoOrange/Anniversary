package com.berber.orange.memories.activity.details;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.BaseActivity;
import com.berber.orange.memories.model.db.Anniversary;
import com.berber.orange.memories.utils.Utils;

import java.io.Serializable;

public class DetailsActivity extends BaseActivity {
    private Toolbar toolbar;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_details);
//        Intent intent = getIntent();
//        if (intent != null) {
//            Anniversary anniversary = (Anniversary) intent.getSerializableExtra("obj");
//            System.out.println("anniversary: " + anniversary.getTitle());
//        }
//    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_details;
    }

    @Override
    protected void initView() {
        super.initView();
        toolbar = findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
