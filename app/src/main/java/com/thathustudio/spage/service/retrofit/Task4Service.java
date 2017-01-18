package com.thathustudio.spage.service.retrofit;

import com.thathustudio.spage.model.Exercise;
import com.thathustudio.spage.model.Question;
import com.thathustudio.spage.model.Result;
import com.thathustudio.spage.model.User;
import com.thathustudio.spage.model.responses.LoginResponse;
import com.thathustudio.spage.model.responses.Task4ListResponse;
import com.thathustudio.spage.model.responses.Task4Response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Task4Service {
    String SERVICE_ADDRESS = "https://forumflow.herokuapp.com/";

    @GET("api/Exercise?sort=desc&order=id")
    Call<Task4ListResponse<Exercise>> getExercises();

    @GET("api/exercise/{id}/questions")
    Call<Task4ListResponse<Question>> getQuestions(@Path("id") int exerciseId);

    @POST("api/result")
    Call<Task4Response<Integer>> postResult(@Body Result result);

    @POST("login")
    Call<Task4Response<User>> postLogin(@Header("Content-Type") String token,@Body User user);

    @POST("api/user")
    Call<Task4Response<Integer>> registerUser (@Header("Content-Type") String token, @Body User user);
}