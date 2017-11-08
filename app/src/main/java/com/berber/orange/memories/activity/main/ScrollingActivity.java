package com.berber.orange.memories.activity.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.berber.orange.memories.APP;
import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.AddItemActivity;
import com.berber.orange.memories.activity.AnniversaryDTO;
import com.berber.orange.memories.activity.ItemEditActivity;
import com.berber.orange.memories.adapter.TimeLineAdapter;
import com.berber.orange.memories.dbservice.Anniversary;
import com.berber.orange.memories.dbservice.AnniversaryDao;
import com.berber.orange.memories.dbservice.DaoSession;
import com.berber.orange.memories.loginservice.user.MyFireBaseUser;
import com.berber.orange.memories.utils.Utils;
import com.berber.orange.memories.vega.VegaLayoutManager;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ScrollingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ScrollingActivity";
    private RecyclerView recycler;
    private DaoSession daoSession;
    private AnniversaryDao anniversaryDao;
    private TimeLineAdapter adapter;
    private final int REQUEST_NEW_ITEM = 9001;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        daoSession = ((APP) getApplication()).getDaoSession();
        anniversaryDao = daoSession.getAnniversaryDao();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivityForResult(new Intent(ScrollingActivity.this, AddItemActivity.class), REQUEST_NEW_ITEM);
                //  startActivityForResult(new Intent(ScrollingActivity.this, AddItemActivity.class));
            }
        });

        TabLayout tab = findViewById(R.id.tab);
        tab.addTab(tab.newTab().setText("Time Line"));
        tab.addTab(tab.newTab().setText("Grid Line"));


        ImageView imageView = findViewById(R.id.image_content);
        Glide.with(this).load("https://cdn.pixabay.com/photo/2016/10/28/11/57/tic-tac-toe-1777859_960_720.jpg").into(imageView);
        initRecycler();

    }


    private void initRecycler() {

        recycler = findViewById(R.id.time_line_recycler);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        adapter = new TimeLineAdapter(getData(), this.getApplicationContext());

        recycler.setLayoutManager(new VegaLayoutManager());
        recycler.setAdapter(adapter);
    }

    private List<Anniversary> getData() {
        return anniversaryDao.queryBuilder().list();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_NEW_ITEM:
                if (data == null) {
                    return;
                }
                AnniversaryDTO dto = data.getParcelableExtra("object");
               // adapter.addNewItem(dto, adapter);
                Utils.showToast(ScrollingActivity.this, "add new item", Toast.LENGTH_LONG);
                Log.e("TAG", dto.toString());
                break;
        }
    }
}
