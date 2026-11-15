-- ==========================================================
-- TipuSkip Seed Data (Data Awal untuk Pengujian & Edukasi)
-- Dikembangkan untuk Anggota 1 & Anggota 2
-- ==========================================================

USE tipuskip_db;

-- 1. Seed Pengguna (Password: admin123 & user123)
INSERT INTO pengguna (username, email, password_hash, role) VALUES
('admin', 'admin@tipuskip.local', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'ADMIN'),
('budi', 'budi@example.com', '04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb', 'PENGGUNA')
ON DUPLICATE KEY UPDATE username=username;

-- 2. Seed Skenario Simulasi
INSERT INTO skenario (id, judul, deskripsi, jenis, kategori, tingkat_kesulitan, status_aktif) VALUES
(1, 'Tawaran Kerja Freelance Like Video YouTube', 'Penipu menawarkan komisi mudah hanya dengan like video, berujung permintaan deposit dana VIP.', 'PESAN', 'Lowongan Kerja Palsu', 'MUDAH', TRUE),
(2, 'Kurir Ekspedisi: Konfirmasi Foto Paket Rusak (.APK)', 'Penipu mengaku kurir ekspedisi mengirim file berformat .APK dengan dalih foto paket rusak untuk menyadap SMS dan OTP perbankan.', 'PESAN', 'Malware / APK Sniffer', 'SEDANG', TRUE),
(3, 'Surat Tilang Elektronik (ETLE) Palsu Kepolisian', 'Penipu mengaku Korlantas Polri mengirimkan surat konfirmasi pelanggaran tilang dalam format .APK dengan ancaman denda dan blokir STNK.', 'PESAN', 'Malware / Impersonasi Instansi', 'SULIT', TRUE),
(4, 'Pemenang Undian Berkah Belanja Marketplace Rp10.000.000', 'Pesan SMS/WA menyatakan Anda menang undian ratusan juta dan diarahkan ke situs tiruan untuk memasukkan PIN dan OTP akun.', 'PESAN', 'Phishing Website', 'MUDAH', TRUE),
(5, 'Panggilan Darurat Bank: Pemblokiran Rekening & Minta OTP', 'Penipu menelepon mengaku dari customer service bank mengabarkan ada transaksi liar dan mendesak korban menyebutkan 6 digit kode OTP.', 'TELEPON', 'Impersonasi Bank / Social Engineering', 'SEDANG', TRUE),
(6, 'Perubahan Tarif Biaya Transaksi Bank Jadi Rp150.000/Bulan', 'Penipu mengabarkan ada perubahan skema tarif bulanan bank yang mahal. Jika nasabah menolak, diarahkan mengisi formulir phishing.', 'TELEPON', 'Manipulasi Biaya Administrasi', 'SEDANG', TRUE),
(7, 'Impersonasi Keluarga Mengalami Musibah / Kecelakaan', 'Penipu berpura-pura menjadi polisi atau petugas rumah sakit, mengabarkan kerabat terdekat kecelakaan/ditangkap dan butuh transfer uang kilat.', 'TELEPON', 'Social Engineering Emosional', 'SULIT', TRUE)
ON DUPLICATE KEY UPDATE judul=VALUES(judul), deskripsi=VALUES(deskripsi), kategori=VALUES(kategori), tingkat_kesulitan=VALUES(tingkat_kesulitan);

-- 3. Seed Langkah Skenario 1 (Pesan: Freelance)
INSERT INTO langkah_skenario (id, skenario_id, nomor_langkah, nama_pengirim, pesan_pengirim, tipe_konten) VALUES
(1, 1, 1, 'Recruiter Sarah (PT Digital Mandiri)', 'Halo Kak! Kami dari HRD PT Digital Mandiri. Sedang membuka lowongan freelance paruh waktu tugas ringan: cukup Like 3 video YouTube dapat gaji Rp150.000/hari. Apakah Kakak berminat?', 'TEXT'),
(2, 1, 2, 'Recruiter Sarah (PT Digital Mandiri)', 'Bagus sekali! Ini link tugas pertamanya: https://s.id/job-like-yt-bonus. Silakan screenshot setelah Like dan kirimkan ke kami ya. Setelah itu kami masukkan ke grup Telegram untuk pencairan bonus.', 'TEXT'),
(3, 1, 3, 'Admin Grup VIP (Toko Invest Global)', 'Selamat bonus Rp30.000 pertama berhasil cair! Untuk tugas tahap VIP selanjutnya dengan komisi Rp1.500.000, Kakak wajib deposit awal Rp300.000 ke rekening bendahara kami sekarang. Slot terbatas 10 menit!', 'TEXT'),

