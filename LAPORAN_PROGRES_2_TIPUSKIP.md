# LAPORAN PROGRES KE-2
## MATA KULIAH PRAKTEK PEMROGRAMAN BERBASIS OBJEK (PBO)
### APLIKASI DESKTOP TIPUSKIP (EDUKASI PENCEGAHAN PENIPUAN DIGITAL)

---

**Dosen Pengampu:**  
**Yunia Ikawati S.ST, M.Tr.Kom.**

**Disusun Oleh (Kelompok TipuSkip):**
1. **M. Imamudin Sofiyullah** (NRP: 3125521035) — *UI/UX & Simulasi Interaktif (Anggota 1)*
2. **Tsaqib Fadhlurrahman Soka** (NRP: 3125521046) — *Basis Data & Panel Manajemen Admin (Anggota 2)*
3. **A’yuni Ikhtiara Mumtaz** (NRP: 3125521050) — *OCR Engine & Analisis Risiko (Anggota 3)*

**PROGRAM STUDI D3 TEKNIK INFORMATIKA PSDKU LAMONGAN**  
**DEPARTEMEN TEKNIK INFORMATIKA DAN KOMPUTER**  
**POLITEKNIK ELEKTRONIKA NEGERI SURABAYA (PENS)**  
**2026**

---

## 1. Ringkasan Perkembangan Progres (Progres 1 vs Progres 2)

Pada Laporan Progres ke-1, pengembangan aplikasi TipuSkip berada pada kisaran **65% – 70%** yang berfokus pada penyusunan antarmuka dasar JavaFX, perancangan FXML navigasi, dan perancangan awal skema tabel basis data MySQL di mana koneksi dinamis JDBC belum terintegrasi penuh.

Pada **Progres ke-2**, pengerjaan sistem secara menyeluruh telah mencapai **92% – 95%**. Seluruh arsitektur inti telah terhubung secara end-to-end:
1. **Koneksi Database & Hybrid Fallback**: Integrasi penuh koneksi JDBC Singleton dan Generic DAO Pattern ke MySQL, dilengkapi sistem *Auto-Fallback Mock Engine* sehingga aplikasi tetap dapat berjalan secara mandiri (*standalone/offline*) tanpa crash ketika MySQL server tidak aktif.
2. **Penyempurnaan UI Tingkat Lanjut**: Implementasi *Custom Integrated Title Bar* (menghilangkan bingkai bawaan sistem operasi agar menyatu modern dengan bodi aplikasi).
3. **Desain Adaptif & Responsif**: Penyesuaian otomatis dimensi jendela terhadap ukuran layar laptop (*Screen-Aware Responsive Layout*) agar pas di tengah layar dan tidak tertutup taskbar Windows.
4. **Multi-Platform Theme Engine**: Penambahan fitur penggantian tema antarmuka media sosial secara dinamis (WhatsApp Business, Instagram DM, Facebook Messenger, dan TikTok Direct Chat) baik untuk simulasi pesan maupun panggilan telepon.
5. **Fitur Pengiriman & Pemutar Video Interaktif**: Simulasi pengiriman bukti video (modus *task scam* like/subscribe vs *selfie* KTP) dengan pemutar video modal interaktif.
6. **Perluasan Skenario Edukatif**: Penambahan naskah cabang interaktif dari 1 skenario awal menjadi **8 skenario komprehensif** (5 skenario chat dan 3 skenario telepon).
7. **Pengujian Unit Otomatis (Unit Testing)**: 16 dari 16 unit test berhasil lolos 100% memverifikasi integritas logika OOP (Polimorfisme, Enkapsulasi, dan Abstraksi).

### Tabel Perbandingan Pencapaian Progres

