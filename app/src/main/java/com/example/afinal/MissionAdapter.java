package com.example.afinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MissionAdapter extends RecyclerView.Adapter<MissionAdapter.MissionViewHolder> {

    private List<Mission> missionList;

    public MissionAdapter(List<Mission> missionList) {
        this.missionList = missionList;
    }

    @NonNull
    @Override
    public MissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_mission_card, parent, false);
        return new MissionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MissionViewHolder holder, int position) {
        Mission mission = missionList.get(position);
        holder.tvTitle.setText(mission.getTitle());
        holder.tvDesc.setText(mission.getDescription());
        holder.tvReward.setText(mission.getReward());
    }

    @Override
    public int getItemCount() {
        return missionList.size();
    }

    public void removeItem(int position) {
        missionList.remove(position);
        notifyItemRemoved(position);
    }

    public static class MissionViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc, tvReward;

        public MissionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvMissionTitle);
            tvDesc = itemView.findViewById(R.id.tvMissionDesc);
            tvReward = itemView.findViewById(R.id.tvReward);
        }
    }
}