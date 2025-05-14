package com.example.quizcraft;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizcraft.api.QuestionsResponse;
import com.example.quizcraft.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameActivity extends AppCompatActivity {

    private int categoryId;
    private String categoryName;
    private QuestionsResponse questionsResponse;
    private int currentQuestionIndex = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Get category ID and name from intent
        categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);
        categoryName = getIntent().getStringExtra("CATEGORY_NAME");

        if (categoryId == -1) {
            Toast.makeText(this, "Invalid category", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(GameActivity.this, "Failed to fetch questions", Toast.LENGTH_SHORT).show();
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
            // Game finished, show results
            Intent resultIntent = new Intent(GameActivity.this, Podsumowanie.class);
            resultIntent.putExtra("SCORE", score);
            resultIntent.putExtra("TOTAL_QUESTIONS", questionsResponse.questions.size());
            resultIntent.putExtra("CATEGORY_NAME", categoryName); // Przekaż kategorię do podsumowania
            startActivity(resultIntent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            boolean isCorrect = data.getBooleanExtra("IS_CORRECT", false);
            if (isCorrect) {
                score++;
            }
            currentQuestionIndex++;
            showNextQuestion();
        } else {
            finish();
        }
    }
}
