package com.thathustudio.spage.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.thathustudio.spage.R;
import com.thathustudio.spage.view.Zoom;

public class ImageViewerActivity extends AppCompatActivity {

    Zoom imv;
    AppCompatImageButton btnClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        imv = (Zoom) findViewById(R.id.activity_image_viewer);
        btnClose = (AppCompatImageButton)findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String url = getIntent().getStringExtra("URL");
        Glide.with(this).load(url).into(imv);


    }
}
