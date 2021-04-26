package com.thathustudio.spage.model.responses;

import com.thathustudio.spage.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentListResponse extends EndPointResponse {

    private List<Comment> response = new ArrayList<>();

    public List<Comment> getResponse() {
        return response;
    }

}
