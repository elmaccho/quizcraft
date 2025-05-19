package com.example.quizcraft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizcraft.api.AnswerUpdateApi;
import com.example.quizcraft.api.QuestionsResponse;
import com.example.quizcraft.api.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameActivity extends AppCompatActivity {

    private int categoryId;
    private String categoryName;
    private QuestionsResponse questionsResponse;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private int currentUserId = -1;  // jebane -1 na start

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // TU jebane pobranie prefs po starcie aktywności
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        currentUserId = prefs.getInt("userId", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Brak zalogowanego użytkownika, pierdol się", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);
        categoryName = getIntent().getStringExtra("CATEGORY_NAME");

        if (categoryId == -1) {
            Toast.makeText(this, "Invalid category, spierdalaj", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchQuestions();
    }

    private void fetchQuestions() {
        RetrofitClient.getApiService().getQuestions(categoryId).enqueue(new Callback<QuestionsResponse>() {
            @Override
            public void onResponse(Call<QuestionsResponse> call, Response<QuestionsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    questionsResponse = response.body();
                    if (questionsResponse.success && questionsResponse.questions != null && !questionsResponse.questions.isEmpty()) {
                        showNextQuestion();
                    } else {
                        Log.e("GameActivity", "API error: success=" + questionsResponse.success + ", message=" + questionsResponse.message);
                        Toast.makeText(GameActivity.this, questionsResponse.message, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Log.e("GameActivity", "Response failed: code=" + response.code() + ", message=" + response.message());
                    Toast.makeText(GameActivity.this, "Failed to fetch questions, kurwa", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<QuestionsResponse> call, Throwable t) {
                Log.e("GameActivity", "Network error: " + t.getMessage(), t);
                Toast.makeText(GameActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < questionsResponse.questions.size()) {
            Intent intent = new Intent(GameActivity.this, Pytanie.class);
            intent.putExtra("QUESTION", questionsResponse.questions.get(currentQuestionIndex));
            intent.putExtra("QUESTION_NUMBER", currentQuestionIndex + 1);
            intent.putExtra("TOTAL_QUESTIONS", questionsResponse.questions.size());
            startActivityForResult(intent, 1);
        } else {
            updateQuizzesPlayed(currentUserId);

            Intent resultIntent = new Intent(GameActivity.this, Podsumowanie.class);
            resultIntent.putExtra("SCORE", score);
            resultIntent.putExtra("TOTAL_QUESTIONS", questionsResponse.questions.size());
            resultIntent.putExtra("CATEGORY_NAME", categoryName);
            startActivity(resultIntent);
            finish();
        }
    }


    private void updateUserAnswers(int userId, boolean isCorrect) {
        RetrofitClient.getRetrofitInstance()
                .create(AnswerUpdateApi.class)
                .updateAnswers(userId, isCorrect)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String respString = response.body().string();
                                Log.d("GameActivity", "Update answers response: " + respString);
                            } catch (Exception e) {
                                Log.e("GameActivity", "Error reading response: " + e.getMessage());
                            }
                        } else {
                            Log.e("GameActivity", "Update answers failed: " + response.code());
                        }
                    }


                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("GameActivity", "Update answers error: " + t.getMessage());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            boolean isCorrect = data.getBooleanExtra("IS_CORRECT", false);

            updateUserAnswers(currentUserId, isCorrect);

            if (isCorrect) {
                score++;
            }
            currentQuestionIndex++;
            showNextQuestion();
        } else {
            finish();
        }
    }

    private void updateQuizzesPlayed(int userId) {
        RetrofitClient.getRetrofitInstance()
                .create(AnswerUpdateApi.class)
                .updateQuizzesPlayed(userId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String respString = response.body().string();
                                Log.d("GameActivity", "Update quizzes played response: " + respString);
                            } catch (Exception e) {
                                Log.e("GameActivity", "Error reading response: " + e.getMessage());
                            }
                        } else {
                            Log.e("GameActivity", "Update quizzes played failed: " + response.code());
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("GameActivity", "Update quizzes played error: " + t.getMessage());
                    }
                });
    }
}