-- Skenario 2 (Pesan: Kurir APK)
(4, 2, 1, 'Kurir JNT Express - Mas Agus', 'Selamat siang kak, saya kurir ekspedisi mau antar paket. Tapi kardus paketnya robek parah saat perjalanan. Tolong cek foto kerusakannya ya kak: [Lihat_Foto_Paket.apk]', 'TEXT'),
(5, 2, 2, 'Kurir JNT Express - Mas Agus', 'Iya kak itu aplikasi sistem komplain resmi ekspedisi kami. Kalau tidak di-install sekarang paket akan kami retur hangus ke gudang pusat lho!', 'TEXT'),

-- Skenario 3 (Pesan: ETLE APK)
(6, 3, 1, 'KORLANTAS POLRI RESMI', 'PEMBERITAHUAN TILANG ELEKTRONIK: Kendaraan Anda tertangkap kamera ETLE melanggar batas kecepatan. Silakan unduh Surat_Pemberitahuan_ETLE.apk untuk melihat foto bukti dan nomor bayar tilang dalam 24 jam sebelum STNK diblokir.', 'TEXT'),

-- Skenario 4 (Pesan: Pemenang Hadiah)
(7, 4, 1, 'Shopee Care Promo', 'Selamat! Nomor WhatsApp Anda terpilih mendapatkan Grand Prize Rp10.000.000 dari Gebyar Promo Berkah. Silakan klaim hadiah Anda melalui portal resmi: https://shopee-klaim-hadiah2026.online sebelum pukul 23:59!', 'TEXT'),

-- Skenario 5 (Telepon: Bank OTP)
(8, 5, 1, 'Panggilan Masuk: +62-812-9988-7711 (Mengaku HaloBCA)', 'Selamat siang Bapak/Ibu. Kami mendeteksi adanya transaksi mencurigakan senilai Rp7.500.000 di toko online luar negeri dari kartu debit Anda. Apakah benar Anda yang melakukan transaksi tersebut?', 'AUDIO_CALL'),
(9, 5, 2, 'Panggilan Masuk: +62-812-9988-7711 (Mengaku HaloBCA)', 'Baik untuk membatalkan transaksi dan mengamankan rekening, sistem kami baru saja mengirim 6 digit kode pembatalan ke SMS Anda. Tolong sebutkan kodenya sekarang juga agar uang tidak terpotong!', 'AUDIO_CALL'),

-- Skenario 6 (Telepon: Tarif Bank)
(10, 6, 1, 'Panggilan Masuk: +62-821-4455-6677 (Customer Care BRI)', 'Selamat pagi Nasabah Terhormat, kami menginformasikan per malam ini ada pembaruan tarif transaksi antarbank menjadi Rp150.000 per bulan yang didebet otomatis. Apakah Bapak/Ibu setuju dengan kenaikan tarif ini?', 'AUDIO_CALL'),
(11, 6, 2, 'Panggilan Masuk: +62-821-4455-6677 (Customer Care BRI)', 'Baik jika tidak setuju, kami bantu batalkan. Kami kirimkan tautan form pembatalan via SMS ya. Nanti silakan masukkan nomor kartu ATM, masa berlaku, dan 3 digit CVV di belakang kartu.', 'AUDIO_CALL'),

-- Skenario 7 (Telepon: Kecelakaan Kerabat)
(12, 7, 1, 'Panggilan Darurat: +62-813-7788-9900 (Petugas Rumah Sakit / Polisi)', 'Halo! Ini dengan keluarga Andi? Andi baru saja mengalami kecelakaan parah dan pendarahan hebat di jalan raya! Kami butuh uang operasi darurat Rp5.000.000 sekarang ke rekening dokter sebelum terlambat!', 'AUDIO_CALL')
ON DUPLICATE KEY UPDATE pesan_pengirim=VALUES(pesan_pengirim), nama_pengirim=VALUES(nama_pengirim);

