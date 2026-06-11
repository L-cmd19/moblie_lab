package com.example.afinal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SleepActivity extends AppCompatActivity {

    private TextInputEditText etSleepTime, etWakeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        etSleepTime = findViewById(R.id.etSleepTime);
        etWakeTime = findViewById(R.id.etWakeTime);
        MaterialButton btnSave = findViewById(R.id.btnSaveSleep);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        etSleepTime.setText(prefs.getString("userSleepTime", "22:00"));
        etWakeTime.setText(prefs.getString("userWakeTime", "06:00"));

        btnSave.setOnClickListener(v -> {
            String sleepTime = etSleepTime.getText().toString();
            String wakeTime = etWakeTime.getText().toString();
            String sleepDuration = "0h 0m";

            try {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                Date dateSleep = format.parse(sleepTime);
                Date dateWake = format.parse(wakeTime);

                long difference = dateWake.getTime() - dateSleep.getTime();
                if (difference < 0) {
                    difference += 24 * 60 * 60 * 1000;
                }

                int hours = (int) (difference / (1000 * 60 * 60));
                int minutes = (int) (difference / (1000 * 60)) % 60;
                sleepDuration = hours + "h " + minutes + "m";

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("userSleepTime", sleepTime);
                editor.putString("userWakeTime", wakeTime);
                editor.putString("userSleepDuration", sleepDuration);
                editor.apply();

                Toast.makeText(this, "Waktu tidur diperbarui!", Toast.LENGTH_SHORT).show();
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Format jam salah. Gunakan HH:mm", Toast.LENGTH_SHORT).show();
            }
        });
    }
}