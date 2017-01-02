package com.thathustudio.spage.service;

import android.support.annotation.NonNull;

import com.thathustudio.spage.model.Comment;

import java.util.List;

public interface SpageService {

    //region Comments
    void createComment(@NonNull final Comment comment, @NonNull final SpageServiceCallback<Comment> callback);

    void readComment(final int commentId, @NonNull final SpageServiceCallback<Comment> callback);

    void readCommentsOfPost(final int postId, @NonNull final SpageServiceCallback<List<Comment>> callback);

    void updateComment(final int commentId, @NonNull final Comment newComment, @NonNull final SpageServiceCallback<Comment> callback);

    void deleteComment(final int commentId, @NonNull final SpageServiceCallback<Comment> callback);
    //endregion


}
