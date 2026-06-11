package com.example.afinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    // UI Elements
    private TextView tvFitData, tvGoogleFitLabel, tvHeaderTitle, tvBMIScore, tvBMIStatus, tvCalorieDesc, tvSleepDuration, tvHydrationAmount, tvCaloriePercentage;
    private ProgressBar progressCalories, progressHydration;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inisialisasi UI Dasbor
        tvHeaderTitle = view.findViewById(R.id.tvHeaderTitle);
        tvBMIScore = view.findViewById(R.id.tvBMIScore);
        tvBMIStatus = view.findViewById(R.id.tvBMIStatus);
        tvCalorieDesc = view.findViewById(R.id.tvCalorieDesc);
        progressCalories = view.findViewById(R.id.progressCalories);
        tvSleepDuration = view.findViewById(R.id.tvSleepDuration);
        tvHydrationAmount = view.findViewById(R.id.tvHydrationAmount);
        progressHydration = view.findViewById(R.id.progressHydration);

        // Inisialisasi Teks Persentase Baru
        tvCaloriePercentage = view.findViewById(R.id.tvCaloriePercentage);

        // Navigasi Pengaturan Profil & Misi
        ShapeableImageView ivProfile = view.findViewById(R.id.ivProfile);
        ivProfile.setOnClickListener(v -> startActivity(new Intent(getActivity(), SettingsActivity.class)));

        MaterialCardView cardMissions = view.findViewById(R.id.cardMissions);
        cardMissions.setOnClickListener(v -> startActivity(new Intent(getActivity(), MissionActivity.class)));

        // Navigasi Hidrasi & Tidur
        MaterialCardView cardHydration = view.findViewById(R.id.cardHydration);
        cardHydration.setOnClickListener(v -> startActivity(new Intent(getActivity(), HydrationActivity.class)));

        MaterialCardView cardSleep = view.findViewById(R.id.cardSleep);
        cardSleep.setOnClickListener(v -> startActivity(new Intent(getActivity(), SleepActivity.class)));

        // Label API Motivasi
        tvFitData = view.findViewById(R.id.tvFitData);
        tvGoogleFitLabel = view.findViewById(R.id.tvGoogleFitLabel);
        tvGoogleFitLabel.setText("🔄 Refresh Motivasi");

        tvGoogleFitLabel.setOnClickListener(v -> fetchQuoteApi());
        fetchQuoteApi();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDashboardData();
    }

    private void updateDashboardData() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        String name = prefs.getString("userName", "Pengguna");
        float bmi = prefs.getFloat("userBMI", 0f);
        String sleepDuration = prefs.getString("userSleepDuration", "0h 0m");

        // Data Kalori
        int caloriesTarget = prefs.getInt("userCalories", 2000);
        int caloriesConsumed = prefs.getInt("userCaloriesConsumed", 0);

        // Data Hidrasi
        int waterTarget = prefs.getInt("userWaterTarget", 2000);
        int waterConsumed = prefs.getInt("userWaterConsumed", 0);

        if (tvHeaderTitle != null) tvHeaderTitle.setText("Halo, " + name + "!");
        if (tvSleepDuration != null) tvSleepDuration.setText(sleepDuration);

        // Update BMI
        if (tvBMIScore != null && tvBMIStatus != null) {
            if (bmi > 0) {
                tvBMIScore.setText(String.format("%.1f", bmi));
                if (bmi < 18.5) tvBMIStatus.setText("Kurang Berat");
                else if (bmi < 24.9) tvBMIStatus.setText("Normal");
                else tvBMIStatus.setText("Kelebihan Berat");
            }
        }

        // Hitung dan Tampilkan Progress Kalori
        if (tvCalorieDesc != null && progressCalories != null) {
            int sisaKalori = caloriesTarget - caloriesConsumed;
            if (sisaKalori < 0) sisaKalori = 0;

            tvCalorieDesc.setText("Sisa " + sisaKalori + " kcal dari total target " + caloriesTarget + " kcal harian.");

            // Menghitung angka persentase sesungguhnya
            int caloriePercentage = (caloriesTarget > 0) ? (int) (((float) caloriesConsumed / caloriesTarget) * 100) : 0;

            progressCalories.setProgress(Math.min(caloriePercentage, 100));

            // Setel teks di tengah lingkaran
            if (tvCaloriePercentage != null) {
                tvCaloriePercentage.setText(caloriePercentage + "%");
            }
        }

        // Progress Bar Hidrasi
        if (tvHydrationAmount != null && progressHydration != null) {
            int actualWaterConsumed = waterConsumed;

            if (waterConsumed > 0 && waterConsumed <= 15) {
                actualWaterConsumed = waterConsumed * 1000;
            }

            float waterInLiter = (float) actualWaterConsumed / 1000;
            tvHydrationAmount.setText(String.format("%.1f L", waterInLiter));

            int waterPercentage = (waterTarget > 0) ? (int) (((float) actualWaterConsumed / waterTarget) * 100) : 0;
            progressHydration.setProgress(Math.min(waterPercentage, 100));
        }
    }

    private void fetchQuoteApi() {
        tvFitData.setText("Menarik kata motivasi hari ini...");
        tvFitData.setTextColor(getResources().getColor(android.R.color.white));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            FitApiService api = RetrofitClient.getClient().create(FitApiService.class);
            api.getRandomQuote().enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            String quote = response.body().get("quote").getAsString();
                            String author = response.body().get("author").getAsString();
                            tvFitData.setText("\"" + quote + "\"\n\n- " + author);
                            tvFitData.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                        } catch (Exception e) {
                            tvFitData.setText("Format data API berubah.");
                        }
                    } else {
                        tvFitData.setText("Gagal mengambil data dari server.");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    tvFitData.setText("Koneksi internet terputus. Klik '🔄 Refresh Motivasi' untuk mencoba lagi.");
                    tvFitData.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }
            });
        });
    }
}