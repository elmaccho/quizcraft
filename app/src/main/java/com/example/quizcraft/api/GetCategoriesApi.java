package com.example.quizcraft.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetCategoriesApi {
    @GET("get_categories.php")
    Call<ApiResponse> getCategories();

    @GET("get_questions.php")
    Call<QuestionsResponse> getQuestions(@Query("category_id") int categoryId);
}