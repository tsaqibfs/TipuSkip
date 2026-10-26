package com.tipuskip.simulation;

import com.tipuskip.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SimulasiPesanTest {

    private Skenario skenarioMock;
    private SimulasiPesan simulasi;

    @BeforeEach
    public void setup() {
        skenarioMock = new Skenario(1, "Test Phishing Link", "Deskripsi test",
                JenisSkenario.PESAN, "Phishing", TingkatKesulitan.MUDAH, true);

        LangkahSkenario l1 = new LangkahSkenario(1, 1, 1, "Penipu", "Klik link ini: https://s.id/dana-kaget", "TEXT");
        l1.tambahPilihanRespons(new PilihanRespons(1, 1, "Tolak dan laporkan", TingkatRisiko.AMAN, 100, "Bagus", "Aman", null));
        l1.tambahPilihanRespons(new PilihanRespons(2, 1, "Buka link sekarang", TingkatRisiko.BAHAYA, 0, "Bahaya", "Rugi", null));

        skenarioMock.tambahLangkah(l1);
        simulasi = new SimulasiPesan(skenarioMock);
        simulasi.jalankan();
    }

    @Test
    @DisplayName("PBO Polymorphism & Scoring: Respons Aman menghasilkan skor 100 dan LULUS_AMAN")
    public void testResponsAman() {
        LangkahSkenario langkah = simulasi.getLangkahSaatIni();
        assertNotNull(langkah);

        PilihanRespons responsAman = langkah.getDaftarPilihanRespons().get(0);
        boolean lanjut = simulasi.prosesRespons(responsAman);

        assertFalse(lanjut, "Simulasi harusnya selesai karena hanya ada 1 langkah.");
        assertTrue(simulasi.isSelesai());
        assertEquals(100, simulasi.hitungSkor());
        assertEquals("LULUS_AMAN", simulasi.evaluasiHasil());
        assertEquals(0, simulasi.getJumlahKlikLinkBahaya());
    }

    @Test
    @DisplayName("PBO Polymorphism & Scoring: Respons Bahaya menghasilkan skor 0 dan BERISIKO_TINGGI")
    public void testResponsBahaya() {
        LangkahSkenario langkah = simulasi.getLangkahSaatIni();
        PilihanRespons responsBahaya = langkah.getDaftarPilihanRespons().get(1);

        simulasi.prosesRespons(responsBahaya);

        assertTrue(simulasi.isSelesai());
        assertEquals(0, simulasi.hitungSkor());
        assertEquals("BERISIKO_TINGGI", simulasi.evaluasiHasil());
        assertEquals(1, simulasi.getJumlahKlikLinkBahaya());
    }
}
