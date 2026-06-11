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

public class RiwayatFragment extends Fragment {

    private TextView tvTotalXP, tvEmptyState;
    private LinearLayout historyContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Pastikan nama XML ini sesuai dengan milikmu (activity_fragment_history.xml)
        return inflater.inflate(R.layout.activity_fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTotalXP = view.findViewById(R.id.tvTotalXP);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        historyContainer = view.findViewById(R.id.historyContainer);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadHistoryData();
    }

    private void loadHistoryData() {
        if (getActivity() == null) return;

        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        int totalXP = prefs.getInt("userXP", 0);
        String history = prefs.getString("missionHistory", "");

        tvTotalXP.setText("🏆 Total XP: " + totalXP);
        historyContainer.removeAllViews();

        if (history.trim().isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            tvEmptyState.setVisibility(View.GONE);

            String[] missions = history.split("\n");

            for (String mission : missions) {
                if (mission.trim().isEmpty()) continue;

                View itemView = getLayoutInflater().inflate(R.layout.activity_item_history, historyContainer, false);

                TextView tvTaskName = itemView.findViewById(R.id.tvTaskName);
                TextView tvPoints = itemView.findViewById(R.id.tvPoints);

                // --- PEMBACA TEKS DINAMIS BARU ---
                // Format yang tersimpan sekarang: "✅ Detox Digital (+100 XP)"
                String cleanName = mission.replace("✅ ", ""); // Menjadi: "Detox Digital (+100 XP)"
                String pointsText = "+20 XP"; // XP default jaga-jaga

                // Pisahkan nama dan poinnya secara otomatis menggunakan tanda kurung
                if (cleanName.contains("(") && cleanName.contains(")")) {
                    int startIndex = cleanName.lastIndexOf("(");
                    int endIndex = cleanName.lastIndexOf(")");

                    if (startIndex < endIndex) {
                        pointsText = cleanName.substring(startIndex + 1, endIndex); // Mengambil "+100 XP"
                        cleanName = cleanName.substring(0, startIndex).trim(); // Mengambil "Detox Digital"
                    }
                } else if (cleanName.contains(" (+20 XP)")) {
                    // Jaga-jaga jika kamu punya data lama yang masih tersimpan
                    cleanName = cleanName.replace(" (+20 XP)", "");
                }

                tvTaskName.setText(cleanName);
                tvPoints.setText(pointsText);

                historyContainer.addView(itemView);
            }
        }
    }
}