-- ==========================================================
-- TipuSkip Seed Data (Data Awal untuk Pengujian & Edukasi)
-- ==========================================================

USE tipuskip_db;

-- 1. Seed Pengguna (Password: admin123 & user123)
INSERT INTO pengguna (username, email, password_hash, role) VALUES
('admin', 'admin@tipuskip.local', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'ADMIN'),
('budi', 'budi@example.com', '04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb', 'PENGGUNA')
ON DUPLICATE KEY UPDATE username=username;

-- 2. Seed Skenario Simulasi
INSERT INTO skenario (id, judul, deskripsi, jenis, kategori, tingkat_kesulitan, status_aktif) VALUES
(1, 'Tawaran Kerja Freelance Like Video YouTube', 'Penipu menawarkan komisi mudah hanya dengan like video, berujung permintaan deposit dana.', 'PESAN', 'Lowongan Kerja Palsu', 'MUDAH', TRUE),
(2, 'Panggilan Darurat: Pemblokiran Rekening Bank', 'Penipu menelepon mengaku dari call center bank resmi dan meminta kode OTP untuk verifikasi data rekening darurat.', 'TELEPON', 'Impersonasi Bank', 'SEDANG', TRUE)
ON DUPLICATE KEY UPDATE judul=VALUES(judul);

-- 3. Seed Langkah Skenario 1 (Pesan: Freelance)
INSERT INTO langkah_skenario (id, skenario_id, nomor_langkah, nama_pengirim, pesan_pengirim, tipe_konten) VALUES
(1, 1, 1, 'Recruiter Sarah (PT Digital Mandiri)', 'Halo Kak! Kami dari HRD PT Digital Mandiri. Sedang membuka lowongan freelance paruh waktu tugas ringan: cukup Like 3 video YouTube dapat gaji Rp150.000/hari. Apakah Kakak berminat?', 'TEXT'),
(2, 1, 2, 'Recruiter Sarah (PT Digital Mandiri)', 'Bagus sekali! Ini link tugas pertamanya: https://s.id/job-like-yt-bonus. Silakan screenshot setelah Like dan kirimkan ke kami ya. Setelah itu kami masukkan ke grup Telegram untuk pencairan bonus.', 'TEXT'),
(3, 1, 3, 'Admin Grup Telegram (Toko Invest)', 'Selamat bonus Rp30.000 pertama berhasil cair! Untuk tugas tahap VIP selanjutnya dengan komisi Rp1.500.000, Kakak wajib deposit awal Rp300.000 ke rekening bendahara kami sekarang. Slot terbatas 10 menit!', 'TEXT')
ON DUPLICATE KEY UPDATE pesan_pengirim=VALUES(pesan_pengirim);

-- 4. Seed Pilihan Respons untuk Langkah Skenario 1
-- Langkah 1
INSERT INTO pilihan_respons (langkah_id, teks_respons, tingkat_risiko, poin_skor, umpan_balik, konsekuensi, langkah_berikutnya) VALUES
(1, 'Halo, dari mana Anda mendapatkan nomor saya? Perusahaan Anda tidak pernah saya lamar.', 'AMAN', 100, 'Respons sangat baik! Kritis terhadap pesan asing adalah perlindungan utama.', 'Penipu merasa Anda waspada dan tidak mudah dimanipulasi.', 2),
(1, 'Wah menarik sekali! Mau dong Kak, bagaimana cara daftarnya?', 'BAHAYA', 0, 'Waspada! Tawaran kerja instan tanpa wawancara resmi hampir pasti merupakan modus penipuan tugas/deposit.', 'Anda langsung menjadi target empuk untuk diarahkan ke skema deposit.', 2),
(1, 'Bisa kirimkan surat penawaran resmi atau website perusahaan terlebih dahulu?', 'WASPADA', 70, 'Cukup baik, tetapi penipu seringkali menyiapkan web palsu/kloningan.', 'Penipu akan mencoba memberikan link atau identitas perusahaan yang dicatut.', 2);

