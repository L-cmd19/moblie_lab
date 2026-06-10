package com.example.afinal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissionActivity extends AppCompatActivity {

    private RecyclerView rvMissions;
    private MissionAdapter adapter;
    private List<Mission> missionList;

    // API KEY RAPIDAPI
    private final String RAPID_API_KEY = "31b497bfa3msh7efb33459d91b75p12c020jsn22627801a763";
    private final String RAPID_API_HOST = "exercisedb.p.rapidapi.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mission_swipe);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rvMissions), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Tombol Back
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Setup Dasar RecyclerView
        rvMissions = findViewById(R.id.rvMissions);
        rvMissions.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        missionList = new ArrayList<>();
        adapter = new MissionAdapter(missionList);
        rvMissions.setAdapter(adapter);

        setupSwipeListener();
        setupButtons();

        // Panggil API saat halaman dibuka
        fetchMissionsFromApi();
    }

    private void fetchMissionsFromApi() {
        Toast.makeText(this, "Mencari quest latihan baru...", Toast.LENGTH_SHORT).show();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            FitApiService api = RetrofitClient.getClient().create(FitApiService.class);
            api.getExercises(RAPID_API_KEY, RAPID_API_HOST, 4).enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        JsonArray exercises = response.body();
                        missionList.clear();

                        for (int i = 0; i < exercises.size(); i++) {
                            JsonObject exercise = exercises.get(i).getAsJsonObject();

                            String name = exercise.get("name").getAsString().toUpperCase();
                            String targetMuscle = exercise.get("target").getAsString();
                            String equipment = exercise.get("equipment").getAsString();

                            String description = "Fokus: Otot " + targetMuscle + ". Peralatan: " + equipment + ".";
                            String reward = "+" + ((i + 1) * 20) + " XP";

                            missionList.add(new Mission(name, description, reward));
                        }

                        adapter.notifyDataSetChanged();
                        Toast.makeText(MissionActivity.this, "Quest Harian Siap!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MissionActivity.this, "Gagal mengambil data. Cek API Key.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Toast.makeText(MissionActivity.this, "Koneksi terputus. Menggunakan misi *offline*.", Toast.LENGTH_LONG).show();
                    loadOfflineMissions();
                }
            });
        });
    }

    private void loadOfflineMissions() {
        missionList.clear();
        missionList.add(new Mission("Peregangan Punggung", "Lakukan peregangan tubuh selama 5 menit.", "+50 XP"));
        missionList.add(new Mission("Detox Digital", "Jauhkan HP 1 jam sebelum tidur.", "+100 XP"));
        adapter.notifyDataSetChanged();
    }

    private void setupButtons() {
        FloatingActionButton btnSkip = findViewById(R.id.btnSkip);
        FloatingActionButton btnComplete = findViewById(R.id.btnComplete);

        btnComplete.setOnClickListener(v -> {
            if (!missionList.isEmpty()) {
                // AMBIL DATA MISI DAN SIMPAN
                Mission completedMission = missionList.get(0);
                saveCompletedMission(completedMission.getName());

                Toast.makeText(MissionActivity.this, "Quest Selesai! XP Ditambahkan.", Toast.LENGTH_SHORT).show();
                adapter.removeItem(0);
            } else {
                Toast.makeText(MissionActivity.this, "Semua quest hari ini sudah selesai!", Toast.LENGTH_SHORT).show();
            }
        });

        btnSkip.setOnClickListener(v -> {
            if (!missionList.isEmpty()) {
                Toast.makeText(MissionActivity.this, "Quest Dilewati.", Toast.LENGTH_SHORT).show();
                adapter.removeItem(0);
            } else {
                Toast.makeText(MissionActivity.this, "Tidak ada quest tersisa.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSwipeListener() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                if (swipeDir == ItemTouchHelper.RIGHT) {
                    // AMBIL DATA MISI DAN SIMPAN JIKA SWIPE KANAN
                    Mission completedMission = missionList.get(position);
                    saveCompletedMission(completedMission.getName());

                    Toast.makeText(MissionActivity.this, "Quest Selesai!", Toast.LENGTH_SHORT).show();
                } else if (swipeDir == ItemTouchHelper.LEFT) {
                    Toast.makeText(MissionActivity.this, "Quest Dilewati.", Toast.LENGTH_SHORT).show();
                }
                adapter.removeItem(position);
            }
        };
        new ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(rvMissions);
    }

    // --- FUNGSI BARU UNTUK MENYIMPAN RIWAYAT & XP ---
    private void saveCompletedMission(String missionName) {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // 1. TAMBAHKAN XP (Pastikan dua baris ini tidak hilang)
        int currentXP = prefs.getInt("userXP", 0);
        editor.putInt("userXP", currentXP + 20); // Tambah 20 XP ke total saat ini

        // 2. Simpan Riwayat
        String riwayatLama = prefs.getString("missionHistory", "");
        String riwayatBaru = "✅ " + missionName + " (+20 XP)\n" + riwayatLama;
        editor.putString("missionHistory", riwayatBaru);

        editor.apply(); // Kunci dan simpan semua perubahan
    }
}