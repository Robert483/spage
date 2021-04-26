package com.thathustudio.spage.model.responses;

import com.thathustudio.spage.model.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phung on 18/01/2017.
 */

public class SubjectListResponse extends EndPointResponse {
    private List<Subject> response = new ArrayList<>();

    public List<Subject> getResponse() {
        return response;
    }
}
