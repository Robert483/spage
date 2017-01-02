package com.thathustudio.spage.activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.thathustudio.spage.R;
import com.thathustudio.spage.utils.ResultRecyclerViewAdapter;
import com.thathustudio.spage.widgets.ArcProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    public static final String RESULT = "Result";

    private String grade(float score) {
        if (score >= 0.925) {
            return "A+";
        } else if (score >= 0.825) {
            return "A";
        } else if (score >= 0.8) {
            return "A-";
        } else if (score >= 0.725) {
            return "B+";
        } else if (score >= 0.625) {
            return "B";
        } else if (score >= 0.6) {
            return "B-";
        } else if (score >= 0.525) {
            return "C+";
        } else if (score >= 0.425) {
            return "C";
        } else if (score >= 0.4) {
            return "C-";
        } else if (score >= 0.325) {
            return "D+";
        } else if (score >= 0.225) {
            return "D";
        } else if (score >= 0.2) {
            return "D-";
        } else {
            return "F";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        boolean[] results = getIntent().getBooleanArrayExtra(ResultActivity.RESULT);
        List<Boolean> resultList = new ArrayList<>();
        for (boolean result : results) {
            resultList.add(result);
        }

        ArcProgressBar arcProgressBar = (ArcProgressBar) findViewById(R.id.arPrgrBr_result);
        arcProgressBar.setMax(resultList.size());
        arcProgressBar.setProgress(Collections.frequency(resultList, true));
        arcProgressBar.setBottomText(grade(1f * arcProgressBar.getProgress() / arcProgressBar.getMax()));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rclrV_result);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ResultRecyclerViewAdapter(this, resultList));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