-- Langkah 2
INSERT INTO pilihan_respons (langkah_id, teks_respons, tingkat_risiko, poin_skor, umpan_balik, konsekuensi, langkah_berikutnya) VALUES
(2, 'Maaf, saya tidak membuka tautan singkat (shortlink) dari orang tidak dikenal.', 'AMAN', 100, 'Tindakan tepat! Shortlink seperti s.id atau bit.ly sering menyembunyikan phishing atau malware.', 'Anda terhindar dari unduhan berbahaya dan pencurian kredensial akun.', 3),
(2, 'Oke saya klik linknya dan saya like videonya sekarang ya kak.', 'BAHAYA', 0, 'Sangat berisiko! Anda masuk ke alur psikologis manipulasi "diberi imbalan kecil di awal".', 'Penipu membangun kepercayaan palsu sebelum meminta uang deposit.', 3);

-- Langkah 3
INSERT INTO pilihan_respons (langkah_id, teks_respons, tingkat_risiko, poin_skor, umpan_balik, konsekuensi, langkah_berikutnya) VALUES
(3, 'Pekerjaan yang meminta uang deposit adalah penipuan! Saya hentikan dan laporkan nomor ini.', 'AMAN', 100, 'Hebat! Anda berhasil mengenali modus penipuan tugas berbayar (task scam).', 'Uang Anda aman dan Anda berhasil menyelamatkan diri dari kerugian finansial.', NULL),
(3, 'Baik saya transfer Rp300.000 sekarang karena komisinya besar.', 'BAHAYA', 0, 'FATAL! Uang Anda akan hilang dan penipu akan meminta deposit lebih besar lagi dengan alasan sistem terkunci.', 'Anda mengalami kerugian finansial nyata.', NULL);

-- 5. Seed Langkah Skenario 2 (Telepon: Bank)
INSERT INTO langkah_skenario (id, skenario_id, nomor_langkah, nama_pengirim, pesan_pengirim, tipe_konten) VALUES
(4, 2, 1, 'Panggilan +62-812-9988-7711 (Mengaku HaloBCA / Bank)', 'Selamat siang Bapak/Ibu. Kami mendeteksi adanya transaksi mencurigakan senilai Rp7.500.000 di merchant online luar negeri dari rekening Anda. Apakah benar Anda yang melakukan transaksi tersebut?', 'AUDIO_CALL'),
(5, 2, 2, 'Panggilan +62-812-9988-7711 (Mengaku HaloBCA / Bank)', 'Baik untuk membatalkan transaksi dan mengamankan rekening, sistem kami baru saja mengirim 6 digit kode pembatalan ke SMS Anda. Tolong sebutkan kodenya sekarang juga agar tidak terdebet otomatis!', 'AUDIO_CALL')
ON DUPLICATE KEY UPDATE pesan_pengirim=VALUES(pesan_pengirim);

-- 6. Seed Pilihan Respons untuk Skenario 2
-- Langkah 1
INSERT INTO pilihan_respons (langkah_id, teks_respons, tingkat_risiko, poin_skor, umpan_balik, konsekuensi, langkah_berikutnya) VALUES
(4, 'Tidak benar! Tolong batalkan sekarang juga!', 'WASPADA', 50, 'Reaksi panik inilah yang dimanfaatkan penipu (rekayasa sosial/urgensi). Tenangkan diri.', 'Penipu akan langsung memanfaatkan kepanikan Anda untuk meminta OTP.', 5),
(4, 'Saya akan matikan telepon ini dan menghubungi call center resmi bank saya secara mandiri.', 'AMAN', 100, 'Sempurna! Langkah verifikasi dua arah adalah standar emas menghadapi social engineering.', 'Penipu gagal memanfaatkan faktor kepanikan dan rekayasa psikologis.', NULL);

