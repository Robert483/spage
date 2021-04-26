package com.thathustudio.spage.service;

import retrofit2.Call;

public interface SpageServiceCallback<Result> {
    void setCall(Call call);

    void cancel();

    void onPreExcute();

    void onPostExcute(Result result, Throwable throwable);
}
