package com.thathustudio.spage.service.retrofit;


import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddHeaderInterceptor implements Interceptor {

//    private String token;
//
//    public AddHeaderInterceptor(String token) {
//        this.token = token;
//    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("Content-Type", "application/json");
//        builder.addHeader("Accept", "application/json");
//        builder.addHeader("Authorization", "Basic " + token);
        return chain.proceed(builder.build());
    }
}