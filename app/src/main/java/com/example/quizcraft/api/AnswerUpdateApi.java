package com.example.quizcraft.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AnswerUpdateApi {

    @FormUrlEncoded
    @POST("update_answers.php")
    Call<ResponseBody> updateAnswers(
            @Field("user_id") int userId,
            @Field("is_correct") boolean isCorrect
    );

    @FormUrlEncoded
    @POST("update_quizzes_played.php")
    Call<ResponseBody> updateQuizzesPlayed(
            @Field("user_id") int userId
    );
}