-- 4. Seed Pilihan Respons
-- Langkah 1 (Skenario 1 - Langkah 1)
INSERT INTO pilihan_respons (id, langkah_id, teks_respons, tingkat_risiko, poin_skor, umpan_balik, konsekuensi, langkah_berikutnya) VALUES
(1, 1, 'Halo, dari mana Anda mendapatkan nomor saya? Perusahaan Anda tidak pernah saya lamar.', 'AMAN', 100, 'Respons sangat baik! Kritis terhadap pengirim asing adalah pertahanan utama.', 'Penipu menyadari Anda waspada dan tidak mudah dimanipulasi.', 2),
(2, 1, 'Wah menarik sekali! Mau dong Kak, bagaimana cara daftarnya?', 'BAHAYA', 0, 'Waspada! Tawaran kerja instan tanpa wawancara resmi hampir pasti merupakan modus task scam.', 'Anda langsung masuk perangkap untuk diarahkan ke skema deposit.', 2),
(3, 1, 'Bisa kirimkan surat penawaran resmi atau website perusahaan terlebih dahulu?', 'WASPADA', 70, 'Cukup baik, tetapi penipu seringkali menyiapkan web fiktif/kloningan.', 'Penipu akan mencoba memberikan link profil perusahaan palsu.', 2),

-- Langkah 2 (Skenario 1 - Langkah 2)
(4, 2, 'Maaf, saya tidak membuka tautan singkat (shortlink) dari orang tidak dikenal.', 'AMAN', 100, 'Tindakan tepat! Shortlink seperti s.id atau bit.ly sering menyembunyikan phishing atau malware.', 'Anda terhindar dari unduhan berbahaya dan pencurian kredensial akun.', 3),
(5, 2, 'Oke saya klik linknya dan saya like videonya sekarang ya kak.', 'BAHAYA', 0, 'Sangat berisiko! Anda masuk ke alur psikologis manipulasi "diberi imbalan kecil di awal".', 'Penipu membangun kepercayaan palsu sebelum meminta uang deposit.', 3),

-- Langkah 3 (Skenario 1 - Langkah 3)
(6, 3, 'Pekerjaan yang meminta uang deposit adalah penipuan! Saya hentikan dan laporkan nomor ini.', 'AMAN', 100, 'Hebat! Anda berhasil mengenali modus penipuan tugas berbayar (task scam).', 'Uang Anda aman dan Anda berhasil menyelamatkan diri dari kerugian finansial.', NULL),
(7, 3, 'Baik saya transfer Rp300.000 sekarang karena komisinya besar.', 'BAHAYA', 0, 'FATAL! Uang Anda akan hilang dan penipu akan meminta deposit lebih besar lagi dengan dalih akun terkunci.', 'Anda mengalami kerugian finansial nyata.', NULL),

-- Langkah 4 (Skenario 2 - Langkah 1)
(8, 4, 'Kenapa fotonya berekstensi .apk? Saya tidak akan install file aplikasi dari orang asing.', 'AMAN', 100, 'Luar biasa waspada! Foto asli berformat .jpg atau .png, bukan berkas aplikasi .apk.', 'Penipu gagal menyusupkan malware ke ponsel pintar Anda.', 2),
(9, 4, 'Waduh rusak ya? Oke langsung saya unduh dan pasang aplikasinya biar bisa lihat fotonya.', 'BAHAYA', 0, 'BAHAYA BESAR! Berkas .APK ini berisi trojan pengirim SMS yang akan menyadap OTP mobile banking Anda.', 'Malware terpasang dan mengakses izin SMS perbankan Anda.', 2),

-- Langkah 5 (Skenario 2 - Langkah 2)
(10, 5, 'Saya akan cek nomor resi paket saya langsung melalui aplikasi resmi J&T Express di Google Play Store.', 'AMAN', 100, 'Tindakan paling cerdas! Verifikasi mandiri di kanal resmi menggagalkan manipulasi kurir gadungan.', 'Akun bank dan perangkat Anda sepenuhnya aman.', NULL),
(11, 5, 'Ya sudah saya izinkan akses install dari sumber tak dikenal (unknown sources).', 'BAHAYA', 0, 'FATAL! Memberi izin instalasi aplikasi luar toko resmi membuka celah kendali total bagi peretas.', 'Peretas berhasil menyadap seluruh SMS notifikasi perbankan Anda.', NULL),

-- Langkah 6 (Skenario 3 - Langkah 1)
(12, 6, 'Kepolisian RI tidak pernah mengirim surat tilang lewat WhatsApp atau berkas .apk. Saya akan cek di situs etle-korlantas.info.', 'AMAN', 100, 'Sangat tepat! Tilang resmi dikirim melalui surat pos fisik ke alamat sesuai STNK atau dicek mandiri di situs resmi.', 'Anda tidak termakan intimidasi kepolisian palsu.', NULL),
(13, 6, 'Takut STNK diblokir, saya langsung unduh dan buka Surat_Pemberitahuan_ETLE.apk.', 'BAHAYA', 0, 'Sangat berbahaya! Rasa panik karena ancaman hukum dimanfaatkan penipu agar Anda mengabaikan akal sehat.', 'Aplikasi pencuri data terpasang pada perangkat Anda.', NULL),
(14, 6, 'Pak polisi, saya melanggar di mana ya? Bisa damai di tempat saja?', 'WASPADA', 40, 'Menanggapi penipu membuat Anda tetap menjadi target rekayasa sosial lebih lanjut.', 'Penipu akan memberikan nomor rekening pribadi untuk transfer uang damai.', NULL),

