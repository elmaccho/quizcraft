package com.example.quizcraft;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.quizcraft.api.QuestionsResponse;


public class Pytanie extends AppCompatActivity {

    ProgressBar progressBar;
    TextView tvQuestion, tvQuestionNumber;
    Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4;
    ImageView ivQuestionImage;
    QuestionsResponse.Question question;
    boolean answerSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pytanie);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressBar = findViewById(R.id.progressBar);
        tvQuestion = findViewById(R.id.tv_pytanie);
        tvQuestionNumber = findViewById(R.id.tv_runda);
        btnAnswer1 = findViewById(R.id.button_odp1);
        btnAnswer2 = findViewById(R.id.button_odp2);
        btnAnswer3 = findViewById(R.id.button_odp3);
        btnAnswer4 = findViewById(R.id.button_odp4);
        ivQuestionImage = findViewById(R.id.avatar);

        // Get question data
        question = (QuestionsResponse.Question) getIntent().getSerializableExtra("QUESTION");
        int questionNumber = getIntent().getIntExtra("QUESTION_NUMBER", 1);
        int totalQuestions = getIntent().getIntExtra("TOTAL_QUESTIONS", 5);

        if (question == null) {
            Toast.makeText(this, "Error loading question", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        tvQuestion.setText(question.tresc);
        tvQuestionNumber.setText("Pytanie " + questionNumber + "/" + totalQuestions);

        if (question.photo != null && !question.photo.isEmpty()) {
            Glide.with(this)
                    .load(Config.BASE_URL + question.photo)
                    .placeholder(R.drawable.logo)
                    .into(ivQuestionImage);
        } else {
            ivQuestionImage.setVisibility(View.GONE);
        }

        if (question.odpowiedzi.size() >= 4) {
            btnAnswer1.setText(question.odpowiedzi.get(0).nazwa);
            btnAnswer2.setText(question.odpowiedzi.get(1).nazwa);
            btnAnswer3.setText(question.odpowiedzi.get(2).nazwa);
            btnAnswer4.setText(question.odpowiedzi.get(3).nazwa);

            btnAnswer1.setOnClickListener(v -> selectAnswer(0));
            btnAnswer2.setOnClickListener(v -> selectAnswer(1));
            btnAnswer3.setOnClickListener(v -> selectAnswer(2));
            btnAnswer4.setOnClickListener(v -> selectAnswer(3));
        }

        new CountDownTimer(15000, 150) {
            @Override
            public void onTick(long millisUntilFinished) {
                int progress = (int) (millisUntilFinished * 100 / 15000);
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                if (!answerSelected) {
                    progressBar.setProgress(0);
                    returnResult(false);
                }
            }
        }.start();
    }

    private void selectAnswer(int index) {
        if (answerSelected) return;
        answerSelected = true;

        boolean isCorrect = question.odpowiedzi.get(index).poprawna == 1;
        Button selectedButton = findViewById(
                new int[]{R.id.button_odp1, R.id.button_odp2, R.id.button_odp3, R.id.button_odp4}[index]
        );
        selectedButton.setBackgroundColor(
                getResources().getColor(isCorrect ? android.R.color.holo_green_light : android.R.color.holo_red_light)
        );

        // Disable all buttons
        btnAnswer1.setEnabled(false);
        btnAnswer2.setEnabled(false);
        btnAnswer3.setEnabled(false);
        btnAnswer4.setEnabled(false);

        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                returnResult(isCorrect);
            }
        }.start();
    }

    private void returnResult(boolean isCorrect) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("IS_CORRECT", isCorrect);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}