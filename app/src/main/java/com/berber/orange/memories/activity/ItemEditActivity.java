package com.berber.orange.memories.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.berber.orange.memories.APP;
import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.main.ScrollingActivity;
import com.berber.orange.memories.activity.model.AnniversaryDTO;
import com.berber.orange.memories.dbservice.Anniversary;
import com.berber.orange.memories.dbservice.AnniversaryDao;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ItemEditActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ItemEditActivity";
    private MaterialCalendarView calendarView;
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private TextView mDateView;
    private TextView mLocationView;
    private TextView mDescriptionView;
    private TextView mTitleView;

    private Date selectedDate;
    private AnniversaryDao anniversaryDao;

    private final int REQUEST_CODE_FOR_ADD_ITEM = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);


        anniversaryDao = ((APP) getApplication()).getDaoSession().getAnniversaryDao();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(" ");

        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectedDate = date.getDate();
                String currentDate = FORMATTER.format(date.getDate());
                mDateView.setText(currentDate);
            }
        });

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                System.out.println("onMonthChanged");
                //System.out.println(FORMATTER.format(widget.getCurrentDate().getDate()));
                //System.out.println(FORMATTER.format(mDateView.getDate()));
            }
        });
        setCurrentDate();
    }

    private void initView() {
        mTitleView = findViewById(R.id.title_value_label);
        mTitleView.setOnClickListener(this);

        mDateView = findViewById(R.id.date_value_label);
        mLocationView = findViewById(R.id.location_value_label);
        mLocationView.setOnClickListener(this);

        Button mAddDescription = findViewById(R.id.btn_add_description);
        mAddDescription.setOnClickListener(this);

        mDescriptionView = findViewById(R.id.description_value_label);


        Button mCancel = findViewById(R.id.add_btn_cancel);
        mCancel.setOnClickListener(this);

        Button mSubmit = findViewById(R.id.add_btn_submit);
        mSubmit.setOnClickListener(this);

    }

    public void setCurrentDate() {

        calendarView.setDateSelected(new Date(), true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_value_label:
                String[] titleParams = new String[]{
                        "input", "add title for your anniversary", "2", "50", "submit", ""
                };
                alertInputDialog(titleParams, "TITLE");
                break;
            case R.id.location_value_label:
                String[] locationParams = new String[]{
                        "input", "where is your location", "2", "20", "submit", ""
                };
                alertInputDialog(locationParams, "LOCATION");
                break;
            case R.id.btn_add_description:
                String[] desParams = new String[]{
                        "input", "add description for your anniversary", "2", "150", "submit", ""
                };
                alertInputDialog(desParams, "DESCRIPTION");
                break;
            case R.id.add_btn_cancel:
                //cancel();
                break;
            case R.id.add_btn_submit:
                submit();
                break;

        }
    }

    private void alertInputDialog(String[] params, final String type) {
        new MaterialDialog.Builder(this)
                .title(params[0])
                .content(params[1])
                .inputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE)
                .inputRange(Integer.valueOf(params[2]), Integer.valueOf(params[3]))
                .positiveText(params[4])
                .input(
                        params[5],
                        params[5],
                        false,
                        new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                switch (type) {
                                    case "TITLE":
                                        mTitleView.setText(input.toString());
                                        break;
                                    case "LOCATION":
                                        mLocationView.setText(input.toString());
                                        break;
                                    case "DESCRIPTION":
                                        mDescriptionView.setText(input.toString());
                                        break;

                                }
                            }
                        })
                .show();
    }

    private void submit() {
        //insert data into database
        Anniversary anniversary = new Anniversary();
        anniversary.setTitle(mTitleView.getText().toString());
        anniversary.setLocation(mLocationView.getText().toString());
        anniversary.setDescription(mDescriptionView.getText().toString());
        anniversary.setDate(selectedDate);
        //anniversary.setRemindDate(null);
        anniversaryDao.insert(anniversary);

        //convert anniversary to dto object


        Intent intent = new Intent(ItemEditActivity.this, ScrollingActivity.class);
        intent.putExtra("object", convertToDTO(anniversary));

        //startActivityForResult(intent, 100);
        setResult(REQUEST_CODE_FOR_ADD_ITEM, intent);
        finish();

    }

    private AnniversaryDTO convertToDTO(Anniversary anniversary) {
        AnniversaryDTO dto = new AnniversaryDTO();
        dto.setTitle(anniversary.getTitle());
        dto.setLocation(anniversary.getLocation());
        dto.setDescription(anniversary.getDescription());
        dto.setDate(anniversary.getDate());
       // dto.setRemindDate(anniversary.getRemindDate());
        dto.setCreateDate(anniversary.getCreateDate());
        return dto;
    }

    private void cancel() {
        onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "onActivityResult from item", Toast.LENGTH_LONG).show();
    }
}
