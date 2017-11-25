package com.berber.orange.memories.activity.details;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.BaseActivity;
import com.berber.orange.memories.model.db.Anniversary;
import com.berber.orange.memories.utils.Utils;
import com.daimajia.numberprogressbar.NumberProgressBar;

import java.io.Serializable;

public class DetailsActivity extends BaseActivity {
    private Toolbar toolbar;
    private NumberProgressBar detailsAnniProgressbar;
    private RecyclerView timeProgressRecyclerView;

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


        detailsAnniProgressbar = findViewById(R.id.details_anni_progressbar);
        detailsAnniProgressbar.setProgress(47);


        //time progress recycler view
        timeProgressRecyclerView = findViewById(R.id.time_progress_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        timeProgressRecyclerView.setLayoutManager(linearLayoutManager);
        timeProgressRecyclerView.setAdapter(new MyTimeProgressAdapter(this));
        timeProgressRecyclerView.setAdapter(new MyTimeProgressAdapter(this));
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(toolbar);
        mImmersionBar.statusBarDarkFont(true, 0.2f);
        mImmersionBar.init();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
