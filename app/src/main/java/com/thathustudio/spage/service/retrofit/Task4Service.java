package com.thathustudio.spage.service.retrofit;

import com.thathustudio.spage.model.Exercise;
import com.thathustudio.spage.model.Question;
import com.thathustudio.spage.model.Result;
import com.thathustudio.spage.model.responses.Task4ListResponse;
import com.thathustudio.spage.model.responses.Task4Response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Task4Service {
    String SERVICE_ADDRESS = "https://forumflow.herokuapp.com/api/";

    @GET("Exercise?sort=desc&order=id")
    Call<Task4ListResponse<Exercise>> getExercises();

    @GET("exercise/{id}/questions")
    Call<Task4ListResponse<Question>> getQuestions(@Path("id") int exerciseId);

    @POST("result")
    Call<Task4Response<Integer>> postResult(@Body Result result);

    @PUT("exercise/{id}")
    Call<Task4Response<Integer>> putExercise(@Path("id") int exerciseId, @Body Exercise exercise);
}