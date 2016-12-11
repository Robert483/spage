package com.thathustudio.spage.temp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.thathustudio.spage.R;
import com.thathustudio.spage.activities.LoginActivity;

public class TempActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        // Redirect or show fragment
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        // -------------------------

        finish();
    }
}
