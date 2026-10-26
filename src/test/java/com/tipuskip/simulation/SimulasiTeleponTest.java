package com.tipuskip.simulation;

import com.tipuskip.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test untuk SimulasiTelepon (Penerapan PBO Pewarisan & Polimorfisme).
 * Hak Akses: Anggota 1.
 */
public class SimulasiTeleponTest {

    private Skenario skenarioTeleponMock;
    private SimulasiTelepon simulasiTelepon;

    @BeforeEach
    public void setup() {
        skenarioTeleponMock = new Skenario(2, "Test Panggilan Bank Minta OTP", "Deskripsi test telepon",
                JenisSkenario.TELEPON, "Impersonasi Bank", TingkatKesulitan.SEDANG, true);

        LangkahSkenario l1 = new LangkahSkenario(1, 2, 1, "Call Center Palsu", "Sebutkan 6 digit kode OTP Anda!", "AUDIO_CALL");
        l1.tambahPilihanRespons(new PilihanRespons(1, 1, "Saya akan telepon call center resmi bank saya secara mandiri.", TingkatRisiko.AMAN, 100, "Bagus!", "Aman", null));
        l1.tambahPilihanRespons(new PilihanRespons(2, 1, "Kodenya 123456 tolong bantu batalkan.", TingkatRisiko.BAHAYA, 0, "Bahaya!", "Kuras Saldo", null));

        skenarioTeleponMock.tambahLangkah(l1);
        simulasiTelepon = new SimulasiTelepon(skenarioTeleponMock);
        simulasiTelepon.jalankan();
    }

    @Test
    @DisplayName("PBO Polymorphism: Verifikasi Mandiri menghasilkan kelulusan aman")
    public void testPanggilanAmanVerifikasiMandiri() {
        LangkahSkenario langkah = simulasiTelepon.getLangkahSaatIni();
        assertNotNull(langkah);

        PilihanRespons responsAman = langkah.getDaftarPilihanRespons().get(0);
        boolean lanjut = simulasiTelepon.prosesRespons(responsAman);

        assertFalse(lanjut, "Simulasi telepon selesai karena langkah telah berakhir.");
        assertTrue(simulasiTelepon.isSelesai());
        assertEquals(100, simulasiTelepon.hitungSkor());
        assertEquals("LULUS_AMAN", simulasiTelepon.evaluasiHasil());
        assertTrue(simulasiTelepon.dapatkanRingkasanPembelajaran().contains("LULUS_AMAN"));
    }

    @Test
    @DisplayName("PBO Polymorphism: Kebocoran OTP memicu penalti skor maksimal 40 dan status BERISIKO_TINGGI")
    public void testPanggilanBocorOTP() {
        LangkahSkenario langkah = simulasiTelepon.getLangkahSaatIni();
        PilihanRespons responsBahaya = langkah.getDaftarPilihanRespons().get(1);

        simulasiTelepon.prosesRespons(responsBahaya);

        assertTrue(simulasiTelepon.isSelesai());
        assertTrue(simulasiTelepon.hitungSkor() <= 40, "Skor harus dikenakan penalti akibat membocorkan OTP.");
        assertEquals("BERISIKO_TINGGI", simulasiTelepon.evaluasiHasil());
        assertTrue(simulasiTelepon.dapatkanRingkasanPembelajaran().contains("Kebocoran Kredensial: YA"));
    }
}
