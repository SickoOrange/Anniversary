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

import com.afollestad.materialdialogs.MaterialDialog;
import com.berber.orange.memories.R;
import com.berber.orange.memories.activity.main.ScrollingActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
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
        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.WEDNESDAY)
                .setMinimumDate(CalendarDay.from(2016, 4, 3))
                .setMaximumDate(CalendarDay.from(2016, 5, 12))
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_value_label:
                alertTitleInputDialog();
                break;
            case R.id.location_value_label:
                alertLocationInputDialog();
                break;
            case R.id.btn_add_description:
                alertDescriptionDialog();
                break;
            case R.id.add_btn_cancel:
                cancel();
                break;
            case R.id.add_btn_submit:
                submit();
                break;

        }
    }

    private void submit() {
        Intent intent = new Intent(ItemEditActivity.this, ScrollingActivity.class);
        AnniversaryDTO dto = new AnniversaryDTO();
        dto.setTitle(mTitleView.getText().toString());
        dto.setDescription(mDescriptionView.getText().toString());
        dto.setLocation(mLocationView.getText().toString());
        dto.setDate(selectedDate);
        intent.putExtra("object", dto);
        startActivity(intent);
    }

    private void cancel() {
        onBackPressed();
    }

    private void alertTitleInputDialog() {
        new MaterialDialog.Builder(this)
                .title("Input")
                .content("add title for your anniversary")
                .inputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE)
                .inputRange(2, 16)
                .positiveText("submit")
                .input(
                        "title",
                        "description",
                        false,
                        new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                mTitleView.setText(input.toString());
                            }
                        })
                .show();
    }

    private void alertDescriptionDialog() {
        new MaterialDialog.Builder(this)
                .title("Input")
                .content("add description for your anniversary")
                .inputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE)
                .inputRange(2, 160)
                .positiveText("submit")
                .input(
                        "description",
                        "description",
                        false,
                        new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                mDescriptionView.setText(input.toString());
                            }
                        })
                .show();

    }

    private void alertLocationInputDialog() {
        new MaterialDialog.Builder(this)
                .title("Input")
                .content("where is your location")
                .inputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                                | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(2, 16)
                .positiveText("submit")
                .input(
                        "London",
                        "London",
                        false,
                        new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                mLocationView.setText(input.toString());
                            }
                        })
                .show();

    }
}
