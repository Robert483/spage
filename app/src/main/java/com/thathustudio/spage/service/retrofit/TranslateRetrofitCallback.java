package com.thathustudio.spage.service.retrofit;


import com.thathustudio.spage.exception.SpageException;
import com.thathustudio.spage.model.responses.EndPointResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class TranslateRetrofitCallback<T extends EndPointResponse> implements Callback<T> {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        SpageException exception = TranslateRetrofitException.translateServiceException(call, response);
        onFinish(call, response.body(), exception);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        SpageException exception = TranslateRetrofitException.translateRetrofitException(call, t);
        onFinish(call, null, exception);
    }

    public void onFinish(Call<T> call, T responseObject, SpageException exception) {
        if (exception == null) {
            LOG.debug("Response is OK. Start parsing json data");
        }
        // TODO: override this method in runtime sub class
    }
}
