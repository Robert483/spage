package com.thathustudio.spage.service.retrofit;

import android.support.annotation.NonNull;

import com.thathustudio.spage.model.Comment;
import com.thathustudio.spage.model.Subject;
import com.thathustudio.spage.model.Subscription;
import com.thathustudio.spage.model.responses.CommentListResponse;
import com.thathustudio.spage.model.responses.EndPointResponse;
import com.thathustudio.spage.model.responses.EndPointResponse;
import com.thathustudio.spage.model.responses.PostListResponse;
import com.thathustudio.spage.model.responses.PostResponse;
import com.thathustudio.spage.model.responses.SubjectListResponse;
import com.thathustudio.spage.service.SpageServiceCallback;
import com.thathustudio.spage.model.Post;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SpageRetrofitService {

    String API_END_POINT_FORMAT = "https://forumflow.herokuapp.com/api/";

    //region Comment
    @POST("comment")
    Call<EndPointResponse> createComment(@Body Comment comment);

    @GET("Comment/{commentId}")
    Call<EndPointResponse> readComment(@Path("commentId") int commentId);

    @GET("post/{postId}/comments")
    Call<CommentListResponse> readCommentsOfPost(@Path("postId") int postId);

    @PUT("comment/{commentId}")
    Call<EndPointResponse> updateComment(@Path("commentId") int commentId, @Body Comment newComment);

    @DELETE("Comment/{commentId}")
    Call<EndPointResponse> deleteComment(@Path("commentId") int commentId);
    //endregion




    //region Post
    @POST("post")
    Call<PostResponse> createPost(@Body Post post);

//
////    /api/user/id/newsfeed
//    @GET("user/{userId}/newsfeed")
//    Call<PostListResponse> readNewFeedOfUser(@Path("userId") int userId);


    @GET("user/{userId}/newsfeed")
    Call<PostListResponse> readNewFeedOfUser(@Path("userId") int userId);


    @GET("user/{userId}/posts")
    Call<PostListResponse> readPostsOfUser(@Path("userId") int userId);


//    /api/subject/id/posts
    @GET("subject/{subjectId}/posts")
    Call<PostListResponse> readPostOfSubject(@Path("subjectId") int subjectId);


    @PUT("post/{postId}")
    Call<PostResponse> updatePost(@Path("postId") int postId, @Body Post newPost);





    @GET("Post")
    Call<PostListResponse>  readListPost();




    @DELETE("Post/{postId}")
    Call<PostResponse> deletePost(@Path("postId") int postId);
    //endregion





    //region Subject
    @GET("Subject")
    Call<SubjectListResponse> getSubjectList();
    //end region

    //region Subscription
    @POST("subscription")
    Call<EndPointResponse> createSubcription(@Body Subscription subcription);
    //end region
}