-- Langkah 7 (Skenario 4 - Langkah 1)
(15, 7, 'Saya tidak pernah mengikuti undian ini. Domain web mencurigakan (bukan shopee.co.id). Ini penipuan!', 'AMAN', 100, 'Tepat sekali! Domain resmi hanya shopee.co.id. Waspadai domain palsu yang dibuat mirip.', 'Kredensial akun belanja dan dompet digital Anda aman.', NULL),
(16, 7, 'Alhamdulillah! Saya klik linknya dan masukkan password serta nomor telepon untuk klaim.', 'BAHAYA', 0, 'FATAL! Anda menyerahkan kata sandi akun ke situs phishing penipu.', 'Akun ShopeePay/dompet digital Anda langsung dikuras oleh penipu.', NULL),

-- Langkah 8 (Skenario 5 - Langkah 1)
(17, 8, 'Saya akan matikan panggilan ini dan menghubungi call center resmi HaloBCA di 1500888 secara mandiri.', 'AMAN', 100, 'Sempurna! Langkah verifikasi dua arah adalah standar emas menghadapi social engineering perbankan.', 'Penipu gagal memanfaatkan faktor kepanikan Anda.', NULL),
(18, 8, 'Tidak benar! Saya tidak pernah belanja itu! Tolong batalkan sekarang juga Pak!', 'WASPADA', 50, 'Reaksi panik inilah yang dimanfaatkan penipu untuk menciptakan rasa genting (urgency). Tenangkan diri.', 'Penipu akan langsung memanfaatkan kepanikan Anda untuk meminta OTP.', 2),

-- Langkah 9 (Skenario 5 - Langkah 2)
(19, 9, 'SMS yang masuk tertulis KODE OTP RAHASIA dan jangan dibagikan kepada siapapun termasuk petugas bank!', 'AMAN', 100, 'Luar biasa! Pihak bank TIDAK PERNAH meminta kode OTP atau PIN Anda dalam kondisi apapun.', 'Rekening Anda terselamatkan dari pembobolan saldo.', NULL),
(20, 9, 'Kodenya 894321, tolong segera dibatalkan transaksinya ya Pak!', 'BAHAYA', 0, 'SANGAT BERBAHAYA! Kode tersebut adalah OTP pengesahan transfer/pembelian yang justru menyetujui transaksi penipu.', 'Saldo rekening Anda terkuras habis.', NULL),

-- Langkah 10 (Skenario 6 - Langkah 1)
(21, 10, 'Saya tidak setuju! Kenapa mahal sekali? Batalkan perubahan tarif rekening saya!', 'WASPADA', 50, 'Penipu sengaja membuat tarif tidak masuk akal agar korban emosi dan terburu-buru minta pembatalan.', 'Penipu mengarahkan Anda ke formulir pembatalan palsu.', 2),
(22, 10, 'Setahu saya kebijakan bank diumumkan via website resmi. Saya akan tanyakan langsung ke kantor cabang.', 'AMAN', 100, 'Langkah perlindungan terbaik! Tidak gegabah merespons informasi sepihak dari nomor seluler biasa.', 'Penipu menyadari modusnya gagal memicu kepanikan Anda.', NULL),

-- Langkah 11 (Skenario 6 - Langkah 2)
(23, 11, '3 digit CVV di belakang kartu dan nomor kartu ATM adalah data rahasia perbankan! Anda penipu!', 'AMAN', 100, 'Cerdas! CVV kartu ATM/Debit adalah kunci otorisasi pembayaran online yang tidak boleh dibagikan kepada siapapun.', 'Anda berhasil melindungi data kartu pembayaran Anda.', NULL),
(24, 11, 'Baik saya isi formnya lengkap dengan nomor kartu dan CVV-nya.', 'BAHAYA', 0, 'FATAL! Data kartu pembayaran Anda dapat langsung dipakai penipu untuk berbelanja online tanpa izin.', 'Kartu debit Anda disalahgunakan untuk transaksi ilegal.', NULL),

