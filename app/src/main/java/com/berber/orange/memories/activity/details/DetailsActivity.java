package com.berber.orange.memories.activity.details;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.berber.orange.memories.R;
import com.berber.orange.memories.model.db.Anniversary;
import com.berber.orange.memories.utils.Utils;

import java.io.Serializable;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        if (intent != null) {
            Anniversary anniversary = (Anniversary) intent.getSerializableExtra("obj");
            Utils.showToast(this, anniversary.getTitle(), Toast.LENGTH_LONG);
        }
    }
}
