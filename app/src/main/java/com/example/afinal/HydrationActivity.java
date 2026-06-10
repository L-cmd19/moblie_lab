package com.example.afinal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class HydrationActivity extends AppCompatActivity {

    private TextInputEditText etWaterTarget, etWaterConsumed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hydration);

        etWaterTarget = findViewById(R.id.etWaterTarget);
        etWaterConsumed = findViewById(R.id.etWaterConsumed);
        MaterialButton btnSave = findViewById(R.id.btnSaveHydration);

        // Muat data lama
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        etWaterTarget.setText(String.valueOf(prefs.getInt("userWaterTarget", 2000)));
        etWaterConsumed.setText(String.valueOf(prefs.getInt("userWaterConsumed", 0)));

        btnSave.setOnClickListener(v -> {
            try {
                int target = Integer.parseInt(etWaterTarget.getText().toString());
                int consumed = Integer.parseInt(etWaterConsumed.getText().toString());

                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("userWaterTarget", target);
                editor.putInt("userWaterConsumed", consumed);
                editor.apply();

                Toast.makeText(this, "Data hidrasi diperbarui!", Toast.LENGTH_SHORT).show();
                finish(); // Kembali ke dasbor
            } catch (Exception e) {
                Toast.makeText(this, "Masukkan angka yang valid!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}