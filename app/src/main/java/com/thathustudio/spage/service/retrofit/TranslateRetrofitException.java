package com.thathustudio.spage.service.retrofit;

import android.util.Log;


import com.google.gson.Gson;
import com.thathustudio.spage.exception.NetworkException;
import com.thathustudio.spage.exception.ServiceException;
import com.thathustudio.spage.exception.SpageException;
import com.thathustudio.spage.model.responses.EndPointResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Created by btloc on 12/2/16.
 */

public class TranslateRetrofitException {

    private static final Logger LOG = LoggerFactory.getLogger(TranslateRetrofitException.class);

    public static <T extends EndPointResponse> SpageException translateRetrofitException(Call<T> call, Throwable throwable) {
        if (call.isCanceled()) {
            return null;
        }

        LOG.error(throwable.getMessage());
        LOG.trace(Log.getStackTraceString(throwable));

        if (throwable instanceof IOException) {
            NetworkException exception = new NetworkException("", throwable);
            return exception;
        }
        else {
            return new SpageException("Undefined Exception", throwable);
        }
    }

    public static <T extends EndPointResponse> ServiceException translateServiceException(Call<T> call,
                                                                                          Response<T> response) {
        if (response.body() == null) {
            // This case should not happen. You must have something wrong!
            String msg = "There is something wrong with request. Please check your request again";

            try {
                EndPointResponse endPointResponse = new Gson().fromJson(response.errorBody().string(), EndPointResponse.class);
                return new ServiceException(endPointResponse.getError().getCode(),
                        endPointResponse.getError().getMessage(),
                        null);
            }
            catch (Exception e) {
                LOG.error(e.getMessage());
                LOG.trace(Log.getStackTraceString(e));

                return new ServiceException(response.code(), msg, null);
            }
        }
        else {
            return null;
        }
    }

}
