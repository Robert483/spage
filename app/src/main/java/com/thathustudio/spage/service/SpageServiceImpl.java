package com.thathustudio.spage.service;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thathustudio.spage.app.CustomApplication;
import com.thathustudio.spage.exception.SpageException;
import com.thathustudio.spage.model.Comment;
import com.thathustudio.spage.model.responses.CommentListResponse;
import com.thathustudio.spage.model.responses.CommentResponse;
import com.thathustudio.spage.service.retrofit.AddHeaderInterceptor;
import com.thathustudio.spage.service.retrofit.GsonDateFormatAdapter;
import com.thathustudio.spage.service.retrofit.RetryInterceptor;
import com.thathustudio.spage.service.retrofit.SpageRetrofitService;
import com.thathustudio.spage.service.retrofit.TranslateRetrofitCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Phung on 16/12/2016.
 */

public class SpageServiceImpl implements SpageService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private Context context;
    private SpageRetrofitService defaultService;
    private final HttpLoggingInterceptor defaultLogging;
    private final Gson defaultGson;

    private final int connectTimeOut = 10;
    private final int readTimeOut = 10;

    //region Initialize
    private SpageServiceImpl() {
        // Don't allow default constructor outside
        defaultLogging = null;
        defaultGson = null;
        defaultService = null;
    }

    public SpageServiceImpl(CustomApplication application) {
        context = application.getApplicationContext();
        defaultLogging = newDefaultLogging();
        defaultGson = newDefaultGson();
        defaultService = null;
    }

    private Gson newDefaultGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new GsonDateFormatAdapter())
                .create();
    }

    private HttpLoggingInterceptor newDefaultLogging() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (CustomApplication.isInDebugMode()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        return logging;
    }

    private SpageRetrofitService getService() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new AddHeaderInterceptor())
                .addInterceptor(defaultLogging)
                .addInterceptor(new RetryInterceptor())
                .readTimeout(readTimeOut, TimeUnit.SECONDS)
                .connectTimeout(connectTimeOut, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SpageRetrofitService.API_END_POINT_FORMAT)
                .addConverterFactory(GsonConverterFactory.create(defaultGson))
                .client(client)
                .build();

        return retrofit.create(SpageRetrofitService.class);
    }
    //endregion

    //region Comment
    @Override
    public void createComment(@NonNull Comment comment, @NonNull final SpageServiceCallback<Comment> callback) {
        Call<CommentResponse> callCreateComment = getService().createComment(comment);
        callback.setCall(callCreateComment);

        callback.onPreExcute();
        callCreateComment.enqueue(new TranslateRetrofitCallback<CommentResponse>() {
            @Override
            public void onFinish(Call<CommentResponse> call, CommentResponse responseObject, SpageException exception) {
                super.onFinish(call, responseObject, exception);

                if(exception == null && responseObject != null){
                    callback.onPostExcute(responseObject.getResponse(), null);
                }
                else {
                    callback.onPostExcute(null, exception);
                }
            }
        });
    }

    @Override
    public void readComment(@NonNull int commentId, @NonNull final SpageServiceCallback<Comment> callback) {
        Call<CommentResponse> callCreateComment = getService().readComment(commentId);
        callback.setCall(callCreateComment);

        callback.onPreExcute();
        callCreateComment.enqueue(new TranslateRetrofitCallback<CommentResponse>() {
            @Override
            public void onFinish(Call<CommentResponse> call, CommentResponse responseObject, SpageException exception) {
                super.onFinish(call, responseObject, exception);

                if(exception == null && responseObject != null){
                    callback.onPostExcute(responseObject.getResponse(), null);
                }
                else {
                    callback.onPostExcute(null, exception);
                }
            }
        });
    }

    @Override
    public void readCommentsOfPost(@NonNull int postId, @NonNull final SpageServiceCallback<List<Comment>> callback) {
        Call<CommentListResponse> callReadCommentsOfPost = getService().readCommentsOfPost(postId);
        callback.setCall(callReadCommentsOfPost);

        callback.onPreExcute();
        callReadCommentsOfPost.enqueue(new TranslateRetrofitCallback<CommentListResponse>() {
            @Override
            public void onFinish(Call<CommentListResponse> call, CommentListResponse responseObject, SpageException exception) {
                super.onFinish(call, responseObject, exception);

                if(exception == null && responseObject != null){
                    if(responseObject.getResponse() == null){
                        callback.onPostExcute(new Vector<Comment>(), null);
                    }
                    else {
                        callback.onPostExcute(responseObject.getResponse(), null);
                    }
                }
                else {
                    callback.onPostExcute(null, exception);
                }
            }
        });
    }

    @Override
    public void updateComment(@NonNull int commentId, @NonNull Comment newComment, @NonNull final SpageServiceCallback<Comment> callback) {
        Call<CommentResponse> callUpdateComment = getService().updateComment(commentId, newComment);
        callback.setCall(callUpdateComment);

        callback.onPreExcute();
        callUpdateComment.enqueue(new TranslateRetrofitCallback<CommentResponse>() {
            @Override
            public void onFinish(Call<CommentResponse> call, CommentResponse responseObject, SpageException exception) {
                super.onFinish(call, responseObject, exception);

                if(exception == null && responseObject != null){
                    callback.onPostExcute(responseObject.getResponse(), null);
                }
                else {
                    callback.onPostExcute(null, exception);
                }
            }
        });
    }

    @Override
    public void deleteComment(@NonNull int commentId, @NonNull final SpageServiceCallback<Comment> callback) {
        Call<CommentResponse> callDeleteComment = getService().deleteComment(commentId);
        callback.setCall(callDeleteComment);

        callback.onPreExcute();
        callDeleteComment.enqueue(new TranslateRetrofitCallback<CommentResponse>() {
            @Override
            public void onFinish(Call<CommentResponse> call, CommentResponse responseObject, SpageException exception) {
                super.onFinish(call, responseObject, exception);

                if(exception == null && responseObject != null){
                    callback.onPostExcute(responseObject.getResponse(), null);
                }
                else {
                    callback.onPostExcute(null, exception);
                }
            }
        });
    }
    //endregion

}
