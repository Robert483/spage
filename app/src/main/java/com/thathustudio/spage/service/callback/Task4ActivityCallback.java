package com.thathustudio.spage.service.callback;

import android.content.Context;
import android.widget.Toast;

import com.thathustudio.spage.activities.Task4Activity;
import com.thathustudio.spage.model.responses.EndPointResponse;

import java.lang.ref.WeakReference;

import retrofit2.Callback;

public abstract class Task4ActivityCallback<T extends EndPointResponse, A extends Task4Activity> implements Callback<T> {
    protected final WeakReference<A> weakReferenceTask4Activity;

    public Task4ActivityCallback(A task4Activity) {
        weakReferenceTask4Activity = new WeakReference<>(task4Activity);
    }

    protected void showExceptionToast(Context context, Throwable throwable) {
        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
    }
}
