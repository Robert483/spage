package com.thathustudio.spage.service.callback;

import android.os.AsyncTask;
import android.widget.Toast;
import com.thathustudio.spage.activities.SpageActivity;
import com.thathustudio.spage.service.SpageServiceCallback;

import java.lang.ref.WeakReference;

import retrofit2.Call;


public class ForegroundTaskDelegate<Result> implements SpageServiceCallback<Result> {

    protected final WeakReference<SpageActivity> activityWeakReference;
    private Call call;

    private ForegroundTaskDelegate() {
        // Don't allow default constructor outside
        activityWeakReference = new WeakReference<>(null);
    }

    public ForegroundTaskDelegate(SpageActivity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }

    @Override
    public void setCall(Call call) {
        cancelCall();
        this.call = call;
    }

    @Override
    public void cancel() {
        cancelCall();
    }

    @Override
    public void onPreExcute() {
        showProgress();
    }

    @Override
    public void onPostExcute(Result result, Throwable throwable) {
        dismissProgress();

        if (throwable != null) {
            if (shouldHandleResultForActivity()) {
                SpageActivity activity = activityWeakReference.get();
                Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void cancelCall() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }

    protected boolean shouldHandleResultForActivity() {
        SpageActivity activity = activityWeakReference.get();
        if (activity != null && !activity.isDestroyed() && !activity.isFinishing())
            return true;
        return false;
    }

    protected void showProgress() {
        if (shouldHandleResultForActivity()) {
            SpageActivity activity = activityWeakReference.get();
            activity.showProgressDialog();
        }
    }

    protected void dismissProgress() {
        if (shouldHandleResultForActivity()) {
            SpageActivity activity = activityWeakReference.get();
            activity.dismissProgressDialog();
        }
    }
}
