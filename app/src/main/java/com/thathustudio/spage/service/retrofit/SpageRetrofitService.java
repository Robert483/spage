package com.thathustudio.spage.service.retrofit;

import com.thathustudio.spage.model.Comment;
import com.thathustudio.spage.model.responses.CommentListResponse;
import com.thathustudio.spage.model.responses.CommentResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SpageRetrofitService {

    String API_END_POINT_FORMAT = "https://forumflow.herokuapp.com/api/";

    //region Comment
    @PUT("comment")
    Call<CommentResponse> createComment(@Body Comment comment);

    @GET("Comment/{commentId}")
    Call<CommentResponse> readComment(@Path("commentId") int commentId);

    @GET("Post/{postId}/comments")
    Call<CommentListResponse> readCommentsOfPost(@Path("postId") int postId);

    @POST("Comment/{commentId}")
    Call<CommentResponse> updateComment(@Path("commentId") int commentId, Comment newComment);

    @DELETE("Comment/{commentId}")
    Call<CommentResponse> deleteComment(@Path("commentId") int commentId);
    //endregion
}
