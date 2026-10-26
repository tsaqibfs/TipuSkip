# Pembagian Tugas Tim TipuSkip (3 Orang)

Dokumentasi ini menetapkan batas kerja (_boundary_), tanggung jawab, dan berkas kerja spesifik untuk masing-masing dari 3 anggota tim agar **tidak terjadi konflik merge (_git conflict_)** selama proses pengerjaan.

---

## Ringkasan Pembagian Modul & Tanggung Jawab

| Anggota | Area Kerja Utama | Kebutuhan Fungsional (PRD) | Folder / Package Fokus |
|---|---|---|---|
| **Anggota 1** | **UI/UX JavaFX & Simulasi Interaktif**<br>Perancangan antarmuka, alur percakapan pesan & panggilan telepon, penyusunan konten skenario, dan dokumentasi user. | FR-01, FR-02, FR-03, FR-04, FR-05, FR-11 | `controller/`, `view/`, `css/`, `simulation/` |
| **Anggota 2** | **Database, DAO & Manajemen Admin**<br>Perancangan skema MySQL, koneksi JDBC, kelas POJO Model, DAO pattern, CRUD skenario & indikator oleh admin, pencatatan riwayat. | FR-10, FR-12, FR-13 | `config/`, `model/`, `dao/`, `database/`, `service/impl/AdminServiceImpl.java` |
| **Anggota 3** | **Analisis Tangkapan Layar & Mesin Risiko**<br>Ekstraksi teks gambar (OCR Tess4J), pencocokan kata kunci/regex, evaluasi bobot & tingkat risiko, penyusunan alasan & saran verifikasi. | FR-06, FR-07, FR-08, FR-09 | `analyzer/`, `tessdata/`, `service/impl/AnalisisServiceImpl.java` |

---

## 👤 Anggota 1: UI/UX JavaFX, Simulasi Pesan & Telepon, Skenario Konten

### Tanggung Jawab Utama:
1. **Perancangan Antarmuka FXML & CSS**:
   - Mendesain antarmuka modern yang nyaman dipakai (chat bubble, dialer telepon, card hasil analisis).
   - Menjaga konsistensi styling di `src/main/resources/com/tipuskip/css/style.css`.
2. **Logika Simulasi (Penerapan Konsep PBO Abstraksi, Pewarisan, Polimorfisme)**:
   - Mengelola kelas turunan `SimulasiPesan` dan `SimulasiTelepon` dari kelas abstrak `Simulasi`.
   - Mengatur transisi langkah demi langkah, branching respons, dan akumulasi poin skor pengguna.
3. **Penyusunan Konten Skenario**:
   - Menulis naskah dialog percakapan penipuan realistis (misal: modus freelance task, penipuan kurir paket, pembaharuan tarif bank).
4. **Dokumentasi Pengguna**:
   - Menyusun materi edukasi pada Tab Edukasi (FR-11) dan buku petunjuk/laporan akhir.

### Berkas yang Menjadi Hak Akses Anggota 1:
- `src/main/java/com/tipuskip/MainApp.java`
- `src/main/java/com/tipuskip/simulation/Simulasi.java` (Abstraksi PBO)
- `src/main/java/com/tipuskip/simulation/SimulasiPesan.java` (Pewarisan & Polimorfisme)
- `src/main/java/com/tipuskip/simulation/SimulasiTelepon.java` (Pewarisan & Polimorfisme)
- `src/main/java/com/tipuskip/controller/MainController.java`
- `src/main/java/com/tipuskip/controller/SimulasiPesanController.java`
- `src/main/java/com/tipuskip/controller/SimulasiTeleponController.java`
- `src/main/resources/com/tipuskip/view/*.fxml`
- `src/main/resources/com/tipuskip/css/style.css`

> **Tips Anti-Konflik**: Anggota 1 dapat menggunakan `MockSimulasiService` dan `MockTextExtractor` untuk menguji UI secara instan tanpa perlu menunggu MySQL atau Tesseract siap di komputernya!

---

## 👤 Anggota 2: Basis Data MySQL, JDBC, Model, DAO & Fitur Admin

### Tanggung Jawab Utama:
1. **Perancangan Basis Data & Migrasi**:
   - Mengembangkan skrip DDL `database/schema.sql` dan `database/seed_data.sql`.
   - Mengelola koneksi database singleton di `DatabaseConnection.java`.
2. **Implementasi POJO Model & Data Access Object (DAO)**:
   - Memastikan prinsip **Enkapsulasi** diterapkan dengan benar pada kelas model (atribut `private`, `getter/setter`, validasi data).
   - Mengimplementasikan `GenericDAO` dan DAO konkret untuk operasi CRUD pada 8 tabel database.
3. **Pengelolaan Skenario & Indikator (Admin Feature)**:
   - Menyediakan fungsi bagi Admin untuk menambah, mengedit, dan menonaktifkan skenario serta indikator penipuan tanpa mengubah baris kode program (FR-12 & FR-13).