-- Langkah 2
INSERT INTO pilihan_respons (langkah_id, teks_respons, tingkat_risiko, poin_skor, umpan_balik, konsekuensi, langkah_berikutnya) VALUES
(5, 'SMS yang masuk tertulis KODE OTP RAHASIA dan jangan dibagikan kepada siapapun termasuk petugas bank!', 'AMAN', 100, 'Luar biasa! Pihak bank TIDAK PERNAH meminta kode OTP atau PIN Anda dalam kondisi apapun.', 'Rekening Anda terselamatkan dari pembobolan saldo.', NULL),
(5, 'Kodenya 894321, tolong segera dibatalkan ya Pak!', 'BAHAYA', 0, 'SANGAT BERBAHAYA! Kode tersebut adalah OTP pengesahan transfer/pembelian yang justru menyetujui transaksi penipu.', 'Saldo rekening Anda terkuras habis.', NULL);

-- 7. Seed Indikator Penipuan (Pola & Regex)
INSERT INTO indikator_penipuan (id, nama_indikator, pola_pencocokan, tipe_pencocokan, bobot_risiko, deskripsi, saran_verifikasi, status_aktif) VALUES
(1, 'Permintaan Kode OTP / Rahasia', '(otp|kode verifikasi|one time password|kode rahasia|kode sms)', 'REGEX', 40, 'Pesan meminta atau menyinggung kode autentikasi satu kali pakai (OTP).', 'Jangan pernah memberikan kode OTP kepada siapa pun, termasuk pihak yang mengaku petugas resmi bank atau aplikasi.', TRUE),
(2, 'Tautan Mencurigakan / Shortlink', '(bit\\.ly|s\\.id|tinyurl|t\\.me/|linkungan|wa\\.me)', 'REGEX', 25, 'Penggunaan tautan pendek atau tautan tidak resmi yang menyembunyikan alamat situs sebenarnya.', 'Cek keaslian domain website. Jangan pernah membuka shortlink dari nomor yang tidak dikenal.', TRUE),
(3, 'Unduhan Berkas APK Berbahaya', '(\\.apk|unduh surat undangan|paket ekspedisi|tagihan pln\\.apk)', 'REGEX', 45, 'Upaya pengiriman aplikasi berbahaya berformat .APK yang berpotensi menyadap SMS dan OTP perbankan.', 'Jangan install file APK di luar Google Play Store atau Apple App Store.', TRUE),
(4, 'Tekanan Waktu dan Urgensi Berlebihan', '(sekarang juga|dalam 5 menit|segera|batas waktu|sebelum hangus|darurat)', 'REGEX', 20, 'Penipu menggunakan manipulasi psikologis agar korban panik dan tidak sempat berpikir logis.', 'Tetap tenang. Lembaga resmi tidak akan menuntut tindakan tergesa-gesa dalam hitungan menit.', TRUE),
(5, 'Ancaman Pemblokiran Rekening / Hukum', '(dibekukan|blokir|pihak berwajib|surat panggilan polisi|tuntutan hukum)', 'REGEX', 25, 'Upaya menakut-nakuti korban dengan konsekuensi hukum atau finansial fiktif.', 'Hubungi kantor polisi atau call center resmi secara mandiri untuk konfirmasi.', TRUE),
(6, 'Janji Keuntungan / Hadiah Fantastis', '(selamat anda menang|hadiah undian|komisi harian|profit 50%|jutaan rupiah|tanpa modal)', 'REGEX', 30, 'Tawaran uang melimpah atau hadiah undian tanpa ada proses pendaftaran yang jelas.', 'Waspadai tawaran "too good to be true". Konfirmasi promo hanya pada kanal resmi perusahaan bersangkutan.', TRUE),
(7, 'Permintaan Deposit / Transfer Uang Awal', '(deposit|biaya administrasi transfer|talangan|tebus hadiah)', 'REGEX', 35, 'Modus meminta uang muka atau deposit untuk mencairkan hadiah atau komisi pekerjaan.', 'Jika Anda harus membayar uang untuk mencairkan hadiah atau pekerjaan, itu adalah 100% penipuan.', TRUE)
ON DUPLICATE KEY UPDATE nama_indikator=VALUES(nama_indikator);