| Komponen / Modul | Status pada Progres 1 | Status pada Progres 2 (Terkini) |
| :--- | :--- | :--- |
| **Persentase Total** | 65% – 70% | **92% – 95% (Hampir Final)** |
| **Bingkai Jendela Aplikasi** | Bingkai bawaan Windows (Default OS Frame) | **Custom Integrated Title Bar (Undecorated Stage, Draggable, Window Controls Kustom)** |
| **Dimensi Layar** | Kaku (*hardcoded* 1100x750 px, rawan terpotong di laptop 1366x768) | **Responsif Dinamis (`Screen.getPrimary().getVisualBounds()`, otomatis *center on screen*)** |
| **Tema Antarmuka Chat** | 1 Gaya (WhatsApp Standar) | **Multi-Platform Theme Switcher: WhatsApp, Instagram DM, Messenger, & TikTok** |
| **Interaksi Multimedia** | Teks dan lampiran statis sederhana | **Pengiriman Video Bukti Tugas & Pemutar Video Dialog Interaktif (`Timeline` Animation)** |
| **Koleksi Skenario** | 1 Skenario Mock awal | **8 Skenario Lengkap (5 Chat + 3 Telepon) dengan Pohon Keputusan (*Decision Tree*)** |
| **Koneksi Basis Data** | Desain skema & lingkungan instalasi awal | **Terintegrasi Penuh (Generic DAO Pattern + JDBC Singleton + Hybrid Auto-Fallback)** |
| **Launcher Desktop** | Dijalankan via terminal IDE | **`Jalankan_TipuSkip.bat` (Auto-Detect JAVA_HOME), `TipuSkip.vbs`, Shortcut `.lnk` & Ikon Multi-Resolusi** |
| **Unit Testing (PBO)** | Pengujian manual parsial | **16 Unit Test Otomatis Lolos 100% (Maven Surefire & JUnit 5)** |

---

## 2. Rincian Hasil Implementasi Fitur Baru & Bukti Screenshot (Fokus Progres 2)

### 2.1. Integrasi Basis Data MySQL & Penyimpanan Data Dinamis
Pada Progres ke-2, basis data MySQL telah terhubung penuh ke antarmuka aplikasi menggunakan arsitektur JDBC Singleton dan Generic DAO Pattern. Seluruh entitas (pengguna, skenario, langkah percakapan, pilihan balasan, riwayat evaluasi, dan indikator penipuan) telah tersimpan secara persisten di database.

> 📷 **[TEMPATKAN SCREENSHOT DATABASE DI SINI]**
> *(Sisipkan tangkapan layar antarmuka phpMyAdmin / MySQL Workbench / DBeaver yang menampilkan struktur relasi tabel database TipuSkip atau isi data dari tabel `skenario`, `langkah_skenario`, dan `hasil_simulasi`).*
> 
> **Gambar 2.1**: Struktur Skema Relasional dan Bukti Data Tabel Basis Data MySQL TipuSkip.

---

### 2.2. Custom Integrated Title Bar (Bebas Bingkai Bawaan OS)
Untuk menghadirkan estetika aplikasi modern setara perangkat lunak profesional (seperti Spotify, Discord, atau VS Code), jendela aplikasi tidak lagi menggunakan bingkai abu-abu kaku bawaan sistem operasi Windows.
* **Mekanisme PBO**: Menggunakan `primaryStage.initStyle(StageStyle.UNDECORATED)` pada `MainApp.java`.
* **Integrated Header Bar**: Area judul atas menyatu langsung dengan bodi utama dengan palet warna `#0b1120`, memuat logo TipuSkip, identitas versi, serta *pill badge* keamanan.
* **Tombol Kontrol Jendela Kustom**:
  * **Minimize (`—`)**: Menyembunyikan jendela ke taskbar (`stage.setIconified(true)`).
  * **Maximize / Restore (`▢` / `❐`)**: Mengubah ukuran ke layar penuh atau memulihkan ukuran semula secara toggle (`stage.setMaximized(!stage.isMaximized())`).
  * **Close (`✕`)**: Tombol silang dengan animasi *hover* merah krisan (`#e11d48`) yang terhubung ke dialog konfirmasi keluar aman.
