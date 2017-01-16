package com.thathustudio.spage.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.thathustudio.spage.R;
import com.thathustudio.spage.model.User;
import com.thathustudio.spage.utils.ShareReferrentHelper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StartAnimations();
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = ShareReferrentHelper.getTempUser(getApplicationContext());
                if(user == null){
                    Intent mainIntent = new Intent(SplashActivity.this.getApplicationContext(), LoginActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }
                else {
                    Intent mainIntent = new Intent(SplashActivity.this.getApplicationContext(), HomeActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }
            }
        }, 3500);
    }

}
