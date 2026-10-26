package com.tipuskip.service.impl;

import com.tipuskip.model.*;
import com.tipuskip.service.ISimulasiService;
import com.tipuskip.simulation.Simulasi;
import com.tipuskip.simulation.SimulasiPesan;
import com.tipuskip.simulation.SimulasiTelepon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Mock Simulasi Service untuk kebutuhan UI testing dan development tanpa koneksi database.
 * Berisi skenario lengkap untuk simulasi pesan dan telepon (Tanggung jawab Anggota 1).
 */
public class MockSimulasiService implements ISimulasiService {
    private final List<Skenario> mockSkenarioList = new ArrayList<>();
    private final List<HasilSimulasi> mockRiwayatList = new ArrayList<>();

    public MockSimulasiService() {
        initMockData();
    }

    private void initMockData() {
        // =========================================================================
        // SKENARIO PESAN 1: Freelance Task Scam (Like Video & Deposit VIP)
        // =========================================================================
        Skenario s1 = new Skenario(1, "Tawaran Kerja Freelance Like Video YouTube",
                "Penipuan berkedok tugas mudah like video dengan komisi menggiurkan, berujung permintaan transfer deposit dana VIP.",
                JenisSkenario.PESAN, "Lowongan Kerja Palsu", TingkatKesulitan.MUDAH, true);

        LangkahSkenario s1_l1 = new LangkahSkenario(1, 1, 1, "Recruiter Sarah (PT Digital Mandiri)",
                "Halo Kak! Kami dari HRD PT Digital Mandiri. Sedang membuka lowongan freelance paruh waktu tugas ringan: cukup Like 3 video YouTube dapat gaji Rp150.000/hari. Apakah Kakak berminat?", "TEXT");
        s1_l1.tambahPilihanRespons(new PilihanRespons(1, 1, "Halo, dari mana Anda mendapatkan nomor saya? Perusahaan Anda tidak pernah saya lamar.", TingkatRisiko.AMAN, 100,
                "Respons sangat baik! Sikap kritis terhadap pengirim asing adalah pertahanan pertama.", "Penipu menyadari Anda waspada dan tidak mudah dimanipulasi.", 2));
        s1_l1.tambahPilihanRespons(new PilihanRespons(2, 1, "Wah menarik sekali! Mau dong Kak, bagaimana cara daftarnya?", TingkatRisiko.BAHAYA, 0,
                "Waspada! Tawaran kerja instan tanpa wawancara resmi hampir pasti merupakan modus task scam.", "Anda langsung masuk perangkap untuk diarahkan ke skema deposit.", 2));
        s1_l1.tambahPilihanRespons(new PilihanRespons(3, 1, "Bisa kirimkan surat penawaran resmi atau website perusahaan terlebih dahulu?", TingkatRisiko.WASPADA, 70,
                "Cukup baik, tetapi penipu seringkali menyiapkan web fiktif/kloningan.", "Penipu akan mencoba memberikan link profil perusahaan palsu.", 2));

        LangkahSkenario s1_l2 = new LangkahSkenario(2, 1, 2, "Recruiter Sarah (PT Digital Mandiri)",
                "Bagus sekali! Ini link tugas pertamanya: https://s.id/job-like-yt-bonus. Silakan screenshot setelah Like dan kirimkan ke kami ya. Setelah itu kami masukkan ke grup Telegram untuk pencairan bonus.", "TEXT");
        s1_l2.tambahPilihanRespons(new PilihanRespons(4, 2, "Maaf, saya tidak membuka tautan singkat (shortlink) dari kontak tidak dikenal.", TingkatRisiko.AMAN, 100,
                "Tindakan tepat! Shortlink seperti s.id atau bit.ly sering menyembunyikan phishing atau malware.", "Anda terhindar dari unduhan berbahaya dan pencurian kredensial akun.", 3));
        s1_l2.tambahPilihanRespons(new PilihanRespons(5, 2, "Oke saya klik linknya dan saya like videonya sekarang ya kak.", TingkatRisiko.BAHAYA, 0,
                "Sangat berisiko! Anda masuk ke alur psikologis manipulasi 'diberi imbalan kecil di awal'.", "Penipu membangun kepercayaan palsu sebelum meminta uang deposit.", 3));

        LangkahSkenario s1_l3 = new LangkahSkenario(3, 1, 3, "Admin Grup VIP (Toko Invest Global)",
                "Selamat bonus Rp30.000 pertama berhasil cair! Untuk tugas tahap VIP selanjutnya dengan komisi Rp1.500.000, Kakak wajib deposit awal Rp300.000 ke rekening bendahara kami sekarang. Slot terbatas 10 menit!", "TEXT");
        s1_l3.tambahPilihanRespons(new PilihanRespons(6, 3, "Pekerjaan yang meminta uang deposit adalah penipuan! Saya hentikan dan laporkan nomor ini.", TingkatRisiko.AMAN, 100,
                "Hebat! Anda berhasil mengenali modus penipuan tugas berbayar (task scam).", "Uang Anda aman dan Anda berhasil menyelamatkan diri dari kerugian finansial.", null));
        s1_l3.tambahPilihanRespons(new PilihanRespons(7, 3, "Baik saya transfer Rp300.000 sekarang karena komisinya besar.", TingkatRisiko.BAHAYA, 0,
                "FATAL! Uang Anda akan hilang dan penipu akan meminta deposit lebih besar lagi dengan dalih akun terkunci.", "Anda mengalami kerugian finansial nyata.", null));

        s1.tambahLangkah(s1_l1);
        s1.tambahLangkah(s1_l2);
        s1.tambahLangkah(s1_l3);
        mockSkenarioList.add(s1);

        // =========================================================================
        // SKENARIO PESAN 2: Kurir Paket Ekspedisi & Undangan .APK Berbahaya
        // =========================================================================
        Skenario s2 = new Skenario(2, "Kurir Ekspedisi: Konfirmasi Foto Paket Rusak (.APK)",
                "Penipu mengaku kurir ekspedisi mengirim file berformat .APK dengan dalih foto paket rusak untuk menyadap SMS dan OTP perbankan.",
                JenisSkenario.PESAN, "Malware / APK Sniffer", TingkatKesulitan.SEDANG, true);

        LangkahSkenario s2_l1 = new LangkahSkenario(4, 2, 1, "Kurir JNT Express - Mas Agus",
                "Selamat siang kak, saya kurir ekspedisi mau antar paket. Tapi kardus paketnya robek parah saat perjalanan. Tolong cek foto kerusakannya ya kak: [Lihat_Foto_Paket.apk]", "TEXT");
        s2_l1.tambahPilihanRespons(new PilihanRespons(8, 4, "Kenapa fotonya berekstensi .apk? Saya tidak akan install file aplikasi dari orang asing.", TingkatRisiko.AMAN, 100,
                "Luar biasa waspada! Foto asli berformat .jpg atau .png, bukan berkas aplikasi .apk.", "Penipu gagal menyusupkan malware ke ponsel pintar Anda.", 2));
        s2_l1.tambahPilihanRespons(new PilihanRespons(9, 4, "Waduh rusak ya? Oke langsung saya unduh dan pasang aplikasinya biar bisa lihat fotonya.", TingkatRisiko.BAHAYA, 0,
                "BAHAYA BESAR! Berkas .APK ini berisi trojan pengirim SMS (SMS forwarder) yang akan menyadap OTP mobile banking Anda.", "Malware terpasang dan mengakses izin SMS perbankan Anda.", 2));

        LangkahSkenario s2_l2 = new LangkahSkenario(5, 2, 2, "Kurir JNT Express - Mas Agus",
                "Iya kak itu aplikasi sistem komplain resmi ekspedisi kami. Kalau tidak di-install sekarang paket akan kami retur hangus ke gudang pusat lho!", "TEXT");
        s2_l2.tambahPilihanRespons(new PilihanRespons(10, 5, "Saya akan cek nomor resi paket saya langsung melalui aplikasi resmi J&T Express di Google Play Store.", TingkatRisiko.AMAN, 100,
                "Tindakan paling cerdas! Verifikasi mandiri di kanal resmi menggagalkan manipulasi kurir gadungan.", "Akun bank dan perangkat Anda sepenuhnya aman.", null));
        s2_l2.tambahPilihanRespons(new PilihanRespons(11, 5, "Ya sudah saya izinkan akses install dari sumber tak dikenal (unknown sources).", TingkatRisiko.BAHAYA, 0,
                "FATAL! Memberi izin instalasi aplikasi luar toko resmi membuka celah kendali total bagi peretas.", "Peretas berhasil menyadap seluruh SMS notifikasi perbankan Anda.", null));

        s2.tambahLangkah(s2_l1);
        s2.tambahLangkah(s2_l2);
        mockSkenarioList.add(s2);

        // =========================================================================
        // SKENARIO PESAN 3: Surat Tilang Elektronik (ETLE) Palsu via WhatsApp
        // =========================================================================
        Skenario s3 = new Skenario(3, "Surat Tilang Elektronik (ETLE) Palsu Kepolisian",
                "Penipu mengaku Korlantas Polri mengirimkan surat konfirmasi pelanggaran tilang dalam format .APK dengan ancaman denda dan blokir STNK.",
                JenisSkenario.PESAN, "Malware / Impersonasi Instansi", TingkatKesulitan.SULIT, true);

        LangkahSkenario s3_l1 = new LangkahSkenario(6, 3, 1, "KORLANTAS POLRI RESMI",
                "PEMBERITAHUAN TILANG ELEKTRONIK: Kendaraan Anda tertangkap kamera ETLE melanggar batas kecepatan. Silakan unduh Surat_Pemberitahuan_ETLE.apk untuk melihat foto bukti dan nomor bayar tilang dalam 24 jam sebelum STNK diblokir.", "TEXT");
        s3_l1.tambahPilihanRespons(new PilihanRespons(12, 6, "Kepolisian RI tidak pernah mengirim surat tilang lewat WhatsApp atau berkas .apk. Saya akan cek di situs etle-korlantas.info.", TingkatRisiko.AMAN, 100,
                "Sangat tepat! Tilang resmi dikirim melalui surat pos fisik ke alamat sesuai STNK atau dicek mandiri di situs resmi.", "Anda tidak termakan intimidasi kepolisian palsu.", null));
        s3_l1.tambahPilihanRespons(new PilihanRespons(13, 6, "Takut STNK diblokir, saya langsung unduh dan buka Surat_Pemberitahuan_ETLE.apk.", TingkatRisiko.BAHAYA, 0,
                "Sangat berbahaya! Rasa panik karena ancaman hukum dimanfaatkan penipu agar Anda mengabaikan akal sehat.", "Aplikasi pencuri data terpasang pada perangkat Anda.", null));
        s3_l1.tambahPilihanRespons(new PilihanRespons(14, 6, "Pak polisi, saya melanggar di mana ya? Bisa damai di tempat saja?", TingkatRisiko.WASPADA, 40,
                "Menanggapi penipu membuat Anda tetap menjadi target rekayasa sosial lebih lanjut.", "Penipu akan memberikan nomor rekening pribadi untuk transfer uang 'damai'.", null));

        s3.tambahLangkah(s3_l1);
        mockSkenarioList.add(s3);

        // =========================================================================
        // SKENARIO PESAN 4: Pemenang Undian Marketplace & Phishing Website
        // =========================================================================
        Skenario s4 = new Skenario(4, "Pemenang Undian Berkah Belanja Marketplace Rp10.000.000",
                "Pesan SMS/WA menyatakan Anda menang undian ratusan juta dan diarahkan ke situs tiruan untuk memasukkan PIN dan OTP akun.",
                JenisSkenario.PESAN, "Phishing Website", TingkatKesulitan.MUDAH, true);

        LangkahSkenario s4_l1 = new LangkahSkenario(7, 4, 1, "Shopee Care Promo",
                "Selamat! Nomor WhatsApp Anda terpilih mendapatkan Grand Prize Rp10.000.000 dari Gebyar Promo Berkah. Silakan klaim hadiah Anda melalui portal resmi: https://shopee-klaim-hadiah2026.online sebelum pukul 23:59!", "TEXT");
        s4_l1.tambahPilihanRespons(new PilihanRespons(15, 7, "Saya tidak pernah mengikuti undian ini. Domain web mencurigakan (bukan shopee.co.id). Ini penipuan!", TingkatRisiko.AMAN, 100,
                "Tepat sekali! Domain resmi hanya shopee.co.id. Waspadai domain palsu yang dibuat mirip.", "Kredensial akun belanja dan dompet digital Anda aman.", null));
        s4_l1.tambahPilihanRespons(new PilihanRespons(16, 7, "Alhamdulillah! Saya klik linknya dan masukkan password serta nomor telepon untuk klaim.", TingkatRisiko.BAHAYA, 0,
                "FATAL! Anda menyerahkan kata sandi akun ke situs phishing penipu.", "Akun ShopeePay/dompet digital Anda langsung dikuras oleh penipu.", null));

        s4.tambahLangkah(s4_l1);
        mockSkenarioList.add(s4);

        // =========================================================================
        // SKENARIO TELEPON 5: Panggilan Darurat Bank & Permintaan Kode OTP
        // =========================================================================
        Skenario s5 = new Skenario(5, "Panggilan Darurat Bank: Pemblokiran Rekening & Minta OTP",
                "Penipu menelepon mengaku dari customer service bank mengabarkan ada transaksi liar dan mendesak korban menyebutkan 6 digit kode OTP.",
                JenisSkenario.TELEPON, "Impersonasi Bank / Social Engineering", TingkatKesulitan.SEDANG, true);

        LangkahSkenario s5_l1 = new LangkahSkenario(8, 5, 1, "Panggilan Masuk: +62-812-9988-7711 (Mengaku HaloBCA)",
                "Selamat siang Bapak/Ibu. Kami mendeteksi adanya transaksi mencurigakan senilai Rp7.500.000 di toko online luar negeri dari kartu debit Anda. Apakah benar Anda yang melakukan transaksi tersebut?", "AUDIO_CALL");
        s5_l1.tambahPilihanRespons(new PilihanRespons(17, 8, "Saya akan matikan panggilan ini dan menghubungi call center resmi HaloBCA di 1500888 secara mandiri.", TingkatRisiko.AMAN, 100,
                "Sempurna! Langkah verifikasi dua arah adalah standar emas menghadapi social engineering perbankan.", "Penipu gagal memanfaatkan faktor kepanikan Anda.", null));
        s5_l1.tambahPilihanRespons(new PilihanRespons(18, 8, "Tidak benar! Saya tidak pernah belanja itu! Tolong batalkan sekarang juga Pak!", TingkatRisiko.WASPADA, 50,
                "Reaksi panik inilah yang dimanfaatkan penipu untuk menciptakan rasa genting (urgency). Tenangkan diri.", "Penipu akan langsung memanfaatkan kepanikan Anda untuk meminta OTP.", 2));

        LangkahSkenario s5_l2 = new LangkahSkenario(9, 5, 2, "Panggilan Masuk: +62-812-9988-7711 (Mengaku HaloBCA)",
                "Baik untuk membatalkan transaksi dan mengamankan rekening, sistem kami baru saja mengirim 6 digit kode pembatalan ke SMS Anda. Tolong sebutkan kodenya sekarang juga agar uang tidak terpotong!", "AUDIO_CALL");
        s5_l2.tambahPilihanRespons(new PilihanRespons(19, 9, "SMS yang masuk tertulis 'KODE OTP RAHASIA, JANGAN BERIKAN KEPADA SIAPAPUN TERMASUK PIHAK BANK'. Saya tidak akan sebutkan!", TingkatRisiko.AMAN, 100,
                "Luar biasa! Pihak bank TIDAK PERNAH meminta kode OTP atau PIN Anda dalam kondisi apapun.", "Rekening dan saldo tabungan Anda terselamatkan dari pembobolan.", null));
        s5_l2.tambahPilihanRespons(new PilihanRespons(20, 9, "Kodenya 894321, tolong segera dibatalkan transaksinya ya Pak!", TingkatRisiko.BAHAYA, 0,
                "SANGAT BERBAHAYA! Kode tersebut adalah OTP otorisasi transaksi yang justru menyetujui penarikan uang oleh penipu.", "Saldo rekening Anda terkuras habis oleh pelaku kejahatan.", null));

        s5.tambahLangkah(s5_l1);
        s5.tambahLangkah(s5_l2);
        mockSkenarioList.add(s5);

        // =========================================================================
        // SKENARIO TELEPON 6: Kenaikan Tarif Transaksi Bank Rp150.000/Bulan
        // =========================================================================
        Skenario s6 = new Skenario(6, "Perubahan Tarif Biaya Transaksi Bank Jadi Rp150.000/Bulan",
                "Penipu mengabarkan ada perubahan skema tarif bulanan bank yang mahal. Jika nasabah menolak, diarahkan mengisi formulir phishing.",
                JenisSkenario.TELEPON, "Manipulasi Biaya Administrasi", TingkatKesulitan.SEDANG, true);

        LangkahSkenario s6_l1 = new LangkahSkenario(10, 6, 1, "Panggilan Masuk: +62-821-4455-6677 (Customer Care BRI)",
                "Selamat pagi Nasabah Terhormat, kami menginformasikan per malam ini ada pembaruan tarif transaksi antarbank menjadi Rp150.000 per bulan yang didebet otomatis. Apakah Bapak/Ibu setuju dengan kenaikan tarif ini?", "AUDIO_CALL");
        s6_l1.tambahPilihanRespons(new PilihanRespons(21, 10, "Saya tidak setuju! Kenapa mahal sekali? Batalkan perubahan tarif rekening saya!", TingkatRisiko.WASPADA, 50,
                "Penipu sengaja membuat tarif tidak masuk akal agar korban emosi dan terburu-buru minta pembatalan.", "Penipu mengarahkan Anda ke formulir pembatalan palsu.", 2));
        s6_l1.tambahPilihanRespons(new PilihanRespons(22, 10, "Setahu saya kebijakan bank diumumkan via website resmi. Saya akan tanyakan langsung ke kantor cabang.", TingkatRisiko.AMAN, 100,
                "Langkah perlindungan terbaik! Tidak gegabah merespons informasi sepihak dari nomor seluler biasa.", "Penipu menyadari modusnya gagal memicu kepanikan Anda.", null));

        LangkahSkenario s6_l2 = new LangkahSkenario(11, 6, 2, "Panggilan Masuk: +62-821-4455-6677 (Customer Care BRI)",
                "Baik jika tidak setuju, kami bantu batalkan. Kami kirimkan tautan form pembatalan via SMS ya. Nanti silakan masukkan nomor kartu ATM, masa berlaku, dan 3 digit CVV di belakang kartu.", "AUDIO_CALL");
        s6_l2.tambahPilihanRespons(new PilihanRespons(23, 11, "3 digit CVV di belakang kartu dan nomor kartu ATM adalah data rahasia perbankan! Anda penipu!", TingkatRisiko.AMAN, 100,
                "Cerdas! CVV kartu ATM/Debit adalah kunci otorisasi pembayaran online yang tidak boleh dibagikan kepada siapapun.", "Anda berhasil melindungi data kartu pembayaran Anda.", null));
        s6_l2.tambahPilihanRespons(new PilihanRespons(24, 11, "Baik saya isi formnya lengkap dengan nomor kartu dan CVV-nya.", TingkatRisiko.BAHAYA, 0,
                "FATAL! Data kartu pembayaran Anda dapat langsung dipakai penipu untuk berbelanja online tanpa izin.", "Kartu debit Anda disalahgunakan untuk transaksi ilegal.", null));

        s6.tambahLangkah(s6_l1);
        s6.tambahLangkah(s6_l2);
        mockSkenarioList.add(s6);

        // =========================================================================
        // SKENARIO TELEPON 7: Impersonasi Kerabat Ditangkap Polisi / Musibah
        // =========================================================================
        Skenario s7 = new Skenario(7, "Impersonasi Keluarga Mengalami Musibah / Kecelakaan",
                "Penipu berpura-pura menjadi polisi atau petugas rumah sakit, mengabarkan kerabat terdekat kecelakaan/ditangkap dan butuh transfer uang kilat.",
                JenisSkenario.TELEPON, "Social Engineering Emosional", TingkatKesulitan.SULIT, true);

        LangkahSkenario s7_l1 = new LangkahSkenario(12, 7, 1, "Panggilan Darurat: +62-813-7788-9900 (Petugas Rumah Sakit / Polisi)",
                "Halo! Ini dengan keluarga Andi? Andi baru saja mengalami kecelakaan parah dan pendarahan hebat di jalan raya! Kami butuh uang operasi darurat Rp5.000.000 sekarang ke rekening dokter sebelum terlambat!", "AUDIO_CALL");
        s7_l1.tambahPilihanRespons(new PilihanRespons(25, 12, "Tolong sebutkan nama rumah sakitnya. Saya akan telepon nomor Andi sendiri dan minta kerabat dekat mendatangi rumah sakit tersebut.", TingkatRisiko.AMAN, 100,
                "Sangat tenang dan bijaksana! Modus kecelakaan menargetkan kepanikan emosional keluarga. Verifikasi ke nomor pribadi kerabat adalah kunci.", "Penipu gagal mengeksploitasi kepanikan keluarga.", null));
        s7_l1.tambahPilihanRespons(new PilihanRespons(26, 12, "Ya Tuhan Andi kecelakaan?! Tolong selamatkan dia, kirim nomor rekeningnya saya transfer uangnya sekarang!", TingkatRisiko.BAHAYA, 0,
                "Berbahaya! Emosi panik membuat korban tidak berpikir kritis dan langsung mentransfer uang ke rekening penipu.", "Uang jutaan rupiah melayang ke rekening penampung penipu.", null));

        s7.tambahLangkah(s7_l1);
        mockSkenarioList.add(s7);
    }

