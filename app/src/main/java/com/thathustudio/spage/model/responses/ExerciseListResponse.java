package com.thathustudio.spage.model.responses;

import com.thathustudio.spage.model.Exercise;

import java.util.List;

public class ExerciseListResponse extends EndPointResponse {
    private List<Exercise> response;

    public List<Exercise> getResponse() {
        return response;
    }
}
