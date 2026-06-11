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
        return inflater.inflate(R.layout.activity_fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTotalXP = view.findViewById(R.id.tvTotalXP);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        historyContainer = view.findViewById(R.id.historyContainer);
    }

    // --- FUNGSI INI WAJIB ADA AGAR XP LANGSUNG BERUBAH ---
    @Override
    public void onResume() {
        super.onResume();
        loadHistoryData();
    }

    private void loadHistoryData() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Membaca XP terbaru dari memori
        int totalXP = prefs.getInt("userXP", 0);
        String history = prefs.getString("missionHistory", "");

        // Langsung perbarui teks di layar
        tvTotalXP.setText("🏆 Total XP: " + totalXP);
        historyContainer.removeAllViews(); // Bersihkan wadah agar kartu tidak menumpuk ganda

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

                String cleanName = mission.replace("✅ ", "").replace(" (+20 XP)", "");

                tvTaskName.setText(cleanName);
                tvPoints.setText("+20 XP");

                historyContainer.addView(itemView);
            }
        }
    }
}