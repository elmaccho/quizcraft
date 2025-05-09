package com.example.quizcraft.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserDataApi {
    @FormUrlEncoded
    @POST("get_user_data.php")
    Call<ResponseBody> getUserData(
            @Field("user_id") String userId
    );
}