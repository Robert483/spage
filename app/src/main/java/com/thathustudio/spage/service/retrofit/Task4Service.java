package com.thathustudio.spage.service.retrofit;

import com.thathustudio.spage.model.responses.ExerciseListResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Task4Service {
    String SERVICE_ADDRESS = "https://forumflow.herokuapp.com/api/";

    @GET("Exercise?sort=desc&order=id")
    Call<ExerciseListResponse> getExercises();
}