    @Override
    public List<Skenario> getDaftarSkenarioAktif() {
        return mockSkenarioList;
    }

    @Override
    public List<Skenario> getDaftarSkenarioBerdasarkanJenis(JenisSkenario jenis) {
        return mockSkenarioList.stream().filter(s -> s.getJenis() == jenis).toList();
    }

    @Override
    public Optional<Skenario> getDetailSkenarioLengkap(int skenarioId) {
        return mockSkenarioList.stream().filter(s -> s.getId() == skenarioId).findFirst();
    }

    @Override
    public Simulasi inisialisasiSimulasi(int skenarioId) {
        Skenario s = getDetailSkenarioLengkap(skenarioId).orElse(mockSkenarioList.get(0));
        Simulasi sim = (s.getJenis() == JenisSkenario.TELEPON) ? new SimulasiTelepon(s) : new SimulasiPesan(s);
        sim.jalankan();
        return sim;
    }

    @Override
    public boolean simpanHasilSimulasi(HasilSimulasi hasilSimulasi) {
        hasilSimulasi.setId(mockRiwayatList.size() + 1);
        mockRiwayatList.add(hasilSimulasi);
        return true;
    }

    @Override
    public List<HasilSimulasi> getRiwayatSimulasiPengguna(int penggunaId) {
        return mockRiwayatList;
    }
}
