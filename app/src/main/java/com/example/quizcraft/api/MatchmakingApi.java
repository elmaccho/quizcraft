package com.example.quizcraft.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MatchmakingApi {
    @POST("matchmaking.php")
    Call<MatchmakingResponse> joinMatchmaking(@Body MatchmakingRequest request);

    @POST("submit_score.php")
    Call<ScoreResponse> submitScore(@Body ScoreRequest request);
}