package com.example.quizcraft.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LogowanieApi {
    @FormUrlEncoded
    @POST("logowanie_api.php")
    Call<ResponseBody> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );
}