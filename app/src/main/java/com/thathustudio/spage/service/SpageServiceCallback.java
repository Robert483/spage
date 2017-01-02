package com.thathustudio.spage.service;

import retrofit2.Call;

/**
 * Created by Phung on 16/12/2016.
 */

public interface SpageServiceCallback<Result extends Object> {
    void setCall(Call call);
    void cancel();
    void onPreExcute();
    void onPostExcute(Result result, Throwable throwable);
}
