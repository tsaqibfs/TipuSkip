# TipuSkip 🛡️
### Aplikasi Desktop Edukasi Pencegahan Penipuan Digital (Praktikum PBO)

**TipuSkip** adalah aplikasi desktop berbasis Java untuk edukasi pencegahan penipuan digital. Aplikasi ini membantu pengguna mengenali indikator penipuan, berlatih menentukan respons yang aman melalui simulasi percakapan pesan dan panggilan telepon, serta memperoleh analisis awal atas tangkapan layar (_screenshot_) percakapan yang mencurigakan.

> **Catatan Edukasi**: Hasil analisis bersifat deteksi awal untuk pembelajaran dan tidak menetapkan secara pasti bahwa suatu pesan atau pengirim adalah penipu.

---

## 👥 Pembagian Tugas Kelompok (3 Orang)

Untuk mencegah terjadinya konflik merge di GitHub, proyek ini telah dipisah menjadi 3 area kerja yang terisolasi dengan _Interface Contracts_:

1. **Anggota 1 (UI/UX & Simulasi Interaktif)**:
   - Antarmuka JavaFX (FXML & CSS), integrasi navigasi tab.
   - Logika simulasi pesan (chat bubble) dan simulasi telepon (dialer & caller ID).
   - Penyusunan naskah skenario dan dokumentasi edukasi.
2. **Anggota 2 (Database, DAO & Admin Management)**:
   - Skema basis data MySQL 8.0, migrasi DDL & DML, koneksi JDBC singleton.
   - Kelas POJO Model (Enkapsulasi) dan DAO Pattern untuk 8 entitas data.
   - Fitur admin untuk mengelola skenario dan indikator penipuan (FR-12 & FR-13), serta riwayat pengguna (FR-10).
3. **Anggota 3 (OCR & Mesin Analisis Risiko)**:
   - Ekstraksi teks dari tangkapan layar menggunakan Tess4J (Tesseract OCR).
   - Mesin pencocokan pola kata kunci dan Regular Expression (Regex).
   - Algoritma penilaian risiko (bobot skor dan ambang batas RENDAH, SEDANG, TINGGI).
   - Penyusunan alasan penilaian dan saran rekomendasi verifikasi.

*Panduan detail tugas dan aturan Git dapat dibaca pada [DOKUMENTASI_PEMBAGIAN_TUGAS.md](DOKUMENTASI_PEMBAGIAN_TUGAS.md) dan [GIT_WORKFLOW.md](GIT_WORKFLOW.md).*

---

## 💡 Penerapan Konsep Pemrograman Berorientasi Objek (PBO)

Aplikasi ini mengimplementasikan 4 pilar utama PBO secara komprehensif:

1. **Enkapsulasi (_Encapsulation_)**:
   - Seluruh atribut pada kelas Model (`Pengguna`, `Skenario`, `LangkahSkenario`, `PilihanRespons`, `HasilSimulasi`, `IndikatorPenipuan`, `AnalisisTangkapanLayar`) dibuat `private` dengan akses melalui `getter` dan `setter` serta validasi.
2. **Abstraksi (_Abstraction_)**:
   - Kelas abstrak `com.tipuskip.simulation.Simulasi` mendefinisikan kontrak utama alur simulasi: `jalankan()`, `prosesRespons()`, `hitungSkor()`, `evaluasiHasil()`, dan `dapatkanRingkasanPembelajaran()`.
   - Antarmuka `ITextExtractor` mengabstraksikan engine OCR (Tesseract asli vs Mock fallback).
   - Antarmuka `GenericDAO` dan Service Interfaces (`ISimulasiService`, `IAnalisisService`, `IAdminService`) memisahkan antarmuka pengguna dari detail database.
3. **Pewarisan (_Inheritance_)**:
   - `SimulasiPesan` dan `SimulasiTelepon` mewarisi (extends) kelas abstrak `Simulasi`.
4. **Polimorfisme (_Polymorphism_)**:
   - Metode `prosesRespons()` dan `jalankan()` dieksekusi secara polimorfis dengan perilaku yang berbeda antara simulasi pesan (tampilan balon obrolan & deteksi link bahaya) dan simulasi telepon (status sambungan & deteksi manipulasi OTP).

