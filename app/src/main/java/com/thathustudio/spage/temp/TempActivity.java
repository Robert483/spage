package com.thathustudio.spage.temp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.thathustudio.spage.R;
import com.thathustudio.spage.activities.LoginActivity;
import com.thathustudio.spage.activities.SplashActivity;
import com.thathustudio.spage.fragments.RoomsFragment;

public class TempActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);

        // Redirect or show fragment

        // - Redirect
        /*Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ResultActivity.EXERCISE_ID, 1);
        bundle.putInt(ResultActivity.USER_ID, 1);
        Random random = new Random(System.currentTimeMillis());
        boolean[] results = new boolean[40];
        for (int i = 0, len = results.length; i < len; i++) {
            results[i] = random.nextBoolean();
        }
        bundle.putBooleanArray(ResultActivity.RESULT, results);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();*/

        // - Show fragment
        //getSupportFragmentManager().beginTransaction().replace(R.id.activity_temp, ExercisesFragment.newInstance(1)).commitAllowingStateLoss();
        //getSupportFragmentManager().beginTransaction().replace(R.id.activity_temp, RoomsFragment.newInstance("Windows")).commitAllowingStateLoss();

        // -------------------------
    }
}
