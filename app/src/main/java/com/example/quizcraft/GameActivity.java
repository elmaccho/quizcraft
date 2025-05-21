package com.example.quizcraft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizcraft.api.AnswerUpdateApi;
import com.example.quizcraft.api.MatchmakingApi;
import com.example.quizcraft.api.QuestionsResponse;
import com.example.quizcraft.api.RetrofitClient;
import com.example.quizcraft.api.ScoreRequest;
import com.example.quizcraft.api.ScoreResponse;

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
    private int currentUserId = -1;
    private int rozgrywkaId = -1;
    private boolean is1v1 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        currentUserId = prefs.getInt("userId", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Brak zalogowanego użytkownika", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);
        categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        rozgrywkaId = getIntent().getIntExtra("ROZGRYWKA_ID", -1);
        is1v1 = getIntent().getBooleanExtra("IS_1V1", false);

        if (categoryId == -1) {
            Toast.makeText(this, "Nieprawidłowa kategoria", Toast.LENGTH_SHORT).show();
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
                        Log.e("GameActivity", "Błąd API: success=" + questionsResponse.success + ", message=" + questionsResponse.message);
                        Toast.makeText(GameActivity.this, questionsResponse.message, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Log.e("GameActivity", "Nieudana odpowiedź: code=" + response.code() + ", message=" + response.message());
                    Toast.makeText(GameActivity.this, "Nie udało się pobrać pytań", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<QuestionsResponse> call, Throwable t) {
                Log.e("GameActivity", "Błąd sieci: " + t.getMessage(), t);
                Toast.makeText(GameActivity.this, "Błąd sieci: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
            if (is1v1) {
                submitScore();
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
    }

    private void submitScore() {
        ScoreRequest request = new ScoreRequest(currentUserId, rozgrywkaId, score);
        RetrofitClient.getMatchmakingApi().submitScore(request).enqueue(new Callback<ScoreResponse>() {
            @Override
            public void onResponse(Call<ScoreResponse> call, Response<ScoreResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ScoreResponse scoreResponse = response.body();
                    if (scoreResponse.success) {
                        Intent resultIntent = new Intent(GameActivity.this, Podsumowanie.class);
                        resultIntent.putExtra("SCORE", score);
                        resultIntent.putExtra("TOTAL_QUESTIONS", questionsResponse.questions.size());
                        resultIntent.putExtra("CATEGORY_NAME", categoryName);
                        resultIntent.putExtra("IS_1V1", true);
                        resultIntent.putExtra("WINNER_ID", scoreResponse.winner_id);
                        if (scoreResponse.scores != null && scoreResponse.scores.length == 2) {
                            resultIntent.putExtra("OPPONENT_SCORE", scoreResponse.scores[0].user_id == currentUserId ? scoreResponse.scores[1].score : scoreResponse.scores[0].score);
                        }
                        startActivity(resultIntent);
                        finish();
                    } else {
                        Toast.makeText(GameActivity.this, scoreResponse.message, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(GameActivity.this, "Błąd serwera", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ScoreResponse> call, Throwable t) {
                Log.e("GameActivity", "Błąd sieci: " + t.getMessage());
                Toast.makeText(GameActivity.this, "Błąd sieci: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void updateUserAnswers(int userId, boolean isCorrect) {
        RetrofitClient.getAnswerUpdateApi()
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
        RetrofitClient.getAnswerUpdateApi()
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