package com.thathustudio.spage.activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.thathustudio.spage.R;
import com.thathustudio.spage.app.CustomApplication;
import com.thathustudio.spage.model.Result;
import com.thathustudio.spage.model.responses.Task4Response;
import com.thathustudio.spage.service.callback.Task4ActivityCallback;
import com.thathustudio.spage.utils.ResultRecyclerViewAdapter;
import com.thathustudio.spage.widgets.ArcProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ResultActivity extends Task4Activity {
    private static final int NO_USER = -1;
    private static final int NO_EXERCISE = -1;
    private static final String SENT = "SENT";
    public static final String EXERCISE_ID = "Exercise ID";
    public static final String USER_ID = "User ID";
    public static final String RESULT = "Result";
    private Result result;
    private boolean sent;

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

    private void showToastAndFinish(String msg) {
        finish();
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int userId = getIntent().getIntExtra(USER_ID, NO_USER);
        if (userId == NO_USER) {
            showToastAndFinish("No user ID");
            return;
        }
        int exerciseId = getIntent().getIntExtra(EXERCISE_ID, NO_EXERCISE);
        if (exerciseId == NO_EXERCISE) {
            showToastAndFinish("No exercise ID");
            return;
        }
        boolean[] results = getIntent().getBooleanArrayExtra(ResultActivity.RESULT);
        if (results == null) {
            showToastAndFinish("No result to display");
            return;
        }

        setContentView(R.layout.activity_result);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        List<Boolean> resultList = new ArrayList<>();
        for (boolean result : results) {
            resultList.add(result);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rclrV_result);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ResultRecyclerViewAdapter(this, resultList));

        int total = resultList.size();
        int progress = Collections.frequency(resultList, true);
        float score = 1f * progress / total;
        result = new Result(userId, exerciseId, (int) (score * 100));

        ArcProgressBar arcProgressBar = (ArcProgressBar) findViewById(R.id.arPrgrBr_result);
        arcProgressBar.setMax(total);
        arcProgressBar.setProgress(progress);
        arcProgressBar.setBottomText(grade(score));

        /*ImageView imageView = (ImageView) findViewById(R.id.imgV_background);
        imageView.setImageBitmap(getScaledBackground());*/
    }

    /*private Bitmap getScaledBackground() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.bg_img, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        float scale = 1;

        if (imageHeight > imageWidth) {
            scale = 1f * imageHeight / metrics.heightPixels;
        } else {
            scale = 1f * imageWidth / metrics.widthPixels;
        }

        options.inJustDecodeBounds = false;
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg_img, options), (int)(1f * imageWidth / scale), (int)(1f * imageHeight / scale), false);
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        if (!sent) {
            CustomApplication customApplication = (CustomApplication) getApplication();
            Call<Task4Response<Integer>> exerciseListResponseCall = customApplication.getTask4Service().postResult(result);
            exerciseListResponseCall.enqueue(new PostResultCallback(this));
            addCall(exerciseListResponseCall);
            sent = true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SENT, sent);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            sent = savedInstanceState.getBoolean(SENT, false);
        }
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

    public static class PostResultCallback extends Task4ActivityCallback<Task4Response<Integer>, ResultActivity> {
        public PostResultCallback(ResultActivity task4Activity) {
            super(task4Activity);
        }

        @Override
        public void onResponse(Call<Task4Response<Integer>> call, Response<Task4Response<Integer>> response) {
            ResultActivity resultActivity = weakReferenceTask4Activity.get();
            if (resultActivity != null) {
                resultActivity.removeCall(call);
            }
        }

        @Override
        public void onFailure(Call<Task4Response<Integer>> call, Throwable t) {
            Task4Activity task4Activity = weakReferenceTask4Activity.get();
            if (task4Activity != null) {
                try {
                    // Handle
                    if (!task4Activity.isReloadable()) {
                        showExceptionToast(task4Activity.getApplicationContext(), t);
                    }
                } catch (Exception ex) {
                    Log.e("SPage", ex.getMessage());
                } finally {
                    task4Activity.removeCall(call);
                }
            }
        }
    }
}