-- Langkah 12 (Skenario 7 - Langkah 1)
(25, 12, 'Tolong sebutkan nama rumah sakitnya. Saya akan telepon nomor Andi sendiri dan minta kerabat dekat mendatangi rumah sakit tersebut.', 'AMAN', 100, 'Sangat tenang dan bijaksana! Modus kecelakaan menargetkan kepanikan emosional keluarga. Verifikasi ke nomor pribadi kerabat adalah kunci.', 'Penipu gagal mengeksploitasi kepanikan keluarga.', NULL),
(26, 12, 'Ya Tuhan Andi kecelakaan?! Tolong selamatkan dia, kirim nomor rekeningnya saya transfer uangnya sekarang!', 'BAHAYA', 0, 'Berbahaya! Emosi panik membuat korban tidak berpikir kritis dan langsung mentransfer uang ke rekening penipu.', 'Uang jutaan rupiah melayang ke rekening penampung penipu.', NULL)
ON DUPLICATE KEY UPDATE teks_respons=VALUES(teks_respons), umpan_balik=VALUES(umpan_balik), konsekuensi=VALUES(konsekuensi);

-- 5. Seed Indikator Penipuan (Pola & Regex)
INSERT INTO indikator_penipuan (id, nama_indikator, pola_pencocokan, tipe_pencocokan, bobot_risiko, deskripsi, saran_verifikasi, status_aktif) VALUES
(1, 'Permintaan Kode OTP / Rahasia', '(otp|kode verifikasi|one time password|kode rahasia|kode sms)', 'REGEX', 40, 'Pesan meminta atau menyinggung kode autentikasi satu kali pakai (OTP).', 'Jangan pernah memberikan kode OTP kepada siapa pun, termasuk pihak yang mengaku petugas resmi bank atau aplikasi.', TRUE),
(2, 'Tautan Mencurigakan / Shortlink', '(bit\\.ly|s\\.id|tinyurl|t\\.me/|linkungan|wa\\.me)', 'REGEX', 25, 'Penggunaan tautan pendek atau tautan tidak resmi yang menyembunyikan alamat situs sebenarnya.', 'Cek keaslian domain website. Jangan pernah membuka shortlink dari nomor yang tidak dikenal.', TRUE),
(3, 'Unduhan Berkas APK Berbahaya', '(\\.apk|unduh surat undangan|paket ekspedisi|tagihan pln\\.apk)', 'REGEX', 45, 'Upaya pengiriman aplikasi berbahaya berformat .APK yang berpotensi menyadap SMS dan OTP perbankan.', 'Jangan install file APK di luar Google Play Store atau Apple App Store.', TRUE),
(4, 'Tekanan Waktu dan Urgensi Berlebihan', '(sekarang juga|dalam 5 menit|segera|batas waktu|sebelum hangus|darurat)', 'REGEX', 20, 'Penipu menggunakan manipulasi psikologis agar korban panik dan tidak sempat berpikir logis.', 'Tetap tenang. Lembaga resmi tidak akan menuntut tindakan tergesa-gesa dalam hitungan menit.', TRUE),
(5, 'Ancaman Pemblokiran Rekening / Hukum', '(dibekukan|blokir|pihak berwajib|surat panggilan polisi|tuntutan hukum)', 'REGEX', 25, 'Upaya menakut-nakuti korban dengan konsekuensi hukum atau finansial fiktif.', 'Hubungi kantor polisi atau call center resmi secara mandiri untuk konfirmasi.', TRUE),
(6, 'Janji Keuntungan / Hadiah Fantastis', '(selamat anda menang|hadiah undian|komisi harian|profit 50%|jutaan rupiah|tanpa modal)', 'REGEX', 30, 'Tawaran uang melimpah atau hadiah undian tanpa ada proses pendaftaran yang jelas.', 'Waspadai tawaran "too good to be true". Konfirmasi promo hanya pada kanal resmi perusahaan bersangkutan.', TRUE),
(7, 'Permintaan Deposit / Transfer Uang Awal', '(deposit|biaya administrasi transfer|talangan|tebus hadiah)', 'REGEX', 35, 'Modus meminta uang muka atau deposit untuk mencairkan hadiah atau komisi pekerjaan.', 'Jika Anda harus membayar uang untuk mencairkan hadiah atau pekerjaan, itu adalah 100% penipuan.', TRUE)
ON DUPLICATE KEY UPDATE nama_indikator=VALUES(nama_indikator);
