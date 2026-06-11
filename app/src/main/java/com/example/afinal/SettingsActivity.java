package com.example.afinal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputEditText;

public class SettingsActivity extends AppCompatActivity {

    private TextInputEditText etName, etAge, etWeight, etHeight, etCalories, etCaloriesConsumed;
    private MaterialButton btnSave;
    private MaterialSwitch switchTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        etCalories = findViewById(R.id.etCalories);

        // Inisialisasi Input Baru
        etCaloriesConsumed = findViewById(R.id.etCaloriesConsumed);

        btnSave = findViewById(R.id.btnSaveSettings);
        switchTheme = findViewById(R.id.switchTheme);

        // Muat data lama
        loadExistingData();

        // Logika Sakelar Tema (Jangan dihapus agar poin spesifikasi aman)
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("isDarkMode", true);
        if (switchTheme != null) {
            switchTheme.setChecked(isDarkMode);
            switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isDarkMode", isChecked);
                editor.apply();

                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            });
        }

        btnSave.setOnClickListener(v -> saveData());
    }

    private void loadExistingData() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        etName.setText(prefs.getString("userName", ""));
        etAge.setText(String.valueOf(prefs.getInt("userAge", 20)));
        etWeight.setText(String.valueOf(prefs.getFloat("userWeight", 60f)));
        etHeight.setText(String.valueOf(prefs.getFloat("userHeight", 170f)));
        etCalories.setText(String.valueOf(prefs.getInt("userCalories", 2000)));

        // Memuat data kalori yang sudah dimakan
        if (etCaloriesConsumed != null) {
            etCaloriesConsumed.setText(String.valueOf(prefs.getInt("userCaloriesConsumed", 0)));
        }
    }

    private void saveData() {
        try {
            String name = etName.getText().toString();
            int age = Integer.parseInt(etAge.getText().toString());
            float weight = Float.parseFloat(etWeight.getText().toString());
            float height = Float.parseFloat(etHeight.getText().toString());
            int calories = Integer.parseInt(etCalories.getText().toString());

            // Mengambil angka kalori yang sudah dimakan dari kotak input
            int caloriesConsumed = 0;
            if (etCaloriesConsumed != null && etCaloriesConsumed.getText() != null && !etCaloriesConsumed.getText().toString().isEmpty()) {
                caloriesConsumed = Integer.parseInt(etCaloriesConsumed.getText().toString());
            }

            // Hitung BMI
            float heightInMeter = height / 100;
            float bmi = weight / (heightInMeter * heightInMeter);

            // Simpan ke SharedPreferences
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("userName", name);
            editor.putInt("userAge", age);
            editor.putFloat("userWeight", weight);
            editor.putFloat("userHeight", height);
            editor.putFloat("userBMI", bmi);
            editor.putInt("userCalories", calories);
            editor.putInt("userCaloriesConsumed", caloriesConsumed); // Menyimpan kalori yang dimakan
            editor.apply();

            Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Harap isi semua kolom dengan benar!", Toast.LENGTH_SHORT).show();
        }
    }
}