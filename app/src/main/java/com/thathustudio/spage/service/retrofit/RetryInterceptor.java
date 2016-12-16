package com.thathustudio.spage.service.retrofit;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class RetryInterceptor implements Interceptor {

//    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // try the request
        Response response = chain.proceed(request);

        int tryCount = 0;
        int maxLimit = 3; //Set your max limit here

        while (!response.isSuccessful() && tryCount < maxLimit) {

//            LOG.debug("Request failed - Retry " + tryCount + 1);

            tryCount++;

            // retry the request
            response = chain.proceed(request);
        }

        // otherwise just pass the original EndPointResponse on
        return response;
    }
}
