package com.thathustudio.spage.service.retrofit;

import com.thathustudio.spage.model.Exercise;
import com.thathustudio.spage.model.Post;
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
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Task4Service {
    String SERVICE_ADDRESS = "https://forumflow.herokuapp.com/";

    @GET("api/Exercise?sort=desc&order=id")
    Call<Task4ListResponse<Exercise>> getExercises();

    @GET("api/exercise/{id}/questions")
    Call<Task4ListResponse<Question>> getQuestions(@Path("id") int exerciseId);

    @POST("api/result")
    Call<Task4Response<Integer>> postResult(@Body Result result);

    // for generate content purpose
    @PUT("api/exercise/{id}")
    Call<Task4Response<Integer>> putExercise(@Path("id") int exerciseId, @Body Exercise exercise);

    @GET("api/challenge")
    Call<Task4ListResponse<Question>> getDuelQuestions(@Query("limit") int limit);

    @POST("login")
    Call<Task4Response<User>> postLogin(@Header("Content-Type") String token,@Body User user);

    @POST("api/user")
    Call<Task4Response<Integer>> registerUser (@Header("Content-Type") String token, @Body User user);

    @GET("api/user/{id}/subscriptions")
    Call<Task4ListResponse<Post>> GetSubject(@Path("id") int userID);

    @PUT("api/user/{id}")
    Call<Task4Response<Integer>> editProflie(@Path("id") int userID,@Body User newUser);
}