---

## 🛠️ Arsitektur Folder Proyek

```text
TipuSkip/
├── .gitignore                      # Mengabaikan IDE, build target, dan dataset besar
├── pom.xml                         # Dependensi Maven (JavaFX 17, MySQL Connector, Tess4J, JUnit 5)
├── docker-compose.yml              # Container MySQL 8.0 & phpMyAdmin siap pakai
├── Dockerfile                      # Multi-stage build runner
├── README.md                       # Dokumentasi utama proyek
├── DOKUMENTASI_PEMBAGIAN_TUGAS.md  # Pembagian tugas 3 anggota & checklist FR
├── GIT_WORKFLOW.md                 # Panduan branching bebas konflik
├── database/
│   ├── schema.sql                  # DDL pembuatan 8 tabel relasional
│   ├── seed_data.sql               # Data awal skenario simulasi & indikator penipuan
│   ├── init.sql                    # Script inisialisasi otomatis Docker
│   └── README.md                   # Petunjuk setup database lokal & Docker
├── tessdata/
│   └── README.md                   # Petunjuk download model bahasa Tesseract
└── src/
    ├── main/
    │   ├── java/com/tipuskip/
    │   │   ├── MainApp.java        # Entry point aplikasi JavaFX
    │   │   ├── config/             # DatabaseConnection (JDBC Singleton)
    │   │   ├── model/              # 8 Entitas Data (Enkapsulasi PBO)
    │   │   ├── simulation/         # Abstraksi, Pewarisan, Polimorfisme Simulasi
    │   │   ├── dao/                # Data Access Object Pattern (Interface & Impl)
    │   │   ├── service/            # Business Logic & Service Factory
    │   │   ├── analyzer/           # OCR Engine, IndikatorMatcher, RiskEvaluator
    │   │   ├── controller/         # JavaFX Controllers (Pesan, Telepon, Analisis, Admin, Riwayat)
    │   │   └── util/               # Alert, Session, & File Validator
    │   └── resources/
    │       ├── application.properties
    │       └── com/tipuskip/
    │           ├── css/style.css   # Modern UI styling
    │           └── view/*.fxml     # Antarmuka FXML per fitur
    └── test/java/com/tipuskip/     # Unit tests untuk verifikasi mandiri
```

---

## 🚀 Cara Menjalankan Proyek

### 1. Prasyarat Sistem
- **Java JDK**: Versi 17 atau lebih tinggi (JDK 17/21/25).
- **Docker Desktop** (Sangat disarankan untuk database bersama) ATAU **XAMPP MySQL**.
- **IDE**: IntelliJ IDEA (Disarankan), VS Code (dengan Java Extension Pack), atau NetBeans.

---

### 2. Menjalankan Database dengan Docker (Paling Praktis)
Cukup jalankan satu perintah ini di terminal:
```bash
docker compose up -d
```
Database MySQL akan aktif di port `3306`, tabel langsung dibuat, dan data awal skenario & indikator terisi secara otomatis!
Anda juga dapat membuka phpMyAdmin di browser: `http://localhost:8085` (User: `tipuskip_user`, Password: `tipuskip_password`).

---

### 3. Menjalankan Aplikasi JavaFX

#### Melalui IntelliJ IDEA:
1. Buka folder `TipuSkip` di IntelliJ IDEA (`File -> Open`).
2. Tunggu IntelliJ mengunduh dependensi dari `pom.xml`.
3. Buka berkas `src/main/java/com/tipuskip/MainApp.java`.
4. Klik tombol **Run** hijau di sebelah metode `main`.

#### Melalui Terminal (Maven):
```bash
mvn javafx:run
```

---

## 🧪 Menjalankan Pengujian Unit Test

Setiap anggota dapat menjalankan unit test modulnya secara mandiri:
```bash
mvn test
```
- `SimulasiPesanTest`: Menguji prinsip PBO polimorfisme, alur branching, dan perhitungan skor simulasi.
- `RiskEvaluatorTest`: Menguji pencocokan pola regex dan kalkulasi ambang batas risiko.
