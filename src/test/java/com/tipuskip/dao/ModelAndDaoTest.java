package com.tipuskip.dao;

import com.tipuskip.dao.impl.*;
import com.tipuskip.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test untuk Verifikasi Enkapsulasi POJO Model dan DAO Pattern 8 Entitas.
 * Bagian Tanggung Jawab Anggota 2.
 */
public class ModelAndDaoTest {

    @Test
    @DisplayName("Test 1: Enkapsulasi Model Pengguna")
    void testPenggunaEncapsulation() {
        Pengguna user = new Pengguna();
        user.setId(1);
        user.setUsername("ayuni");
        user.setEmail("ayuni@example.com");
        user.setPasswordHash("hashedpassword");
        user.setRole(Role.ADMIN);
        user.setCreatedAt(LocalDateTime.now());

        assertEquals(1, user.getId());
        assertEquals("ayuni", user.getUsername());
        assertEquals("ayuni@example.com", user.getEmail());
        assertEquals("hashedpassword", user.getPasswordHash());
        assertEquals(Role.ADMIN, user.getRole());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    @DisplayName("Test 2: Enkapsulasi Model Skenario")
    void testSkenarioEncapsulation() {
        Skenario skenario = new Skenario();
        skenario.setId(10);
        skenario.setJudul("Modus Undian Berhadiah");
        skenario.setDeskripsi("SMS penipuan undian");
        skenario.setJenis(JenisSkenario.PESAN);
        skenario.setKategori("Phishing Hadiah");
        skenario.setTingkatKesulitan(TingkatKesulitan.MUDAH);
        skenario.setStatusAktif(true);

        LangkahSkenario l1 = new LangkahSkenario();
        l1.setId(1);
        l1.setNomorLangkah(1);
        skenario.tambahLangkah(l1);

        assertEquals(10, skenario.getId());
        assertEquals("Modus Undian Berhadiah", skenario.getJudul());
        assertEquals(JenisSkenario.PESAN, skenario.getJenis());
        assertEquals(1, skenario.getDaftarLangkah().size());
        assertTrue(skenario.isStatusAktif());
    }

    @Test
    @DisplayName("Test 3: Enkapsulasi Model LangkahSkenario")
    void testLangkahSkenarioEncapsulation() {
        LangkahSkenario langkah = new LangkahSkenario(1, 10, 1, "Penipu", "Kirimkan kode OTP sekarang", "TEXT");
        PilihanRespons p1 = new PilihanRespons();
        p1.setId(101);
        p1.setTeksRespons("Jangan kirim");
        p1.setTingkatRisiko(TingkatRisiko.AMAN);
        langkah.tambahPilihanRespons(p1);

        assertEquals(1, langkah.getId());
        assertEquals(10, langkah.getSkenarioId());
        assertEquals(1, langkah.getNomorLangkah());
        assertEquals("Penipu", langkah.getNamaPengirim());
        assertEquals("Kirimkan kode OTP sekarang", langkah.getPesanPengirim());
        assertEquals(1, langkah.getDaftarPilihanRespons().size());
    }

    @Test
    @DisplayName("Test 4: Enkapsulasi Model PilihanRespons")
    void testPilihanResponsEncapsulation() {
        PilihanRespons resp = new PilihanRespons(1, 10, "Beri OTP", TingkatRisiko.BAHAYA, 0, "Fatal!", "Akun dibobol", null);

        assertEquals(1, resp.getId());
        assertEquals(10, resp.getLangkahId());
        assertEquals("Beri OTP", resp.getTeksRespons());
        assertEquals(TingkatRisiko.BAHAYA, resp.getTingkatRisiko());
        assertEquals(0, resp.getPoinSkor());
        assertEquals("Fatal!", resp.getUmpanBalik());
        assertEquals("Akun dibobol", resp.getKonsekuensi());
        assertNull(resp.getLangkahBerikutnya());
    }

    @Test
    @DisplayName("Test 5: Enkapsulasi Model HasilSimulasi")
    void testHasilSimulasiEncapsulation() {
        HasilSimulasi hasil = new HasilSimulasi();
        hasil.setId(1);
        hasil.setPenggunaId(2);
        hasil.setSkenarioId(3);
        hasil.setJudulSkenario("Simulasi Panggilan");
        hasil.setTotalSkor(100);
        hasil.setStatusEvaluasi("SEMPURNA");
        hasil.setDurasiDetik(45);
        hasil.setWaktuSelesai(LocalDateTime.now());

        assertEquals(1, hasil.getId());
        assertEquals(2, hasil.getPenggunaId());
        assertEquals(3, hasil.getSkenarioId());
        assertEquals(100, hasil.getTotalSkor());
        assertEquals("SEMPURNA", hasil.getStatusEvaluasi());
        assertEquals(45, hasil.getDurasiDetik());
        assertNotNull(hasil.getWaktuSelesai());
    }

    @Test
    @DisplayName("Test 6: Enkapsulasi Model IndikatorPenipuan")
    void testIndikatorPenipuanEncapsulation() {
        IndikatorPenipuan ind = new IndikatorPenipuan(1, "Kata Kunci OTP", "(otp|kode)", TipePencocokan.REGEX, 40, "Pola OTP", "Jangan berikan");

        assertEquals(1, ind.getId());
        assertEquals("Kata Kunci OTP", ind.getNamaIndikator());
        assertEquals("(otp|kode)", ind.getPolaPencocokan());
        assertEquals(TipePencocokan.REGEX, ind.getTipePencocokan());
        assertEquals(40, ind.getBobotRisiko());
        assertTrue(ind.isStatusAktif());
    }

    @Test
    @DisplayName("Test 7: Enkapsulasi Model AnalisisTangkapanLayar")
    void testAnalisisTangkapanLayarEncapsulation() {
        AnalisisTangkapanLayar atl = new AnalisisTangkapanLayar();
        atl.setId(1);
        atl.setPenggunaId(2);
        atl.setNamaFile("ss_chat.png");
        atl.setTeksEkstraksi("Menang hadiah 50 juta klik link!");
        atl.setTingkatRisiko(TingkatRisikoAnalisis.TINGGI);
        atl.setTotalSkorRisiko(80);
        atl.setAlasanPenilaian("Terdapat pola hadiah dan shortlink");
        atl.setSaranVerifikasi("Cek website resmi penyelenggara");

        DetailAnalisis da = new DetailAnalisis(1, 1, 1, "hadiah", 30);
        atl.tambahDetail(da);

        assertEquals(1, atl.getId());
        assertEquals(2, atl.getPenggunaId());
        assertEquals("ss_chat.png", atl.getNamaFile());
        assertEquals(TingkatRisikoAnalisis.TINGGI, atl.getTingkatRisiko());
        assertEquals(80, atl.getTotalSkorRisiko());
        assertEquals(1, atl.getDaftarDetail().size());
    }

    @Test
    @DisplayName("Test 8: Enkapsulasi Model DetailAnalisis")
    void testDetailAnalisisEncapsulation() {
        IndikatorPenipuan ip = new IndikatorPenipuan();
        ip.setId(5);
        ip.setNamaIndikator("Permintaan Transfer");
        ip.setBobotRisiko(35);

        DetailAnalisis da = new DetailAnalisis(1, ip, "transfer segera");

        assertEquals(1, da.getAnalisisId());
        assertEquals(5, da.getIndikatorId());
        assertEquals(35, da.getBobotTerhitung());
        assertEquals("transfer segera", da.getTeksDitemukan());
        assertNotNull(da.getIndikator());
    }

    @Test
    @DisplayName("Test 9: Inisialisasi DAO Pattern 8 Entitas")
    void testDaoPatternInitialization() {
        PenggunaDAO penggunaDAO = new PenggunaDAOImpl();
        SkenarioDAO skenarioDAO = new SkenarioDAOImpl();
        LangkahSkenarioDAO langkahDAO = new LangkahSkenarioDAOImpl();
        PilihanResponsDAO responsDAO = new PilihanResponsDAOImpl();
        HasilSimulasiDAO hasilDAO = new HasilSimulasiDAOImpl();
        IndikatorPenipuanDAO indikatorDAO = new IndikatorPenipuanDAOImpl();
        AnalisisTangkapanLayarDAO analisisDAO = new AnalisisTangkapanLayarDAOImpl();
        DetailAnalisisDAO detailDAO = new DetailAnalisisDAOImpl();

        assertNotNull(penggunaDAO);
        assertNotNull(skenarioDAO);
        assertNotNull(langkahDAO);
        assertNotNull(responsDAO);
        assertNotNull(hasilDAO);
        assertNotNull(indikatorDAO);
        assertNotNull(analisisDAO);
        assertNotNull(detailDAO);
    }
}
