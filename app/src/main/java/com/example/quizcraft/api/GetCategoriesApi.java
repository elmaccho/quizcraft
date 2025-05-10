package com.example.quizcraft.api;

import retrofit2.Call;
import retrofit2.http.GET;
import com.example.quizcraft.api.ApiResponse;

public interface GetCategoriesApi {
    @GET("get_categories.php")
    Call<ApiResponse> getCategories();
}