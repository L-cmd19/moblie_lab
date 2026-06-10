package com.example.afinal;

public class Mission {
    private String name;
    private String description;
    private String reward;

    public Mission(String name, String description, String reward) {
        this.name = name;
        this.description = description;
        this.reward = reward;
    }

    // Fungsi yang dipakai oleh MissionActivity untuk menyimpan riwayat
    public String getName() {
        return name;
    }

    // --- TAMBAHAN UNTUK MENGATASI ERROR "getTitle" DI ADAPTER ---
    public String getTitle() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getReward() {
        return reward;
    }
}