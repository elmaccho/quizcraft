package com.example.quizcraft;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.quizcraft.api.ApiResponse;
import com.example.quizcraft.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button buttonProfile;
    LinearLayout categoriesContainer;
    private static final int ITEMS_PER_ROW = 3;
    private static final String BASE_URL = Config.BASE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonProfile = findViewById(R.id.button_profile);
        categoriesContainer = findViewById(R.id.categories_container);

        buttonProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Profil.class);
            startActivity(intent);
        });

        fetchCategories();
    }

    private void fetchCategories() {
        RetrofitClient.getApiService().getCategories().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.success && apiResponse.categories != null) {
                        displayCategories(apiResponse.categories);
                    } else {
                        Log.e("MainActivity", "API error: success=" + apiResponse.success + ", message=" + apiResponse.message);
                        Toast.makeText(MainActivity.this, apiResponse.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("MainActivity", "Response failed: code=" + response.code() + ", message=" + response.message());
                    if (response.errorBody() != null) {
                        try {
                            Log.e("MainActivity", "Error body: " + response.errorBody().string());
                        } catch (Exception e) {
                            Log.e("MainActivity", "Error reading error body: " + e.getMessage());
                        }
                    }
                    Toast.makeText(MainActivity.this, "Failed to fetch categories: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("MainActivity", "Network error: " + t.getMessage(), t);
                Toast.makeText(MainActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayCategories(List<Category> categories) {
        categoriesContainer.removeAllViews();

        TextView header = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER_HORIZONTAL;

        header.setLayoutParams(params);
        header.setText("WYBIERZ KATEGORIE");
        header.setTextColor(getResources().getColor(android.R.color.white));
        header.setTextSize(18);
        header.setTypeface(null, Typeface.BOLD);
        header.setGravity(Gravity.CENTER);
        header.setPadding(0, 16, 0, 16);

        categoriesContainer.addView(header);

        List<Category> currentRow = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            currentRow.add(categories.get(i));

            if (currentRow.size() == ITEMS_PER_ROW || i == categories.size() - 1) {
                addCategoryRow(currentRow);
                currentRow = new ArrayList<>();
            }
        }
    }

    private void addCategoryRow(List<Category> categories) {
        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setGravity(android.view.Gravity.CENTER);
        rowLayout.setPadding(0, 0, 0, 16);

        for (Category category : categories) {
            View categoryView = LayoutInflater.from(this)
                    .inflate(R.layout.item_category, rowLayout, false);

            ImageView imageView = categoryView.findViewById(R.id.category_image);
            Button button = categoryView.findViewById(R.id.category_button);

            button.setText(category.getName());

            String imageUrl = BASE_URL + category.getImage_url();
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.logo)
                    .into(imageView);

            button.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("CATEGORY_ID", category.getId());
                intent.putExtra("CATEGORY_NAME", category.getName());
                startActivity(intent);
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            categoryView.setLayoutParams(params);
            rowLayout.addView(categoryView);
        }

        categoriesContainer.addView(rowLayout);
    }
}