package com.example.quizcraft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizcraft.api.MatchmakingApi;
import com.example.quizcraft.api.MatchmakingRequest;
import com.example.quizcraft.api.MatchmakingResponse;
import com.example.quizcraft.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ladowanie extends AppCompatActivity {

    TextView tv_czas;
    Button btn_zagrajsam, btn_powrot;
    int categoryId;
    String categoryName;
    int userId;
    int rozgrywkaId = -1;
    CountDownTimer timer;
    boolean isCheckingStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.ladowanie);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tv_czas = findViewById(R.id.tv_czas);
        btn_zagrajsam = findViewById(R.id.btn_zagrajsam);
        btn_powrot = findViewById(R.id.btn_powrot);

        categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);
        categoryName = getIntent().getStringExtra("CATEGORY_NAME");

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        if (userId == -1 || categoryId == -1) {
            Toast.makeText(this, "Błąd: Brak użytkownika lub kategorii", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btn_powrot.setOnClickListener(view -> {
            if (timer != null) {
                timer.cancel();
            }
            Intent intent = new Intent(Ladowanie.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btn_zagrajsam.setOnClickListener(view -> startGame());

        joinMatchmaking();
    }

    private void joinMatchmaking() {
        MatchmakingRequest request = new MatchmakingRequest(userId, categoryId);
        RetrofitClient.getMatchmakingApi().joinMatchmaking(request).enqueue(new Callback<MatchmakingResponse>() {
            @Override
            public void onResponse(Call<MatchmakingResponse> call, Response<MatchmakingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MatchmakingResponse matchmakingResponse = response.body();
                    if (matchmakingResponse.success) {
                        rozgrywkaId = matchmakingResponse.rozgrywka_id;
                        if ("active".equals(matchmakingResponse.status)) {
                            startGame();
                        } else {
                            startWaitingTimer();
                        }
                    } else {
                        Toast.makeText(Ladowanie.this, matchmakingResponse.message, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(Ladowanie.this, "Błąd serwera", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<MatchmakingResponse> call, Throwable t) {
                Log.e("Ladowanie", "Błąd sieci: " + t.getMessage());
                Toast.makeText(Ladowanie.this, "Błąd sieci: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void startWaitingTimer() {
        timer = new CountDownTimer(30000, 1000) {
            int czas = 30;

            @Override
            public void onTick(long l) {
                String formattedTime = String.format("0:%02d", czas);
                tv_czas.setText(formattedTime);
                czas--;

                if (!isCheckingStatus) {
                    isCheckingStatus = true;
                    checkGameStatus();
                }
            }

            @Override
            public void onFinish() {
                tv_czas.setText("Brak przeciwnika!");
                btn_zagrajsam.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void checkGameStatus() {
        MatchmakingRequest request = new MatchmakingRequest(userId, categoryId);
        RetrofitClient.getMatchmakingApi().joinMatchmaking(request).enqueue(new Callback<MatchmakingResponse>() {
            @Override
            public void onResponse(Call<MatchmakingResponse> call, Response<MatchmakingResponse> response) {
                isCheckingStatus = false;
                if (response.isSuccessful() && response.body() != null && response.body().success) {
                    if ("active".equals(response.body().status) && response.body().rozgrywka_id == rozgrywkaId) {
                        timer.cancel();
                        startGame();
                    }
                }
            }

            @Override
            public void onFailure(Call<MatchmakingResponse> call, Throwable t) {
                isCheckingStatus = false;
                Log.e("Ladowanie", "Błąd sprawdzania statusu: " + t.getMessage());
            }
        });
    }

    private void startGame() {
        Intent intent = new Intent(Ladowanie.this, GameActivity.class);
        intent.putExtra("CATEGORY_ID", categoryId);
        intent.putExtra("CATEGORY_NAME", categoryName);
        intent.putExtra("ROZGRYWKA_ID", rozgrywkaId);
        intent.putExtra("IS_1V1", true);
        startActivity(intent);
        finish();
    }
}