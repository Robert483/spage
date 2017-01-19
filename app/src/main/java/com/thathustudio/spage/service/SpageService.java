package com.thathustudio.spage.service;

import android.support.annotation.NonNull;

import com.thathustudio.spage.model.Comment;
import com.thathustudio.spage.model.Post;
import com.thathustudio.spage.model.Subject;
import com.thathustudio.spage.model.Subscription;
import com.thathustudio.spage.model.responses.EndPointResponse;
import com.thathustudio.spage.model.responses.PostResponse;

import java.util.List;

public interface SpageService {

    //region Comments
    void createComment(@NonNull final Comment comment, @NonNull final SpageServiceCallback<EndPointResponse> callback);

    void readComment(@NonNull final int commentId, @NonNull final SpageServiceCallback<EndPointResponse> callback);

    void readCommentsOfPost(@NonNull final int postId, @NonNull final SpageServiceCallback<List<Comment>> callback);

    void updateComment(@NonNull final int commentId, @NonNull final Comment newComment, @NonNull final SpageServiceCallback<EndPointResponse> callback);

    void deleteComment(@NonNull final int commentId, @NonNull final SpageServiceCallback<EndPointResponse> callback);
    //endregion

    //region Subjects
    void getSubjectList(SpageServiceCallback<List<Subject>> callback);
    //end region


    //region Subjects
    void createSubscription(Subscription subscription, SpageServiceCallback<EndPointResponse> callback);
    //end region


    //region Post
    void createPost(@NonNull final Post post, @NonNull final SpageServiceCallback<PostResponse> callback);

    void readPost(@NonNull final int postID, @NonNull final SpageServiceCallback<Post> callback);

    void readNewFeedOfUser(@NonNull final int userId, @NonNull final SpageServiceCallback<List<Post>> callback);

    void readPostsOfUser(@NonNull final int userId, @NonNull final SpageServiceCallback<List<Post>> callback);

    void readPostOfSubject(@NonNull final int subjectId, @NonNull final SpageServiceCallback<List<Post>> callback);

    void updatePost(@NonNull final int postID, @NonNull final Post newPost, @NonNull final SpageServiceCallback<PostResponse> callback);


    //    void (@NonNull final int commentId, @NonNull final Comment newComment, @NonNull final SpageServiceCallback<EndPointResponse> callback);
//    void updatePost(@NonNull final int postID, @NonNull final Post newPost, @NonNull final SpageServiceCallback<PostResponse> callback);
    void deletePost(@NonNull final int postId, @NonNull final SpageServiceCallback<Post> callback);
    //endregion


    void readListSubject(@NonNull final SpageServiceCallback<List<Subject>> callback);


//    Call<PostResponse> updatePost(@Path("postId") int postId,Post newPost, @NonNull final SpageServiceCallback<Post> callback);


}
