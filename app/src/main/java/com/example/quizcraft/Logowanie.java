package com.example.quizcraft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.quizcraft.api.ApiResponse;
import com.example.quizcraft.api.LogowanieApi;
import com.example.quizcraft.api.RetrofitClient;
import com.google.gson.Gson;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Logowanie extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.logowanie);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicjalizacja elementów UI
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);

        // Listener dla TextView rejestracji
        tvRegister.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Rejestracja.class);
            startActivity(intent);
        });

        // Listener dla przycisku logowania
        btnLogin.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                CustomToast.showToast(Logowanie.this, "Wypełnij wszystkie pola!", R.drawable.logo, Toast.LENGTH_LONG);
                return;
            }

            loginUser(email, password);
        });
    }

    private void loginUser(String email, String password) {
        Log.d("LoginRequest", "Email: " + email + ", Password: " + password);

        LogowanieApi apiService = RetrofitClient.getRetrofitInstance().create(LogowanieApi.class);

        Call<ResponseBody> call = apiService.loginUser(email, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            String responseBody = response.body().string();
                            Log.d("ResponseBody", responseBody);

                            Gson gson = new Gson();
                            ApiResponse apiResponse = gson.fromJson(responseBody, ApiResponse.class);

                            if (apiResponse != null && apiResponse.message != null) {
                                CustomToast.showToast(Logowanie.this, apiResponse.message, R.drawable.logo, Toast.LENGTH_LONG);
                                if (apiResponse.success && apiResponse.user != null) {
                                    SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putInt("userId", apiResponse.user.getId());
                                    editor.putString("username", apiResponse.user.getUsername());
                                    editor.putString("name", apiResponse.user.getName());
                                    editor.putString("email", apiResponse.user.getEmail());
                                    editor.putString("photo", apiResponse.user.getPhoto());
                                    editor.apply();

                                    Intent intent = new Intent(Logowanie.this, Ladowanie.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                CustomToast.showToast(Logowanie.this, "Błąd logowania: Nieznany błąd.", R.drawable.logo, Toast.LENGTH_LONG);
                            }
                        } else {
                            CustomToast.showToast(Logowanie.this, "Błąd odpowiedzi z serwera.", R.drawable.logo, Toast.LENGTH_LONG);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        CustomToast.showToast(Logowanie.this, "Błąd w trakcie przetwarzania odpowiedzi.", R.drawable.logo, Toast.LENGTH_LONG);
                    }
                } else {
                    Log.e("HTTP Error", "Error Code: " + response.code());
                    CustomToast.showToast(Logowanie.this, "Błąd logowania. Kod błędu: " + response.code(), R.drawable.logo, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CustomToast.showToast(Logowanie.this, "Błąd połączenia z serwerem: " + t.getMessage(), R.drawable.logo, Toast.LENGTH_LONG);
                t.printStackTrace();
            }
        });
    }
}