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
 */
public class MockSimulasiService implements ISimulasiService {
    private final List<Skenario> mockSkenarioList = new ArrayList<>();
    private final List<HasilSimulasi> mockRiwayatList = new ArrayList<>();

    public MockSimulasiService() {
        initMockData();
    }

    private void initMockData() {
        // Skenario Pesan
        Skenario s1 = new Skenario(1, "Tawaran Kerja Freelance Like Video (Offline Mock)",
                "Penipuan berkedok tugas like video dengan komisi menggiurkan.",
                JenisSkenario.PESAN, "Lowongan Kerja Palsu", TingkatKesulitan.MUDAH, true);

        LangkahSkenario l1 = new LangkahSkenario(1, 1, 1, "Recruiter Sarah",
                "Halo Kak! Tertarik freelance like video YouTube dapat Rp150.000/hari?", "TEXT");
        l1.tambahPilihanRespons(new PilihanRespons(1, 1, "Halo, dari mana dapat nomor saya? Ini perusahaan apa?", TingkatRisiko.AMAN, 100, "Bagus! Kritis terhadap pengirim asing.", "Penipu waspada.", 2));
        l1.tambahPilihanRespons(new PilihanRespons(2, 1, "Wah mau banget! Caranya gimana kak?", TingkatRisiko.BAHAYA, 0, "Bahaya! Jangan mudah percaya tugas instan.", "Anda masuk perangkap penipu.", 2));

        LangkahSkenario l2 = new LangkahSkenario(2, 1, 2, "Recruiter Sarah",
                "Silakan klik link https://s.id/tugas-video dan like ya. Lalu transfer deposit Rp200.000 untuk komisi VIP.", "TEXT");
        l2.tambahPilihanRespons(new PilihanRespons(3, 2, "Pekerjaan yang minta deposit adalah penipuan. Saya tolak!", TingkatRisiko.AMAN, 100, "Hebat! Anda terhindar dari task scam.", "Uang Anda selamat.", null));
        l2.tambahPilihanRespons(new PilihanRespons(4, 2, "Oke saya transfer depositnya sekarang ya.", TingkatRisiko.BAHAYA, 0, "FATAL! Uang Anda akan hilang.", "Kerugian finansial.", null));

        s1.tambahLangkah(l1);
        s1.tambahLangkah(l2);
        mockSkenarioList.add(s1);

        // Skenario Telepon
        Skenario s2 = new Skenario(2, "Panggilan Darurat Bank: Pemblokiran Rekening (Offline Mock)",
                "Panggilan penipuan mengaku call center bank meminta kode OTP.",
                JenisSkenario.TELEPON, "Impersonasi Bank", TingkatKesulitan.SEDANG, true);

        LangkahSkenario l3 = new LangkahSkenario(3, 2, 1, "Call Center Bank +628129999",
                "Ada transaksi mencurigakan Rp5.000.000 di rekening Anda! Sebutkan kode SMS 6 digit sekarang untuk batalkan!", "AUDIO_CALL");
        l3.tambahPilihanRespons(new PilihanRespons(5, 3, "Kode SMS tertulis RAHASIA dan jangan dibagikan ke siapapun termasuk bank!", TingkatRisiko.AMAN, 100, "Tepat sekali! Bank tidak pernah minta OTP.", "Rekening aman.", null));
        l3.tambahPilihanRespons(new PilihanRespons(6, 3, "Kodenya 123456, tolong batalkan sekarang!", TingkatRisiko.BAHAYA, 0, "Bahaya besar! Ini adalah OTP transaksi penipu.", "Saldo rekening terpotong.", null));

        s2.tambahLangkah(l3);
        mockSkenarioList.add(s2);
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
