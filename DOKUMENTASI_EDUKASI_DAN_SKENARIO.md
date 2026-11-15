# 📖 Dokumentasi Naskah Skenario Simulasi & Modul Edukasi (TipuSkip)
**Fokus Tanggung Jawab:** Anggota 1 (UI/UX & Simulasi Interaktif)  
**Kebutuhan Fungsional (PRD):** FR-01, FR-02, FR-03, FR-04, FR-05, FR-11  

---

## 📌 Daftar Isi
1. [Ringkasan Peran & Batasan Modul](#1-ringkasan-peran--batasan-modul)
2. [Penerapan Konsep Pemrograman Berorientasi Objek (PBO)](#2-penerapan-konsep-pemrograman-berorientasi-objek-pbo)
3. [Naskah Skenario Simulasi Pesan (Chat Simulation)](#3-naskah-skenario-simulasi-pesan-chat-simulation)
4. [Naskah Skenario Simulasi Telepon (Phone & Dialer Simulation)](#4-naskah-skenario-simulasi-telepon-phone--dialer-simulation)
5. [Struktur Alur Percakapan & Pohon Keputusan (Branching Tree)](#5-struktur-alur-percakapan--pohon-keputusan-branching-tree)
6. [Materi Modul Edukasi Pencegahan Penipuan Digital (FR-11)](#6-materi-modul-edukasi-pencegahan-penipuan-digital-fr-11)
7. [Panduan Pengujian Mandiri (Unit Testing)](#7-panduan-pengujian-mandiri-unit-testing)

---

## 1. Ringkasan Peran & Batasan Modul

Sebagai **Anggota 1**, fokus pengerjaan mencakup perancangan pengalaman pengguna (UI/UX), penyusunan naskah percakapan penipuan realistis, pembuatan logika alur simulasi berbasis OOP, serta penyusunan modul edukasi pengguna:

| Modul / Komponen | Berkas Terkait | Deskripsi Implementasi |
|---|---|---|
| **Antarmuka Utama & Tab** | `main.fxml`, `MainController.java` | Navigasi atas yang responsif dengan penanda tombol aktif dan pemuatan view modular. |
| **Simulasi Pesan Interaktif** | `simulasi_pesan.fxml`, `SimulasiPesanController.java` | Antarmuka gaya chat instan WhatsApp dengan gelembung pesan masuk/keluar, pratinjau lampiran berbahaya, deteksi link, serta panel evaluasi langsung. |
| **Simulasi Telepon & Dialer** | `simulasi_telepon.fxml`, `SimulasiTeleponController.java` | Tampilan smartphone lengkap dengan Caller ID, lencana peringatan spam (Truecaller style), pengatur waktu durasi live, serta tombol dialer keypad interaktif (0-9, *, #, DTMF). |
| **Modul Edukasi (FR-11)** | `edukasi.fxml`, `EdukasiController.java` | Katalog 6 modus terpopuler di Indonesia, 5 sinyal bahaya (red flags), kamus kejahatan siber, dan nomor kontak darurat resmi. |
| **Styling CSS Modern** | `src/main/resources/com/tipuskip/css/style.css` | Tata letak rapi, palet warna berstandar Tailwind UI, bayangan lembut, dan penanda status risiko. |

---

## 2. Penerapan Konsep Pemrograman Berorientasi Objek (PBO)

### 2.1. Abstraksi (*Abstraction*)
- Diterapkan pada kelas abstrak `com.tipuskip.simulation.Simulasi`.
- Kelas ini menyediakan kerangka acuan dan kontrak baku untuk segala jenis simulasi interaktif:
  ```java
  public abstract class Simulasi {
      public abstract void jalankan();
      public abstract boolean prosesRespons(PilihanRespons respons);
      public abstract int hitungSkor();
      public abstract String evaluasiHasil();
      public abstract String dapatkanRingkasanPembelajaran();
  }
  ```

### 2.2. Pewarisan (*Inheritance*)
- `SimulasiPesan` dan `SimulasiTelepon` mewarisi properti dan metode bersama dari kelas `Simulasi`:
  - `indeksLangkahSaatIni`, `skorTerkumpul`, `riwayatResponsPengguna`, `waktuMulai`, `waktuSelesai`.
  - Mengurangi duplikasi kode (*code reuse*) pada manajemen langkah dan pembentukan riwayat hasil.

### 2.3. Polimorfisme (*Polymorphism*)
- Metode `prosesRespons()` dan `hitungSkor()` memiliki perilaku berbeda (*dynamic polymorphism*):
  - Pada `SimulasiPesan`: Menghitung persentase respon aman dan melacak interaksi pembukaan tautan/file .APK berbahaya.
  - Pada `SimulasiTelepon`: Mendeteksi apakah korban membocorkan kode OTP (memberikan penalti skor maksimal 40) atau berhasil melakukan verifikasi mandiri.

### 2.4. Enkapsulasi (*Encapsulation*)
- Seluruh model data (`Skenario`, `LangkahSkenario`, `PilihanRespons`) menerapkan variabel `private` dengan akses ketat melalui `getter` dan `setter`, serta validasi koleksi data null.

---

## 3. Naskah Skenario Simulasi Pesan (Chat Simulation)

### Skenario 1: Tawaran Kerja Freelance Like Video YouTube (Task Scam)
- **Tingkat Kesulitan:** MUDAH | **Kategori:** Lowongan Kerja Palsu
- **Latar Belakang:** Penipu menawarkan komisi instan hanya dengan menonton/menyukai video, namun berujung pada keharusan menyetor deposit uang jaminan.
- **Langkah 1:**
  - *Pengirim:* Recruiter Sarah (PT Digital Mandiri)
  - *Pesan:* "Halo Kak! Kami dari HRD PT Digital Mandiri. Sedang membuka lowongan freelance paruh waktu tugas ringan: cukup Like 3 video YouTube dapat gaji Rp150.000/hari. Apakah Kakak berminat?"
  - *Pilihan Respons:*
    1. *(AMAN - 100 Poin)* "Halo, dari mana Anda mendapatkan nomor saya? Perusahaan Anda tidak pernah saya lamar."  
       *Konsekuensi:* Penipu menyadari Anda waspada dan tidak mudah dimanipulasi.
    2. *(BAHAYA - 0 Poin)* "Wah menarik sekali! Mau dong Kak, bagaimana cara daftarnya?"  
       *Konsekuensi:* Anda langsung masuk perangkap skema deposit.
    3. *(WASPADA - 70 Poin)* "Bisa kirimkan surat penawaran resmi atau website perusahaan terlebih dahulu?"  
       *Konsekuensi:* Penipu akan mencoba memberikan link profil perusahaan fiktif.
- **Langkah 2:**
  - *Pesan:* "Bagus sekali! Ini link tugas pertamanya: https://s.id/job-like-yt-bonus. Silakan screenshot setelah Like dan kirimkan ke kami ya. Setelah itu kami masukkan ke grup Telegram untuk pencairan bonus."
  - *Pilihan Respons:*
    1. *(AMAN - 100 Poin)* "Maaf, saya tidak membuka tautan singkat (shortlink) dari orang tidak dikenal."
    2. *(BAHAYA - 0 Poin)* "Oke saya klik linknya dan saya like videonya sekarang ya kak."
- **Langkah 3:**
  - *Pengirim:* Admin Grup VIP (Toko Invest Global)
  - *Pesan:* "Selamat bonus Rp30.000 pertama berhasil cair! Untuk tugas tahap VIP selanjutnya dengan komisi Rp1.500.000, Kakak wajib deposit awal Rp300.000 ke rekening bendahara kami sekarang. Slot terbatas 10 menit!"
  - *Pilihan Respons:*
    1. *(AMAN - 100 Poin)* "Pekerjaan yang meminta uang deposit adalah penipuan! Saya hentikan dan laporkan nomor ini."
    2. *(BAHAYA - 0 Poin)* "Baik saya transfer Rp300.000 sekarang karena komisinya besar."

---

### Skenario 2: Kurir Ekspedisi: Konfirmasi Foto Paket Rusak (.APK)
- **Tingkat Kesulitan:** SEDANG | **Kategori:** Malware / APK Sniffer
- **Latar Belakang:** Penipu mengirim file berekstensi .APK menyamar sebagai foto paket untuk menyadap SMS OTP perbankan.
- **Langkah 1:**
  - *Pengirim:* Kurir JNT Express - Mas Agus
  - *Pesan:* "Selamat siang kak, saya kurir ekspedisi mau antar paket. Tapi kardus paketnya robek parah saat perjalanan. Tolong cek foto kerusakannya ya kak: [Lihat_Foto_Paket.apk]"
  - *Pilihan Respons:*
    1. *(AMAN - 100 Poin)* "Kenapa fotonya berekstensi .apk? Saya tidak akan install file aplikasi dari orang asing."
    2. *(BAHAYA - 0 Poin)* "Waduh rusak ya? Oke langsung saya unduh dan pasang aplikasinya biar bisa lihat fotonya."
- **Langkah 2:**
  - *Pesan:* "Iya kak itu aplikasi sistem komplain resmi ekspedisi kami. Kalau tidak di-install sekarang paket akan kami retur hangus ke gudang pusat lho!"
  - *Pilihan Respons:*
    1. *(AMAN - 100 Poin)* "Saya akan cek nomor resi paket saya langsung melalui aplikasi resmi J&T Express di Google Play Store."
    2. *(BAHAYA - 0 Poin)* "Ya sudah saya izinkan akses install dari sumber tak dikenal (unknown sources)."

---

### Skenario 3: Surat Tilang Elektronik (ETLE) Palsu Kepolisian
- **Tingkat Kesulitan:** SULIT | **Kategori:** Malware / Impersonasi Instansi
- **Langkah 1:**
  - *Pengirim:* KORLANTAS POLRI RESMI
  - *Pesan:* "PEMBERITAHUAN TILANG ELEKTRONIK: Kendaraan Anda tertangkap kamera ETLE melanggar batas kecepatan. Silakan unduh Surat_Pemberitahuan_ETLE.apk untuk melihat foto bukti dan nomor bayar tilang dalam 24 jam sebelum STNK diblokir."
  - *Pilihan Respons:*
    1. *(AMAN - 100 Poin)* "Kepolisian RI tidak pernah mengirim surat tilang lewat WhatsApp atau berkas .apk. Saya akan cek di situs etle-korlantas.info."
    2. *(BAHAYA - 0 Poin)* "Takut STNK diblokir, saya langsung unduh dan buka Surat_Pemberitahuan_ETLE.apk."
    3. *(WASPADA - 40 Poin)* "Pak polisi, saya melanggar di mana ya? Bisa damai di tempat saja?"

---

### Skenario 4: Pemenang Undian Belanja Marketplace Rp10.000.000
- **Tingkat Kesulitan:** MUDAH | **Kategori:** Phishing Website
- **Langkah 1:**
  - *Pengirim:* Shopee Care Promo
  - *Pesan:* "Selamat! Nomor WhatsApp Anda terpilih mendapatkan Grand Prize Rp10.000.000 dari Gebyar Promo Berkah. Silakan klaim hadiah Anda melalui portal resmi: https://shopee-klaim-hadiah2026.online sebelum pukul 23:59!"
  - *Pilihan Respons:*
    1. *(AMAN - 100 Poin)* "Saya tidak pernah mengikuti undian ini. Domain web mencurigakan (bukan shopee.co.id). Ini penipuan!"
    2. *(BAHAYA - 0 Poin)* "Alhamdulillah! Saya klik linknya dan masukkan password serta nomor telepon untuk klaim."

---

## 4. Naskah Skenario Simulasi Telepon (Phone & Dialer Simulation)

### Skenario 5: Panggilan Darurat Bank: Pemblokiran Rekening & Minta OTP
- **Tingkat Kesulitan:** SEDANG | **Kategori:** Impersonasi Bank / Social Engineering
- **Langkah 1:**
  - *Penelepon:* Panggilan Masuk: +62-812-9988-7711 (Mengaku HaloBCA)
  - *Ucapan:* "Selamat siang Bapak/Ibu. Kami mendeteksi adanya transaksi mencurigakan senilai Rp7.500.000 di toko online luar negeri dari kartu debit Anda. Apakah benar Anda yang melakukan transaksi tersebut?"
  - *Pilihan Respons:*
    1. *(AMAN - 100 Poin)* "Saya akan matikan panggilan ini dan menghubungi call center resmi HaloBCA di 1500888 secara mandiri."
    2. *(WASPADA - 50 Poin)* "Tidak benar! Saya tidak pernah belanja itu! Tolong batalkan sekarang juga Pak!"
- **Langkah 2:**
  - *Ucapan:* "Baik untuk membatalkan transaksi dan mengamankan rekening, sistem kami baru saja mengirim 6 digit kode pembatalan ke SMS Anda. Tolong sebutkan kodenya sekarang juga agar uang tidak terpotong!"
  - *Pilihan Respons:*
    1. *(AMAN - 100 Poin)* "SMS yang masuk tertulis 'KODE OTP RAHASIA, JANGAN BERIKAN KEPADA SIAPAPUN TERMASUK PIHAK BANK'. Saya tidak akan sebutkan!"
    2. *(BAHAYA - 0 Poin)* "Kodenya 894321, tolong segera dibatalkan transaksinya ya Pak!"

---

### Skenario 6: Perubahan Tarif Biaya Transaksi Bank Jadi Rp150.000/Bulan
- **Tingkat Kesulitan:** SEDANG | **Kategori:** Manipulasi Biaya Administrasi
- **Langkah 1:**
  - *Penelepon:* Customer Care BRI (+62-821-4455-6677)
  - *Ucapan:* "Selamat pagi Nasabah Terhormat, kami menginformasikan per malam ini ada pembaruan tarif transaksi antarbank menjadi Rp150.000 per bulan yang didebet otomatis. Apakah Bapak/Ibu setuju dengan kenaikan tarif ini?"
  - *Pilihan Respons:*
    1. *(WASPADA - 50 Poin)* "Saya tidak setuju! Kenapa mahal sekali? Batalkan perubahan tarif rekening saya!"
    2. *(AMAN - 100 Poin)* "Setahu saya kebijakan bank diumumkan via website resmi. Saya akan tanyakan langsung ke kantor cabang."
- **Langkah 2:**
  - *Ucapan:* "Baik jika tidak setuju, kami bantu batalkan. Kami kirimkan tautan form pembatalan via SMS ya. Nanti silakan masukkan nomor kartu ATM, masa berlaku, dan 3 digit CVV di belakang kartu."
  - *Pilihan Respons:*
    1. *(AMAN - 100 Poin)* "3 digit CVV di belakang kartu dan nomor kartu ATM adalah data rahasia perbankan! Anda penipu!"
    2. *(BAHAYA - 0 Poin)* "Baik saya isi formnya lengkap dengan nomor kartu dan CVV-nya."

---

### Skenario 7: Impersonasi Kerabat Mengalami Musibah / Kecelakaan
- **Tingkat Kesulitan:** SULIT | **Kategori:** Social Engineering Emosional
- **Langkah 1:**
  - *Penelepon:* Petugas Rumah Sakit / Polisi (+62-813-7788-9900)
  - *Ucapan:* "Halo! Ini dengan keluarga Andi? Andi baru saja mengalami kecelakaan parah dan pendarahan hebat di jalan raya! Kami butuh uang operasi darurat Rp5.000.000 sekarang ke rekening dokter sebelum terlambat!"
  - *Pilihan Respons:*
    1. *(AMAN - 100 Poin)* "Tolong sebutkan nama rumah sakitnya. Saya akan telepon nomor Andi sendiri dan minta kerabat dekat mendatangi rumah sakit tersebut."
    2. *(BAHAYA - 0 Poin)* "Ya Tuhan Andi kecelakaan?! Tolong selamatkan dia, kirim nomor rekeningnya saya transfer uangnya sekarang!"

---

## 5. Struktur Alur Percakapan & Pohon Keputusan (Branching Tree)

```
[Mulai Simulasi]
       │
       ▼
[Langkah 1: Kontak Awal]
  ├─ Pilihan A (AMAN): Skor +100 ────► [Lanjut Langkah 2 atau Akhiri Aman]
  ├─ Pilihan B (WASPADA): Skor +50-70 ► [Lanjut Langkah 2 (Penipu Menekan)]
  └─ Pilihan C (BAHAYA): Skor 0 ──────► [Lanjut Langkah 2 (Masuk Jebakan)]
                                                │
                                                ▼
                                    [Langkah 2: Eksploitasi Inti]
                                      (Minta Deposit / File APK / Minta OTP)
                                      ├─ AMAN: Evaluasi "LULUS_AMAN"
                                      └─ BAHAYA: Evaluasi "BERISIKO_TINGGI"
```

---

## 6. Materi Modul Edukasi Pencegahan Penipuan Digital (FR-11)

### 6.1. 5 Prinsip Emas Anti-Scam:
1. **Waspada OTP/PIN/CVV:** Tidak ada pihak resmi mana pun yang meminta data ini.
2. **Periksa Ekstensi Berkas:** Jangan buka file `.apk` yang dikirim orang tak dikenal melalui WhatsApp.
3. **Cek Alamat Situs Web (URL):** Bedakan situs resmi instansi dengan situs gratisan/kloningan.
4. **Verifikasi Jalur Mandiri:** Tutup panggilan mencurigakan dan hubungi nomor resmi instansi secara mandiri.
5. **Tolak Tawaran "Terlalu Indah untuk Jadi Nyata":** Waspadai komisi fantastis dari pekerjaan mudah atau hadiah undian tanpa pendaftaran.

### 6.2. Kanal Layanan Pengaduan Resmi Indonesia:
- **OJK (Otoritas Jasa Keuangan):** Telepon `157` / WhatsApp `081-157-157-157`
- **Patroli Siber Bareskrim Polri:** `patrolisiber.id`
- **Cek Rekening Kominfo:** `cekrekening.id`
- **Aduan Konten Kominfo:** `aduankonten.id`
- **HaloBCA Resmi:** `1500888`
- **Mandiri Call Resmi:** `14000`

---

## 7. Panduan Pengujian Mandiri (Unit Testing)

Setiap anggota dapat memverifikasi modul simulasi tanpa perlu menyalakan database MySQL:
```bash
# Menjalankan seluruh pengujian unit
mvn test

# Menjalankan pengujian khusus Simulasi Pesan
mvn test -Dtest=SimulasiPesanTest

# Menjalankan pengujian khusus Simulasi Telepon
mvn test -Dtest=SimulasiTeleponTest
```

Pengujian memastikan bahwa:
- Percakapan bercabang (*branching logic*) berpindah langkah dengan tepat.
- Pilihan respons pengguna mengakumulasi skor secara adil.
- Status evaluasi (`LULUS_AMAN`, `PERLU_WASPADA`, `BERISIKO_TINGGI`) dihasilkan secara konsisten.
- Penalti kebocoran kredensial OTP pada simulasi telepon berfungsi dengan benar.
