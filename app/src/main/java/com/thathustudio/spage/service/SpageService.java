package com.thathustudio.spage.service;

import android.support.annotation.NonNull;

import com.thathustudio.spage.model.Comment;

import java.util.List;

/**
 * Created by Phung on 16/12/2016.
 */

public interface SpageService {

    //region Comments
    void createComment(@NonNull final Comment comment, @NonNull final SpageServiceCallback<Comment> callback);
    void readComment(@NonNull final int commentId, @NonNull final SpageServiceCallback<Comment> callback);
    void readCommentsOfPost(@NonNull final int postId, @NonNull final SpageServiceCallback<List<Comment>> callback);
    void updateComment(@NonNull final int commentId, @NonNull final Comment newComment, @NonNull final SpageServiceCallback<Comment> callback);
    void deleteComment(@NonNull final int commentId, @NonNull final SpageServiceCallback<Comment> callback);
    //endregion


}
