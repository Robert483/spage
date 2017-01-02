package com.thathustudio.spage.model.responses;

import com.thathustudio.spage.model.Comment;

import java.util.List;

/**
 * Created by Phung on 16/12/2016.
 */

public class CommentListResponse extends EndPointResponse {

    private List<Comment> response;

    public List<Comment> getResponse() {
        return response;
    }

}