* **Interaktivitas Draggable & Resizing**:
  * Pengguna dapat mengklik dan menggeser (*drag*) bagian header di mana saja untuk memindahkan posisi aplikasi di layar.
  * *Double-click* pada title bar akan otomatis memaksimalkan/memulihkan ukuran jendela.
  * Dibuatkan utility [`WindowResizeHelper.java`](file:///C:/Users/SFI%20PC/.gemini/antigravity/scratch/TipuSkip/src/main/java/com/tipuskip/util/WindowResizeHelper.java) yang mendeteksi kursor di tepi dan sudut jendela sehingga jendela tetap dapat ditarik (*resize*) dari segala arah.

> 📷 **[TEMPATKAN SCREENSHOT CUSTOM TITLE BAR & JENDELA UTAMA DI SINI]**
> *(Sisipkan tangkapan layar jendela utama aplikasi yang memperlihatkan Title Bar terintegrasi menyatu dengan tombol minimize, maximize, dan close kustom).*
> 
> **Gambar 2.2**: Tampilan Antarmuka Aplikasi Desktop TipuSkip dengan Custom Integrated Title Bar & Tata Letak Responsif.

---

### 2.3. Multi-Platform Theme Engine pada Simulasi Pesan
Fitur ini dirancang karena modus kejahatan siber saat ini tidak hanya terjadi di WhatsApp, melainkan tersebar luas di berbagai media sosial populer. Disediakan menu *dropdown* pemilih platform secara *real-time*:
1. **🟢 WhatsApp Business (Emerald Aesthetic)**:
   * Header warna hijau tua (`#075e54`), status *Online*, lencana enkripsi end-to-end.
   * Balon percakapan putih dengan tanda centang ganda biru (`✓✓`).
2. **📸 Instagram DM (Direct Message)**:
   * Header modern dark OLED (`#121212`), nama pengguna berformat `@akun_penipuan` dilengkapi lencana centang biru (*verified*).
   * Avatar pengirim berbingkai lingkaran gradien pelangi (*story ring*).
   * Balon pengirim dilengkapi **stiker reaksi emoji hati (`❤️ 1`)**, dan balon balasan pengguna menggunakan **gradien sunset Instagram (`linear-gradient` ungu-merah-oranye)**.
3. **💬 Facebook Messenger**:
   * Header putih bersih dengan teks hitam kontras, avatar bundar, dan indikator status *Active 5m ago*.
   * Balon balasan pengguna menggunakan **gradien biru elektrik khas Messenger (`#0084FF`)** dengan status *Terkirim/Dibaca*.
4. **🎵 TikTok Direct Chat**:
   * Nuansa *cyber dark* (`#000000`), status pengikut 1.2M, dan tombol audio call.
   * Balon pengirim memiliki garis aksen neon cyan (`#25f4ee`) dan balasan pengguna berwarna merah-pink khas TikTok (`#fe2c55`).

> 📷 **[TEMPATKAN SCREENSHOT PERBANDINGAN TEMA MEDIA SOSIAL DI SINI]**
> *(Sisipkan tangkapan layar simulasi pesan saat menggunakan tema Instagram DM / TikTok / Messenger yang memperlihatkan balon gradien dan reaksi emoji).*
> 
> **Gambar 2.3**: Implementasi Multi-Platform Theme Engine pada Simulasi Pesan (Mode Instagram DM & TikTok).

---

### 2.4. Fitur Pengiriman & Pemutar Video Interaktif (*Video Task Scam Simulation*)
Untuk mengedukasi modus penipuan berkedok pekerjaan paruh waktu (*freelance task scam* like video) dan pemerasan video identitas (*biometrik selfie* KTP):
1. Pengguna dapat menekan tombol **"🎥 Kirim Bukti Video"** untuk memilih rekaman yang akan dikirim ke penipu:
   * *Opsi Aman/Peringatan*: Mengirimkan rekaman bukti like video YouTube (sistem memberi umpan balik mengenai bahaya jebakan komisi awal).
   * *Opsi Fatal*: Mengirimkan rekaman selfie wajah memegang KTP asli (sistem langsung memicu status skor **🚨 BERISIKO (10/100)** dan peringatan bahaya pembobolan pinjaman online ilegal).
2. Disediakan **Modal Video Player Dialog** yang mensimulasikan pemutaran video cuplikan tugas resmi menggunakan animasi `Timeline` JavaFX, tombol kontrol *Play/Pause/Restart*, progress bar durasi, serta analisis edukatif di bagian bawah.

> 📷 **[TEMPATKAN SCREENSHOT FITUR VIDEO & PEMUTAR VIDEO DI SINI]**
> *(Sisipkan tangkapan layar saat dialog pemutar video terbuka di atas obrolan simulasi).*
> 
> **Gambar 2.4**: Fitur Pengiriman Video Tugas dan Modal Pemutar Video Interaktif (*Video Task Player*).

---

### 2.5. Multi-Platform Calling Screen pada Simulasi Telepon & Smart Keypad Dialer
Simulasi panggilan telepon suara kini juga dilengkapi pemilih tema platform panggilan masuk:
* **📱 Telepon Seluler (GSM / Truecaller)**: Antarmuka smartphone dengan peringatan spam komunitas (*Truecaller-style scam warning*).
* **🟢 Panggilan Suara WhatsApp**: Antarmuka panggilan suara WhatsApp lengkap dengan wallpaper hijau tua dan status enkripsi.
* **📸 Panggilan Audio Instagram**: Antarmuka panggilan masuk audio Instagram dengan bingkai avatar berputar gradien.
* **💬 Panggilan Suara Messenger**: Layar panggilan bernuansa gradien biru Messenger.
* **🎵 Panggilan Langsung TikTok**: Layar panggilan audio TikTok Live.

> 📷 **[TEMPATKAN SCREENSHOT SIMULASI TELEPON DI SINI]**
> *(Sisipkan tangkapan layar antarmuka panggilan masuk dengan badge peringatan penipuan dan keypad dialer DTMF).*
> 
> **Gambar 2.5**: Tampilan Simulasi Panggilan Telepon Suara Multi-Platform dan Keypad Dialer Interaktif.

### 2.6. Perluasan 8 Skenario Lengkap Berbasis Pohon Keputusan (*Decision Tree*)
Tidak lagi hanya 1 skenario, sistem kini memuat 8 skenario komprehensif:
* **Skenario Pesan (5 Kasus)**:
  1. *Pesan 1*: Tawaran Kerja Freelance Like Video YouTube (Tingkat: MUDAH).
  2. *Pesan 2*: Pengiriman Berkas APK Berkedok Foto Paket Rusak Ekspedisi (Tingkat: MUDAH).
  3. *Pesan 3*: Phishing Undian Hadiah Mengatasnamakan E-Commerce Terbesar (Tingkat: SEDANG).
  4. *Pesan 4*: Konfirmasi Tilang Elektronik ETLE Melalui Surat Dokumen APK (Tingkat: SEDANG).
  5. *Pesan 5*: Notifikasi SMS Perubahan Tarif Transaksi Bank & Tautan Palsu (Tingkat: SULIT).
* **Skenario Telepon (3 Kasus)**:
  1. *Telepon 1*: Panggilan Darurat Bank Pemblokiran Kartu ATM & Manipulasi Kode OTP (Tingkat: SEDANG).
  2. *Telepon 2*: Panggilan Undian Berhadiah Mengatasnamakan Operator Seluler (Tingkat: MUDAH).
  3. *Telepon 3*: Ancaman Paket Narkoba dari Oknum Mengaku Polisi & Jaksa Gadungan (Tingkat: SULIT).

---

## 3. Penerapan Prinsip Pemrograman Berorientasi Objek (PBO)

Arsitektur aplikasi TipuSkip dibangun dengan mematuhi pilar-pilar OOP:

### 3.1. Polimorfisme (Polymorphism)
* **Hirarki Simulasi**: Kelas abstrak `Simulasi` memiliki method abstrak `inisialisasi()`, `prosesRespons()`, `hitungSkor()`, dan `evaluasiHasil()`. Method ini di-override secara polimorfis oleh `SimulasiPesan` (menangani interaksi balon teks, lampiran file APK, dan video) dan `SimulasiTelepon` (menangani state timer panggilan, dering, dan nada DTMF keypad).
* **Ekstraksi Teks (OCR)**: Interface `ITextExtractor` memiliki dua implementasi polimorfik, yaitu `TesseractTextExtractor` (menggunakan engine Tess4J asli) dan `MockTextExtractor` (untuk pengujian otomatis lingkungan CI/CD tanpa ketergantungan library binary native).

### 3.2. Enkapsulasi (Encapsulation)
* Seluruh atribut data pada model (`Pengguna`, `Skenario`, `LangkahSkenario`, `PilihanRespons`, `HasilSimulasi`, `IndikatorPenipuan`) dideklarasikan `private`.
* Akses data dikontrol penuh melalui getter, setter, dan metode validasi internal sehingga mencegah manipulasi state secara langsung dari luar kelas.

### 3.3. Abstraksi (Abstraction) & Desain Arsitektur
* **Generic DAO Pattern**: Menyediakan interface generik `GenericDAO<T, ID>` dengan method standar CRUD (`findById`, `findAll`, `save`, `update`, `delete`), yang diimplementasikan secara spesifik oleh DAO masing-masing entitas melalui JDBC.
* **Service Layer & Factory Pattern**: Antarmuka grafis controller tidak berhubungan langsung dengan DAO, melainkan melalui `ISimulasiService`, `IAnalisisService`, dan `IAdminService` yang diinstansiasi secara terpusat oleh `ServiceFactory`.
* **Singleton Pattern**: Koneksi database MySQL diatur melalui `DatabaseConnection` yang menjamin hanya ada satu instance koneksi aktif dalam siklus hidup aplikasi.

---

## 4. Pengujian Sistem & Verifikasi (Testing)

Pengujian perangkat lunak dilakukan secara terotomatisasi menggunakan framework **JUnit 5** dan **Maven Surefire Plugin**.

### 4.1. Hasil Unit Testing Otomatis
Seluruh 16 unit test berhasil dijalankan dengan hasil **100% SUCCESS (0 Failure, 0 Error)**:

```text
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running com.tipuskip.analyzer.RiskEvaluatorTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.232 s
Running com.tipuskip.dao.ModelAndDaoTest
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.060 s
Running com.tipuskip.simulation.SimulasiPesanTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.013 s
Running com.tipuskip.simulation.SimulasiTeleponTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.006 s

Results:
Tests run: 16, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### 4.2. Pengujian Portabilitas Antar Perangkat & JDK
Aplikasi telah diuji dan berhasil dijalankan secara identik di perangkat laptop anggota lain:
* Berjalan mulus pada **Java SE 17** (target kompilasi baseline) hingga **Java SE 26 (Oracle JDK 26.0.2.1)** berkat prinsip *backward compatibility*.
* Skrip peluncur `Jalankan_TipuSkip.bat` dan wrapper `mvnw.cmd` berhasil mengotomatiskan pencarian path `JAVA_HOME` tanpa mengharuskan pengguna mengatur variabel lingkungan Windows secara manual.

---

## 5. Pembagian Kerja Tim & Kontribusi Anggota

| Nama Anggota | NRP | Peran & Tanggung Jawab Utama | Kontribusi Spesifik pada Progres 2 |
| :--- | :--- | :--- | :--- |
| **M. Imamudin Sofiyullah** *(Anggota 1)* | 3125521035 | UI/UX Design, Simulasi Interaktif & Modul Edukasi | • Mengembangkan *Custom Integrated Title Bar* (Undecorated Window & Draggable).<br>• Mengembangkan *Multi-Platform Theme Engine* (WhatsApp, Instagram, Messenger, TikTok).<br>• Mengimplementasikan fitur pengiriman video interaktif dan dialog pemutar video.<br>• Menyusun 8 naskah skenario percakapan dan panggilan telepon. |
| **Tsaqib Fadhlurrahman Soka** *(Anggota 2)* | 3125521046 | Basis Data MySQL, JDBC Singleton, Generic DAO & Admin Panel | • Menyelesaikan implementasi 8 DAO konkret berbasis JDBC dan SQL schema.<br>• Mengembangkan mekanisme *Hybrid Fallback Mock Engine* saat MySQL offline.<br>• Menghubungkan penyimpanan hasil riwayat skor ke database.<br>• Mengintegrasikan Admin Panel untuk CRUD skenario dinamis. |
| **A’yuni Ikhtiara Mumtaz** *(Anggota 3)* | 3125521050 | Analisis Tangkapan Layar, OCR Engine, & Algoritma Risiko | • Mengembangkan integrasi OCR Tess4J untuk ekstraksi teks gambar tangkapan layar.<br>• Mengimplementasikan algoritma `IndikatorMatcher` berbasis regex dan kata kunci bobot bahaya.<br>• Menyusun engine evaluasi skor `RiskEvaluator` (Tingkat Risiko: Rendah, Sedang, Tinggi, Kritis). |

---

## 6. Rencana Tahap Selanjutnya (Menuju Laporan Akhir 100%)

1. **Penyempurnaan Akhir Visual**: Menambahkan animasi transisi halus antar-halaman (*fade transition*) dan penambahan data seed skenario tambahan.
2. **Pengemasan Eksekusi Mandiri (*Self-Contained Executable*)**: Melakukan packaging menggunakan `jpackage` atau installer `.exe` native Windows agar aplikasi dapat langsung dijalankan oleh masyarakat awam tanpa perlu instalasi JDK atau Maven terlebih dahulu.
3. **Penyusunan Laporan Akhir & Video Demo Aplikasi**: Membuat dokumentasi laporan akhir lengkap beserta rekaman video peragaan sistem untuk presentasi evaluasi tugas akhir mata kuliah Praktek Pemrograman Berbasis Objek.
