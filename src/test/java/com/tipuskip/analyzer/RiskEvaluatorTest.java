package com.tipuskip.analyzer;

import com.tipuskip.model.DetailAnalisis;
import com.tipuskip.model.IndikatorPenipuan;
import com.tipuskip.model.TipePencocokan;
import com.tipuskip.model.TingkatRisikoAnalisis;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RiskEvaluatorTest {

    @Test
    @DisplayName("Pencocokan Indikator Regex OTP dan Shortlink berhasil dideteksi")
    public void testIndikatorMatcher() {
        IndikatorMatcher matcher = new IndikatorMatcher();

        List<IndikatorPenipuan> indikatorList = List.of(
                new IndikatorPenipuan(1, "OTP Request", "(otp|kode verifikasi)", TipePencocokan.REGEX, 40, "Minta OTP", "Jangan beri", true),
                new IndikatorPenipuan(2, "Shortlink", "(s\\.id|bit\\.ly)", TipePencocokan.REGEX, 25, "Shortlink", "Cek link", true)
        );

        String sampleText = "Halo, segera masukkan kode verifikasi Anda ke situs https://s.id/verif sekarang.";
        List<DetailAnalisis> matches = matcher.cocokkan(sampleText, indikatorList);

        assertEquals(2, matches.size(), "Harus mendeteksi 2 indikator.");
        assertTrue(matches.stream().anyMatch(m -> m.getTeksDitemukan().equalsIgnoreCase("kode verifikasi")));
        assertTrue(matches.stream().anyMatch(m -> m.getTeksDitemukan().equalsIgnoreCase("s.id")));
    }

    @Test
    @DisplayName("Penilaian Risiko: Akumulasi skor >= 55 menghasilkan Tingkat Risiko TINGGI")
    public void testEvaluasiRisikoTinggi() {
        RiskEvaluator evaluator = new RiskEvaluator();
        List<DetailAnalisis> list = new ArrayList<>();

        IndikatorPenipuan ind1 = new IndikatorPenipuan(1, "OTP", "otp", TipePencocokan.KEYWORD, 40, "Minta OTP", "Jangan beri", true);
        IndikatorPenipuan ind2 = new IndikatorPenipuan(2, "APK", "apk", TipePencocokan.KEYWORD, 30, "File APK", "Jangan install", true);

        list.add(new DetailAnalisis(1, ind1, "otp"));
        list.add(new DetailAnalisis(1, ind2, "apk"));

        HasilPenilaianRisiko hasil = evaluator.evaluasi(list);

        assertEquals(TingkatRisikoAnalisis.TINGGI, hasil.getTingkatRisiko());
        assertEquals(70, hasil.getTotalSkorRisiko());
        assertTrue(hasil.getAlasanPenilaian().contains("Risiko Tinggi"));
        assertFalse(hasil.getSaranVerifikasi().isBlank());
    }

    @Test
    @DisplayName("Penilaian Risiko: Tanpa indikator menghasilkan Tingkat Risiko RENDAH")
    public void testEvaluasiRisikoRendah() {
        RiskEvaluator evaluator = new RiskEvaluator();
        HasilPenilaianRisiko hasil = evaluator.evaluasi(new ArrayList<>());

        assertEquals(TingkatRisikoAnalisis.RENDAH, hasil.getTingkatRisiko());
        assertEquals(0, hasil.getTotalSkorRisiko());
    }
}
