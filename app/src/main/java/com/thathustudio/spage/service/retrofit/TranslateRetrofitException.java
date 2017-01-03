package com.thathustudio.spage.service.retrofit;

import android.util.Log;

import com.google.gson.Gson;
import com.thathustudio.spage.exception.NetworkException;
import com.thathustudio.spage.exception.ServiceException;
import com.thathustudio.spage.exception.SpageException;
import com.thathustudio.spage.model.responses.EndPointResponse;
import com.thathustudio.spage.model.responses.ErrorResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class TranslateRetrofitException {

    private static final Logger LOG = LoggerFactory.getLogger(TranslateRetrofitException.class);

    public static <T extends EndPointResponse> SpageException translateRetrofitException(Call<T> call, Throwable throwable) {
        if (call.isCanceled()) {
            return null;
        }

        LOG.error(throwable.getMessage());
        LOG.trace(Log.getStackTraceString(throwable));

        if (throwable instanceof IOException) {
            return new NetworkException(throwable.getMessage(), throwable);
        } else {
            return new SpageException("Undefined Exception", throwable);
        }
    }

    public static <T extends EndPointResponse> ServiceException translateServiceException(Call<T> call,
                                                                                          Response<T> response) {
        T body = response.body();
        if (body == null) {
            // This case should not happen. You must have something wrong!
            try {
                ErrorResult errorResult = new Gson().fromJson(response.errorBody().string(), ErrorResult.class);
                return new ServiceException(errorResult.getCode(),
                        errorResult.getResponse(),
                        null);
            } catch (Exception e) {
                LOG.error(e.getMessage());
                LOG.trace(Log.getStackTraceString(e));

                return new ServiceException(response.code(),
                        "There is something wrong with request. Please check your request again.",
                        null);
            }
        } else if (body.getCode() != 200) {
            return new ServiceException(body.getCode(),
                    "Server return mismatch code",
                    null);
        } else {
            return null;
        }
    }

}
