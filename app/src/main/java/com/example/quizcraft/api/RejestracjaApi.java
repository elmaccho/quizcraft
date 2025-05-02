package com.example.quizcraft.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RejestracjaApi {
    @FormUrlEncoded
    @POST("rejestracja_api.php")
    Call<ResponseBody> registerUser(
            @Field("username") String username,
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("photo") String photo
    );
}