4. **Pencatatan & Pengambilan Riwayat**:
   - Menyimpan hasil simulasi (`hasil_simulasi`) dan riwayat analisis gambar (`analisis_tangkapan_layar`) per akun pengguna (FR-05 & FR-10).

### Berkas yang Menjadi Hak Akses Anggota 2:
- `src/main/java/com/tipuskip/config/DatabaseConnection.java`
- `src/main/java/com/tipuskip/model/*` (POJO Enkapsulasi)
- `src/main/java/com/tipuskip/dao/*` (DAO Interface)
- `src/main/java/com/tipuskip/dao/impl/*` (DAO JDBC Implementation)
- `src/main/java/com/tipuskip/service/impl/AdminServiceImpl.java`
- `src/main/java/com/tipuskip/controller/AdminController.java`
- `src/main/java/com/tipuskip/controller/RiwayatController.java`
- `database/*.sql`
- `docker-compose.yml`

> **Tips Anti-Konflik**: Gunakan `docker compose up -d` untuk menjalankan database MySQL lokal yang identik dengan anggota tim lainnya. Uji query dan DAO menggunakan unit test tanpa perlu menyentuh antarmuka JavaFX.

---

## 👤 Anggota 3: Analisis Tangkapan Layar (OCR, Pencocokan Indikator & Penilaian Risiko)

### Tanggung Jawab Utama:
1. **Ekstraksi Teks (OCR)**:
   - Mengintegrasikan library Tess4J dengan file model bahasa (`tessdata/ind.traineddata` dan `eng.traineddata`).
   - Menyediakan fallback mekanisme (`MockTextExtractor`) agar tidak menyebabkan crash jika Tesseract native belum terkonfigurasi.
2. **Mesin Pencocokan Indikator**:
   - Mengembangkan algoritma pencocokan kata kunci dan Regular Expression (Regex) yang toleran huruf besar/kecil (_case-insensitive_).
   - Menangkap potongan konteks kata/kalimat yang memicu indikator penipuan.
3. **Evaluasi Tingkat Risiko**:
   - Mengakumulasi bobot indikator yang cocok.
   - Menetapkan ambang batas risiko secara konsisten:
     - **RENDAH**: Skor < 25 (minim pola mencurigakan)
     - **SEDANG**: Skor 25 - 54 (perlu verifikasi manual)
     - **TINGGI**: Skor >= 55 (indikator kuat seperti OTP / APK berbahaya)
4. **Penyusunan Hasil Analisis & Batasan Edukasi**:
   - Menyusun alasan penilaian yang jelas dan saran verifikasi tindakan nyata.
   - Memastikan pernyataan _disclaimer_ (bahwa hasil bersifat edukasi dan bukan kepastian hukum) selalu disertakan (FR-09 & Aturan Bisnis).

### Berkas yang Menjadi Hak Akses Anggota 3:
- `src/main/java/com/tipuskip/analyzer/ITextExtractor.java`
- `src/main/java/com/tipuskip/analyzer/TesseractTextExtractor.java`
- `src/main/java/com/tipuskip/analyzer/MockTextExtractor.java`
- `src/main/java/com/tipuskip/analyzer/IndikatorMatcher.java`
- `src/main/java/com/tipuskip/analyzer/RiskEvaluator.java`
- `src/main/java/com/tipuskip/analyzer/HasilPenilaianRisiko.java`
- `src/main/java/com/tipuskip/service/impl/AnalisisServiceImpl.java`
- `tessdata/*`
- `src/test/java/com/tipuskip/analyzer/RiskEvaluatorTest.java`

> **Tips Anti-Konflik**: Anggota 3 dapat menguji seluruh logika OCR, Regex, dan kalkulasi risiko dengan menjalankan `RiskEvaluatorTest.java` secara independen dari JUnit tanpa perlu membuka window GUI JavaFX ataupun MySQL!

---

## 🔄 Kontrak Antar Modul (Bagaimana 3 Orang Terhubung)

```
[ Anggota 1: JavaFX GUI ]
         │
         ▼
[ Service Interfaces: ISimulasiService / IAnalisisService / IAdminService ]
         │
         ├───────────────────────────────┼──────────────────────────────┐
         ▼                               ▼                              ▼
[ Anggota 1: Simulasi Engine ]   [ Anggota 2: DAO & JDBC ]   [ Anggota 3: OCR & Risk Engine ]
  - SimulasiPesan                  - SkenarioDAO               - TesseractTextExtractor
  - SimulasiTelepon                - IndikatorPenipuanDAO      - IndikatorMatcher
                                   - DatabaseConnection        - RiskEvaluator
```

Dengan kontrak antarmuka (_interfaces_) ini, setiap anggota tim dapat bekerja secara independen di branch git masing-masing tanpa takut merusak kode milik rekan sekelompok.
