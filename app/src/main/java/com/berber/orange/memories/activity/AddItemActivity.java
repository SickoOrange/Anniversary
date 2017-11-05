package com.berber.orange.memories.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.berber.orange.memories.R;

import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends AppCompatActivity {

    private static final int HOME_ITEM_SIZE = 10;

    private ViewPager viewPager;
    private ArrayList<ModelAnniversaryType> modelAnniversaryTypes;
    private LinearLayout homeEntrance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Your Anniversary");

        initAnniversaryTypeData();
        initView();
        init();

    }

    private void init() {

        int pageCount = (int) Math.ceil(modelAnniversaryTypes.size() * 1.0 / HOME_ITEM_SIZE);
        List<View> viewList = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < pageCount; i++) {
            RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.item_recycler_view, null, false);
            recyclerView.setLayoutManager(new GridLayoutManager(AddItemActivity.this, 5));
            //add adapter to every recycler view
            AnniversaryTypeRecyclerViewAdapter anniversaryTypeRecyclerViewAdapter = new AnniversaryTypeRecyclerViewAdapter(AddItemActivity.this, modelAnniversaryTypes, i, HOME_ITEM_SIZE);
            recyclerView.setAdapter(anniversaryTypeRecyclerViewAdapter);

            viewList.add(recyclerView);
            //viewList.add(inflater.inflate(R.layout.test, viewPager, false));
        }

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(viewList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // TODO: 2017/11/05  switch  indicator
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {
        viewPager = findViewById(R.id.anniversary_type_vp);
    }

    private void initAnniversaryTypeData() {
        modelAnniversaryTypes = new ArrayList<>();
        modelAnniversaryTypes.add(new ModelAnniversaryType("美食", R.mipmap.ic_category_0));
        modelAnniversaryTypes.add(new ModelAnniversaryType("电影", R.mipmap.ic_category_1));
        modelAnniversaryTypes.add(new ModelAnniversaryType("酒店住宿", R.mipmap.ic_category_2));
        modelAnniversaryTypes.add(new ModelAnniversaryType("生活服务", R.mipmap.ic_category_3));
        modelAnniversaryTypes.add(new ModelAnniversaryType("KTV", R.mipmap.ic_category_4));
        modelAnniversaryTypes.add(new ModelAnniversaryType("旅游", R.mipmap.ic_category_5));
        modelAnniversaryTypes.add(new ModelAnniversaryType("学习培训", R.mipmap.ic_category_6));
        modelAnniversaryTypes.add(new ModelAnniversaryType("汽车服务", R.mipmap.ic_category_7));
        modelAnniversaryTypes.add(new ModelAnniversaryType("摄影写真", R.mipmap.ic_category_8));
        modelAnniversaryTypes.add(new ModelAnniversaryType("休闲娱乐", R.mipmap.ic_category_10));
        modelAnniversaryTypes.add(new ModelAnniversaryType("丽人", R.mipmap.ic_category_11));
        modelAnniversaryTypes.add(new ModelAnniversaryType("运动健身", R.mipmap.ic_category_12));
        modelAnniversaryTypes.add(new ModelAnniversaryType("大保健", R.mipmap.ic_category_13));
        modelAnniversaryTypes.add(new ModelAnniversaryType("团购", R.mipmap.ic_category_14));
        modelAnniversaryTypes.add(new ModelAnniversaryType("景点", R.mipmap.ic_category_16));
        modelAnniversaryTypes.add(new ModelAnniversaryType("全部分类", R.mipmap.ic_category_15));
    }

}
