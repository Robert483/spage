package com.thathustudio.spage.model.responses;

import com.thathustudio.spage.model.Comment;
import com.thathustudio.spage.model.Post;

import java.util.List;

/**
 * Created by Phung on 16/12/2016.
 */

public class PostListResponse extends EndPointResponse {

    private List<Post> response;
    public List<Post> getResponse() {
        return response;
    }

}
