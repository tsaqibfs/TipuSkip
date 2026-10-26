# Panduan Alur Kerja Git & Strategi Bebas Konflik (Git Workflow)

Agar 3 orang anggota kelompok dapat bekerja secara paralel di laptop masing-masing tanpa mengalami **Merge Conflict**, terapkan aturan alur kerja Git berikut.

---

## 🌳 Struktur Cabang (Branching Strategy)

1. `main`:
   - Cabang rilis final yang stabil dan siap didemokan kepada dosen/asisten praktikum.
   - **Dilarang langsung melakukan `git commit` ke `main`**.
2. `develop`:
   - Cabang integrasi bersama. Tempat seluruh fitur baru digabungkan dan diuji sebelum rilis.
3. `feature/branch-name`:
   - Cabang kerja mandiri masing-masing anggota:
     - Anggota 1: `feature/ui-dan-simulasi`
     - Anggota 2: `feature/database-dan-admin`
     - Anggota 3: `feature/ocr-dan-analyzer`

---

## 🚫 4 Aturan Emas Mencegah Konflik (Conflict Prevention Rules)

1. **Aturan Hak Kepemilikan Berkas**:
   - Jangan pernah menyunting berkas yang merupakan area fokus anggota lain (lihat tabel di `DOKUMENTASI_PEMBAGIAN_TUGAS.md`).
   - Jika Anda membutuhkan data atau fungsi dari modul lain, gunakan antarmuka (_Interface_) atau _Method Signature_ yang sudah disepakati di package `service/` dan `dao/`.
2. **Pull Sebelum Push**:
   - Selalu lakukan sinkronisasi cabang Anda dengan `develop` sebelum mulai bekerja dan sebelum membuat Pull Request:
     ```bash
     git checkout feature/<nama-fitur-anda>
     git pull origin develop
     ```
3. **Komit Sering dengan Pesan Jelas (Atomic Commits)**:
   - Buat komit kecil dan fokus (misal: "tambah fxml untuk halaman riwayat" atau "implementasi regex pencocokan OTP"). Hindari menumpuk pekerjaan satu minggu dalam satu komit besar.
4. **Gunakan .gitignore Secara Ketat**:
   - Jangan pernah melakukan komit pada folder IDE (`.idea/`, `.vscode/`, `*.iml`), folder build (`target/`), atau data lokal Docker (`mysql_data/`). Berkas-berkas tersebut sudah dimasukkan ke `.gitignore`.

---

## 🚀 Panduan Langkah demi Langkah (Step-by-Step)

### 1. Inisialisasi Pertama Kali & Clone Repositori
Setelah repositori dibuat di GitHub oleh ketua kelompok:
```bash
git clone https://github.com/<username>/TipuSkip.git
cd TipuSkip
```

### 2. Berpindah ke Cabang Kerja Anda
Pilih cabang sesuai peran Anda:

**Anggota 1 (UI & Simulasi):**
```bash
git checkout -b feature/ui-dan-simulasi
```

**Anggota 2 (Database & Admin):**
```bash
git checkout -b feature/database-dan-admin
```

**Anggota 3 (OCR & Analyzer):**
```bash
git checkout -b feature/ocr-dan-analyzer
```

### 3. Alur Kerja Harian (Edit, Test, Commit)
Saat Anda selesai menyelesaikan satu unit tugas:
```bash
# 1. Periksa status berkas yang diubah
git status

# 2. Tambahkan berkas ke staging
git add src/main/java/com/tipuskip/...

# 3. Buat commit deskriptif
git commit -m "feat(simulasi): tambahkan logika branching skenario pesan"

# 4. Push cabang Anda ke GitHub
git push origin feature/<nama-cabang-anda>
```

### 4. Menggabungkan Pekerjaan ke Cabang `develop` (Pull Request)
1. Buka repositori proyek di GitHub.
2. Klik tombol **"Compare & pull request"**.
3. Pastikan target penggabungan adalah `base: develop` $\leftarrow$ `compare: feature/<nama-cabang-anda>`.
4. Minta rekan sekelompok Anda untuk mereview kode sebelum ditekan tombol **"Merge pull request"**.

---

## 🛠️ Cara Mengatasi Konflik Jika Tetap Terjadi

Jika terjadi konflik saat merge di Git:
1. Jalankan `git status` untuk melihat berkas yang bertanda `both modified`.
2. Buka berkas tersebut di VS Code atau IntelliJ IDEA.
3. Anda akan melihat penanda konflik:
   ```java
   <<<<<<< HEAD
   // Kode di branch Anda
   =======
   // Kode dari branch rekan Anda di develop
   >>>>>>> develop
   ```
4. Diskusikan dengan rekan sekelompok baris mana yang harus dipertahankan, hapus penanda konflik (`<<<<<<<`, `=======`, `>>>>>>>`), lalu simpan berkas.
5. Jalankan:
   ```bash
   git add <nama-berkas-yang-sudah-diperbaiki>
   git commit -m "fix: selesaikan konflik merge pada controller"
   git push origin <nama-cabang-anda>
   ```
