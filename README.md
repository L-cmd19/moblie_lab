# Life and Live

**Life and Live** adalah aplikasi pelacak kesehatan dan kebugaran berbasis Android yang membantu pengguna memantau kebiasaan harian seperti hidrasi, durasi tidur, target kalori, dan indeks massa tubuh (BMI). Aplikasi ini dilengkapi sistem gamifikasi berupa *Daily Quests* untuk mendorong pengguna tetap aktif.

Proyek ini dikembangkan sebagai pemenuhan tugas akhir praktikum/laboratorium, mencakup implementasi Activity, Fragment, Navigation Component, Background Thread, Networking (API), dan Local Data Persistence.

---

## Fitur Utama

### 1. Dasbor Statistik Interaktif
Menampilkan ringkasan data kesehatan pengguna (BMI, sisa kalori, konsumsi air, durasi tidur) dengan indikator visual menggunakan *Circular Progress Bar* dan *Horizontal Progress Bar*.

### 2. Misi Harian (Gamifikasi)
Mengambil data panduan gerakan olahraga secara acak dari ExerciseDB API dan menampilkannya via `RecyclerView` dengan gestur *Swipe-to-Complete/Skip*. Setiap misi yang diselesaikan memberikan +20 XP. Dilengkapi mode offline bila koneksi internet tidak tersedia.

### 3. Riwayat & Papan Skor
Menampilkan total XP yang terkumpul beserta daftar riwayat misi yang berhasil diselesaikan dalam tampilan kartu.

### 4. Pencatat Hidrasi & Tidur
Menghitung durasi tidur otomatis berdasarkan jam tidur dan bangun, serta menghitung persentase pemenuhan target konsumsi air harian (dalam mL dan L).

### 5. Tema Dinamis (Dark / Light Mode)
Mendukung pergantian tema gelap dan terang secara langsung dari halaman Pengaturan. Preferensi tema tersimpan secara persisten dan diterapkan saat aplikasi dibuka kembali.

### 6. Kutipan Motivasi Harian
Mengambil kutipan motivasi dari server secara asynchronous, dengan tombol *refresh* manual untuk memuat kutipan baru.

---

## Teknologi & Arsitektur

| Kategori | Detail |
|---|---|
| Bahasa | Java & XML |
| Minimum SDK | 24 (Android 7.0 Nougat) |
| Navigasi | Android Navigation Component + Bottom Navigation |
| UI | Google Material Design (`MaterialCardView`, `MaterialSwitch`, `ShapeableImageView`) |
| Networking | Retrofit2 + Gson Converter |
| Background Task | `ExecutorService` (Single Thread Executor) |
| Penyimpanan Lokal | `SharedPreferences` |
| Sumber API | ExerciseDB via RapidAPI |

---

## Instalasi & Konfigurasi

### 1. Clone Repositori

```bash
git clone https://github.com/username/life-and-live.git
```

### 2. Buka di Android Studio

Buka folder proyek menggunakan Android Studio versi terbaru, lalu tunggu hingga proses sinkronisasi Gradle selesai.

### 3. Konfigurasi API Key

Aplikasi memerlukan API Key dari [RapidAPI](https://rapidapi.com/) untuk mengakses data olahraga.

1. Buat akun di RapidAPI dan berlangganan **ExerciseDB API** (tersedia paket gratis).
2. Buka file berikut: app/src/main/java/com/example/afinal/MissionActivity.java
3. Ganti nilai variabel `RAPID_API_KEY` dengan kunci milikmu:
```java
   private final String RAPID_API_KEY = "MASUKKAN_API_KEY_KAMU_DI_SINI";
```

### 4. Build & Run

Tekan **Run** (`Shift + F10`) untuk mengompilasi dan menjalankan aplikasi di emulator atau perangkat fisik.

---

## Struktur Navigasi

| Komponen | Deskripsi |
|---|---|
| `MainActivity` | Titik masuk utama dan pengendali Bottom Navigation |
| `HomeFragment` | Dasbor ringkasan status kesehatan pengguna |
| `RiwayatFragment` | Akumulasi XP dan daftar riwayat misi selesai |
| `SettingsActivity` | Formulir metrik tubuh dan pengaturan tema |
| `HydrationActivity` | Pembaruan data konsumsi air |
| `SleepActivity` | Pembaruan data jam tidur |
| `MissionActivity` | Eksekusi Daily Quests (fetch API + interaksi swipe) |

---

## Pengembang

**Muhammad Arlis**
Mahasiswa Program Studi Sistem Informasi — Universitas Hasanuddin

*Proyek ini disusun sebagai pemenuhan tugas akhir praktikum/laboratorium.*
