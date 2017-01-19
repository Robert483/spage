package com.thathustudio.spage.service;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thathustudio.spage.app.CustomApplication;
import com.thathustudio.spage.exception.SpageException;
import com.thathustudio.spage.model.Comment;

import com.thathustudio.spage.model.Subject;
import com.thathustudio.spage.model.Subscription;
import com.thathustudio.spage.model.responses.CommentListResponse;
import com.thathustudio.spage.model.responses.EndPointResponse;

import com.thathustudio.spage.model.Post;
import com.thathustudio.spage.model.Subject;
import com.thathustudio.spage.model.responses.CommentListResponse;
import com.thathustudio.spage.model.responses.CommentResponse;
import com.thathustudio.spage.model.responses.EndPointResponse;
import com.thathustudio.spage.model.responses.PostListResponse;
import com.thathustudio.spage.model.responses.PostResponse;

import com.thathustudio.spage.model.responses.SubjectListResponse;
import com.thathustudio.spage.service.retrofit.AddHeaderInterceptor;
import com.thathustudio.spage.service.retrofit.GsonDateFormatAdapter;
import com.thathustudio.spage.service.retrofit.RetryInterceptor;
import com.thathustudio.spage.service.retrofit.SpageRetrofitService;
import com.thathustudio.spage.service.retrofit.TranslateRetrofitCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class SpageServiceImpl implements SpageService {
    private Context context;
    private SpageRetrofitService defaultService;
    private final HttpLoggingInterceptor defaultLogging;
    private final Gson defaultGson;

    private final int connectTimeOut = 10;
    private final int readTimeOut = 10;
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

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
    public void createComment(@NonNull Comment comment, @NonNull final SpageServiceCallback<EndPointResponse> callback) {
        Call<EndPointResponse> callCreateComment = getService().createComment(comment);
        callback.setCall(callCreateComment);

        callback.onPreExcute();
        callCreateComment.enqueue(new TranslateRetrofitCallback<EndPointResponse>() {
            @Override
            public void onFinish(Call<EndPointResponse> call, EndPointResponse responseObject, SpageException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    callback.onPostExcute(responseObject, null);
                } else {
                    callback.onPostExcute(null, exception);
                }
            }
        });
    }

    @Override
    public void readComment(@NonNull int commentId, @NonNull final SpageServiceCallback<EndPointResponse> callback) {
        Call<EndPointResponse> callCreateComment = getService().readComment(commentId);
        callback.setCall(callCreateComment);

        callback.onPreExcute();
        callCreateComment.enqueue(new TranslateRetrofitCallback<EndPointResponse>() {
            @Override
            public void onFinish(Call<EndPointResponse> call, EndPointResponse responseObject, SpageException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    callback.onPostExcute(responseObject, null);
                } else {
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

                if (exception == null && responseObject != null) {
                    if (responseObject == null) {
                        callback.onPostExcute(new ArrayList<Comment>(), null);
                    } else {
                        callback.onPostExcute(responseObject.getResponse(), null);
                    }
                } else {
                    callback.onPostExcute(null, exception);
                }
            }
        });
    }

    @Override
    public void updateComment(@NonNull int commentId, @NonNull Comment newComment, @NonNull final SpageServiceCallback<EndPointResponse> callback) {
        Call<EndPointResponse> callUpdateComment = getService().updateComment(commentId, newComment);
        callback.setCall(callUpdateComment);

        callback.onPreExcute();
        callUpdateComment.enqueue(new TranslateRetrofitCallback<EndPointResponse>() {
            @Override
            public void onFinish(Call<EndPointResponse> call, EndPointResponse responseObject, SpageException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    callback.onPostExcute(responseObject, null);
                } else {
                    callback.onPostExcute(null, exception);
                }
            }
        });
    }

    @Override
    public void deleteComment(@NonNull int commentId, @NonNull final SpageServiceCallback<EndPointResponse> callback) {
        Call<EndPointResponse> callDeleteComment = getService().deleteComment(commentId);
        callback.setCall(callDeleteComment);

        callback.onPreExcute();
        callDeleteComment.enqueue(new TranslateRetrofitCallback<EndPointResponse>() {
            @Override
            public void onFinish(Call<EndPointResponse> call, EndPointResponse responseObject, SpageException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    callback.onPostExcute(responseObject, null);
                } else {
                    callback.onPostExcute(null, exception);
                }
            }
        });
    }

    @Override
    public void createPost(@NonNull final Post post, @NonNull final SpageServiceCallback<PostResponse> callback) {
        Call<PostResponse> callCreatePost = getService().createPost(post);
        callback.setCall(callCreatePost);

        callback.onPreExcute();
        callCreatePost.enqueue(new TranslateRetrofitCallback<PostResponse>() {
            @Override
            public void onFinish(Call<PostResponse> call, PostResponse responseObject, SpageException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    callback.onPostExcute(responseObject, null);
                } else {
                    callback.onPostExcute(null, exception);
                }
            }
        });
    }

    @Override
    public void readPost(@NonNull int postID, @NonNull final SpageServiceCallback<Post> callback) {
//        Call<PostResponse> callReadPost = getService().readPost(postID);
//        callback.setCall(callReadPost);
//
//        callback.onPreExcute();
//        callReadPost.enqueue(new TranslateRetrofitCallback<PostResponse>() {
//            @Override
//            public void onFinish(Call<PostResponse> call, PostResponse responseObject, SpageException exception) {
//                super.onFinish(call, responseObject, exception);
//
//                if(exception == null && responseObject != null){
//                    callback.onPostExcute(responseObject.getResponse(), null);
//                }
//                else {
//                    callback.onPostExcute(null, exception);
//                }
//            }
//        });
    }

    @Override
    public void readNewFeedOfUser(int userId, @NonNull final SpageServiceCallback<List<Post>> callback) {
        Call<PostListResponse> callReadCommentsOfPost = getService().readNewFeedOfUser(userId);
        callback.setCall(callReadCommentsOfPost);

        callback.onPreExcute();
        callReadCommentsOfPost.enqueue(new TranslateRetrofitCallback<PostListResponse>() {
            @Override
            public void onFinish(Call<PostListResponse> call, PostListResponse responseObject, SpageException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    if (responseObject == null) {
                        callback.onPostExcute(new ArrayList<Post>(), null);
                    } else {
                        callback.onPostExcute(responseObject.getResponse(), null);
                    }
                } else {
                    callback.onPostExcute(null, exception);
                }
            }
        });
    }

    @Override
    public void readPostsOfUser(@NonNull int userId, @NonNull final SpageServiceCallback<List<Post>> callback) {


        Call<PostListResponse> callReadCommentsOfPost = getService().readPostsOfUser(userId);
        callback.setCall(callReadCommentsOfPost);

        callback.onPreExcute();
        callReadCommentsOfPost.enqueue(new TranslateRetrofitCallback<PostListResponse>() {
            @Override
            public void onFinish(Call<PostListResponse> call, PostListResponse responseObject, SpageException exception) {
                super.onFinish(call, responseObject, exception);
                if (exception == null && responseObject != null) {
                    if (responseObject == null) {
                        callback.onPostExcute(new ArrayList<Post>(), null);
                    } else {
                        callback.onPostExcute(responseObject.getResponse(), null);
                    }
                } else {
                    Log.d("Thai", exception.getMessage());
                    callback.onPostExcute(null, exception);
                }
            }
        });
    }

    @Override
    public void readPostOfSubject(@NonNull int subjectId, @NonNull final SpageServiceCallback<List<Post>> callback) {
        Call<PostListResponse> callReadCommentsOfPost = getService().readPostOfSubject(subjectId);
        callback.setCall(callReadCommentsOfPost);

        callback.onPreExcute();
        callReadCommentsOfPost.enqueue(new TranslateRetrofitCallback<PostListResponse>() {
            @Override
            public void onFinish(Call<PostListResponse> call, PostListResponse responseObject, SpageException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    if (responseObject == null) {
                        callback.onPostExcute(new ArrayList<Post>(), null);
                    } else {
                        callback.onPostExcute(responseObject.getResponse(), null);
                    }
                } else {
                    callback.onPostExcute(null, exception);
                }
            }
        });
    }



    @Override
    public void updatePost(@NonNull int postID, @NonNull Post newPost, @NonNull final SpageServiceCallback<PostResponse> callback) {
        Call<PostResponse> callUpdateComment = getService().updatePost(postID, newPost);
        callback.setCall(callUpdateComment);

        callback.onPreExcute();
        callUpdateComment.enqueue(new TranslateRetrofitCallback<PostResponse>() {
            @Override
            public void onFinish(Call<PostResponse> call, PostResponse responseObject, SpageException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    callback.onPostExcute(responseObject, null);
                } else {
                    callback.onPostExcute(null, exception);
                }
            }
        });
    }

    @Override
    public void deletePost(@NonNull int postId, @NonNull final SpageServiceCallback<Post> callback) {
        Call<PostResponse> callDeletePost = getService().deletePost(postId);
        callback.setCall(callDeletePost);

        callback.onPreExcute();
        callDeletePost.enqueue(new TranslateRetrofitCallback<PostResponse>() {
            @Override
            public void onFinish(Call<PostResponse> call, PostResponse responseObject, SpageException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    callback.onPostExcute(null, null);
                } else {
                    callback.onPostExcute(null, exception);
                }
            }
        });
    }

    @Override
    public void readListSubject(@NonNull final SpageServiceCallback<List<Subject>> callback) {
        Call<SubjectListResponse> callReadCommentsOfPost = getService().getSubjectList();
        callback.setCall(callReadCommentsOfPost);

        callback.onPreExcute();
        callReadCommentsOfPost.enqueue(new TranslateRetrofitCallback<SubjectListResponse>() {
            @Override
            public void onFinish(Call<SubjectListResponse> call, SubjectListResponse responseObject, SpageException exception) {
                super.onFinish(call, responseObject, exception);


                if (exception == null && responseObject != null) {
                    if (responseObject.getResponse() == null) {
                        callback.onPostExcute(new Vector<Subject>(), null);
                    } else {
                        callback.onPostExcute(responseObject.getResponse(), null);
                    }
                } else {
                    callback.onPostExcute(null, exception);
                }
            }
        });
    }
    //endregion

    //region Subject
    @Override
    public void getSubjectList(final SpageServiceCallback<List<Subject>> callback) {
        Call<SubjectListResponse> callGetSubjectList = getService().getSubjectList();
        callback.setCall(callGetSubjectList);
        callback.onPreExcute();
        callGetSubjectList.enqueue(new TranslateRetrofitCallback<SubjectListResponse>() {
            @Override
            public void onFinish(Call<SubjectListResponse> call, SubjectListResponse responseObject, SpageException exception) {
                super.onFinish(call, responseObject, exception);
                if (exception == null && responseObject != null) {
                    if (responseObject == null) {
                        callback.onPostExcute(new ArrayList<Subject>(), null);
                    } else {
                        callback.onPostExcute(responseObject.getResponse(), null);
                    }
                } else {
                    callback.onPostExcute(null, exception);
                }
            }
        });
    }

//end region

    //region Subscription
    @Override
    public void createSubscription(Subscription subscription, final SpageServiceCallback<EndPointResponse> callback) {
        Call<EndPointResponse> callCreateSubcription = getService().createSubcription(subscription);
        callback.setCall(callCreateSubcription);
        callback.onPreExcute();
        callCreateSubcription.enqueue(new TranslateRetrofitCallback<EndPointResponse>() {
            @Override
            public void onFinish(Call<EndPointResponse> call, EndPointResponse responseObject, SpageException exception) {
                super.onFinish(call, responseObject, exception);
                if (exception == null && responseObject != null) {
                    if (exception == null && responseObject != null) {
                        callback.onPostExcute(responseObject, null);
                    } else {
                        callback.onPostExcute(null, exception);
                    }
                }
            }
        });
    }

}