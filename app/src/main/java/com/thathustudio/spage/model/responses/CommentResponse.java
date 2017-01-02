package com.thathustudio.spage.model.responses;

import com.thathustudio.spage.model.Comment;

/**
 * Created by Phung on 16/12/2016.
 */

public final class CommentResponse extends EndPointResponse {

    private Comment response;
    public Comment getResponse(){
        return response;
    }
}
