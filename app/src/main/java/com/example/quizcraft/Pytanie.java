package com.example.quizcraft;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Pytanie extends AppCompatActivity {

    ProgressBar progressBar;

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

        new CountDownTimer(15000, 150) {
            @Override
            public void onTick(long millisUntilFinished) {
                int progress = (int) (millisUntilFinished * 100 / 15000);
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(0);
            }
        }.start();

    }
}