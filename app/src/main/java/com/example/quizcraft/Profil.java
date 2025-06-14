package com.example.quizcraft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizcraft.api.UserDataApi;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profil extends AppCompatActivity {

    Button buttonHome;
    private TextView tvNick, tvData, tvRozQuizy1, tvSku1;
    private ImageView ivProfil, cameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);
        // Inicjalizacja widoków
        tvNick = findViewById(R.id.tv_nick);
        tvData = findViewById(R.id.tv_data);
        tvRozQuizy1 = findViewById(R.id.tv_rozquizy1);
        tvSku1 = findViewById(R.id.tv_sku1);
        ivProfil = findViewById(R.id.iv_profil);
        buttonHome = findViewById(R.id.button_home);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "Brak ID użytkownika. Zaloguj się ponownie.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        fetchUserData(userId);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profil.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fetchUserData(int userId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserDataApi userDataApi = retrofit.create(UserDataApi.class);
        Call<ResponseBody> call = userDataApi.getUserData(String.valueOf(userId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonString = response.body().string();
                        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

                        if (jsonObject.has("error")) {
                            Toast.makeText(Profil.this, jsonObject.get("error").getAsString(), Toast.LENGTH_LONG).show();
                            return;
                        }

                        String username = jsonObject.get("username").getAsString();
                        String createdAt = jsonObject.has("created_at") && !jsonObject.get("created_at").isJsonNull() ? jsonObject.get("created_at").getAsString() : "";
                        int answers = jsonObject.get("answers").getAsInt();
                        int correctAnswers = jsonObject.get("correct_answers").getAsInt();
                        int allgames = jsonObject.get("quizzes_played").getAsInt();

                        String formattedDate = "";
                        if (!createdAt.isEmpty()) {
                            try {
                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("pl", "PL"));
                                Date date = inputFormat.parse(createdAt);
                                formattedDate = "Dołączono " + outputFormat.format(date);
                            } catch (Exception e) {
                                formattedDate = "Dołączono: brak danych";
                            }
                        }

                        String effectiveness = answers > 0 ? String.format(Locale.getDefault(), "%.0f%%", (correctAnswers * 100.0 / answers)) : "0%";

                        tvNick.setText("@" + username);
                        tvData.setText(formattedDate);
                        tvRozQuizy1.setText(String.valueOf(allgames));
                        tvSku1.setText(effectiveness);

                    } catch (IOException e) {
                        Toast.makeText(Profil.this, "Błąd parsowania danych", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Profil.this, "Błąd odpowiedzi serwera", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Profil.this, "Błąd sieci: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}