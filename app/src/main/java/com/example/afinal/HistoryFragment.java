package com.example.afinal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HistoryFragment extends Fragment {

    private TextView tvTotalXP, tvEmptyState;
    private LinearLayout historyContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Menghubungkan variabel Java dengan elemen di XML
        tvTotalXP = view.findViewById(R.id.tvTotalXP);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        historyContainer = view.findViewById(R.id.historyContainer);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadHistoryData(); // Memaksa halaman untuk membaca data terbaru setiap kali dibuka
    }

    private void loadHistoryData() {
        if (getActivity() == null) return;

        // Mengakses memori HP tempat kita menyimpan XP
        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        int totalXP = prefs.getInt("userXP", 0);
        String history = prefs.getString("missionHistory", "");

        // Tampilkan Total XP
        tvTotalXP.setText("🏆 Total XP: " + totalXP);
        historyContainer.removeAllViews(); // Bersihkan wadah agar kartu tidak menumpuk

        if (history.trim().isEmpty()) {
            // Jika kosong, tampilkan tulisan "Belum ada misi"
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            // Jika ada isinya, sembunyikan tulisan "Belum ada misi"
            tvEmptyState.setVisibility(View.GONE);

            // Pecah teks riwayat per baris
            String[] missions = history.split("\n");

            for (String mission : missions) {
                if (mission.trim().isEmpty()) continue;

                // Cetak desain kartu (activity_item_history.xml)
                View itemView = getLayoutInflater().inflate(R.layout.activity_item_history, historyContainer, false);

                TextView tvTaskName = itemView.findViewById(R.id.tvTaskName);
                TextView tvPoints = itemView.findViewById(R.id.tvPoints);

                // Membersihkan format teks
                String cleanName = mission.replace("✅ ", "");
                String pointsText = "+20 XP"; // Default

                // Memisahkan nama misi dan jumlah XP-nya secara dinamis
                if (cleanName.contains("(") && cleanName.contains(")")) {
                    int startIndex = cleanName.lastIndexOf("(");
                    int endIndex = cleanName.lastIndexOf(")");

                    if (startIndex < endIndex) {
                        pointsText = cleanName.substring(startIndex + 1, endIndex);
                        cleanName = cleanName.substring(0, startIndex).trim();
                    }
                } else if (cleanName.contains(" (+20 XP)")) {
                    cleanName = cleanName.replace(" (+20 XP)", "");
                }

                // Masukkan teks ke dalam kartu
                tvTaskName.setText(cleanName);
                tvPoints.setText(pointsText);

                // Tempelkan kartu ke layar
                historyContainer.addView(itemView);
            }
        }
    }
}