package com.berber.orange.memories.activity.main;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.berber.orange.memories.APP;
import com.berber.orange.memories.R;
import com.berber.orange.memories.SharedPreferencesHelper;
import com.berber.orange.memories.activity.BaseActivity;
import com.berber.orange.memories.activity.about.AboutActivity;
import com.berber.orange.memories.activity.additem.AddItemActivity;
import com.berber.orange.memories.helper.Constant;
import com.berber.orange.memories.helper.MatisseImagePicker;
import com.berber.orange.memories.loginservice.user.MyFireBaseUser;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zhihu.matisse.Matisse;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class CoordinatorActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recycler;

    private TimeLineAdapter adapter;
    private final int REQUEST_NEW_ITEM = 9001;
    private LinearLayoutManager linearLayoutManager;
    private Toolbar toolbar;
    //因为setExpanded会调用事件监听，所以通过标志过滤掉
    public static int expendedtag = 2;
    public static final int COORDINATOR_ACTIVITY_REQUEST_CHOOSE_IMAGE = 9898;
    private ImageView mLandingPageImageView;

    public static final int RC_PICK_IMAGE_PERM = 123;

    @Override
    protected void initView() {
        super.initView();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            //verticalOffset是当前appbarLayout的高度与最开始appbarlayout高度的差，向上滑动的话是负数
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //通过日志得出活动启动是两次，由于之前有setExpanded所以三次
                if (getSupportActionBar().getHeight() - appBarLayout.getHeight() == verticalOffset) {
                    //折叠监听
                    // Toast.makeText(CoordinatorActivity.this, "折叠了", Toast.LENGTH_SHORT).show();
                    //mImmersionBar.statusBarDarkFont(true, 0.2f);
                    toolbar.setTitle("Anniversary");
                    //mImmersionBar.init();
                }
                if (expendedtag == 2 && verticalOffset == 0) {
                    //展开监听
                    //Toast.makeText(CoordinatorActivity.this, "展开了", Toast.LENGTH_SHORT).show();
                    //  mImmersionBar.statusBarDarkFont(false, 0.2f);
                    // mImmersionBar.init();
                    toolbar.setTitle("");

                }
                if (expendedtag != 2 && verticalOffset == 0) {
                    expendedtag++;
                }
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);

        CircleImageView user_photo = headerView.findViewById(R.id.user_display_photo);
        TextView user_name = headerView.findViewById(R.id.user_display_name);


        String displayName;
        Uri photoUri;
        MyFireBaseUser user = getIntent().getParcelableExtra("user");
        if (user == null) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            displayName = currentUser.getDisplayName();
            photoUri = Uri.parse(String.valueOf(currentUser.getPhotoUrl()));
        } else {
            displayName = user.getDisplayName();
            photoUri = Uri.parse(user.getPhotoUri());
        }

        Glide.with(this).load(photoUri.toString()).into(user_photo);
        user_name.setText("Hello, dear " + displayName);


        Glide.with(this).load(photoUri.toString()).into(user_photo);
        user_name.setText("Hello, dear " + displayName);


        mLandingPageImageView = findViewById(R.id.image_content);

        Glide.with(this).load(R.drawable.user_profile_bg).into(mLandingPageImageView);

        //save uri into shared preference
        initRecycler();
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(toolbar);
        mImmersionBar.statusBarDarkFont(true, 0.2f);
        mImmersionBar.init();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void doTaskAfterPermissionsGranted(int requestCode) {
        if (requestCode == RC_PICK_IMAGE_PERM) {
            MatisseImagePicker.open(this, COORDINATOR_ACTIVITY_REQUEST_CHOOSE_IMAGE);
        }
    }

    private void initRecycler() {

        recycler = findViewById(R.id.time_line_recycler);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        adapter = new TimeLineAdapter(this);

        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(adapter);


        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recycler);


    }

//    private List<Anniversary> getData() {
//        List<Anniversary> anniversaries = anniversaryDao.queryBuilder().list();
//        return anniversaries.isEmpty() ? new ArrayList<Anniversary>() : anniversaries;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_coordinator, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(CoordinatorActivity.this, AddItemActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_anniversary) {
            // Handle the camera action
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //
        //adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_NEW_ITEM:
                break;
            case COORDINATOR_ACTIVITY_REQUEST_CHOOSE_IMAGE:
                if (data == null) {
                    return;
                }
                List<Uri> mSelected = Matisse.obtainResult(data);
                if (!mSelected.isEmpty()) {
                    Glide.with(this).load(mSelected.get(0)).into(mLandingPageImageView);
                    //save uri into shared preference
                    SharedPreferencesHelper.getInstance().saveData("main_cover", mSelected.get(0).toString());
                }
                Log.d("Matisse", "mSelected: " + mSelected);
                break;

            case Constant.COORDINATOR_OPEN_SETTING_ACTIVITY:
                //loading new image
                int defaultRes = R.drawable.backgroud4;
                String main_cover = (String) SharedPreferencesHelper.getInstance().getData("main_cover", String.valueOf(defaultRes));
                Log.e("TAG", "test: " + main_cover);
                Glide.with(this).load(main_cover).into(mLandingPageImageView);
                break;

        }
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
