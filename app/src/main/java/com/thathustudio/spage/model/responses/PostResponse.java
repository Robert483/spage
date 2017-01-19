package com.thathustudio.spage.model.responses;

import com.thathustudio.spage.model.Comment;
import com.thathustudio.spage.model.Post;

/**
 * Created by Phung on 16/12/2016.
 */

public final class PostResponse extends EndPointResponse {

    int response;

    int code;
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getId() {
        return response;
    }

    public void setId(int id) {
        this.response = id;
    }
    //    private Post response;
//    public Post getResponse(){
//        return response;
//    }
}
