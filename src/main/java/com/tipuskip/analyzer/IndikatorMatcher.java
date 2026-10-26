package com.tipuskip.analyzer;

import com.tipuskip.model.DetailAnalisis;
import com.tipuskip.model.IndikatorPenipuan;
import com.tipuskip.model.TipePencocokan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mesin Pencocok Pola Indikator Penipuan (Keyword & Regex Matching Engine).
 * Bagian Tanggung Jawab Anggota 3.
 */
public class IndikatorMatcher {
    private static final Logger logger = LoggerFactory.getLogger(IndikatorMatcher.class);

    /**
     * Mencocokkan teks hasil OCR dengan daftar indikator aktif.
     * @param teksEkstraksi Teks hasil OCR.
     * @param daftarIndikator Daftar indikator penipuan aktif dari basis data.
     * @return Daftar DetailAnalisis yang berisi indikator yang cocok beserta potongan teksnya.
     */
    public List<DetailAnalisis> cocokkan(String teksEkstraksi, List<IndikatorPenipuan> daftarIndikator) {
        List<DetailAnalisis> hasil = new ArrayList<>();
        if (teksEkstraksi == null || teksEkstraksi.isBlank() || daftarIndikator == null) {
            return hasil;
        }

        String teksLower = teksEkstraksi.toLowerCase();

        for (IndikatorPenipuan ind : daftarIndikator) {
            if (!ind.isStatusAktif()) continue;

            try {
                if (ind.getTipePencocokan() == TipePencocokan.REGEX) {
                    Pattern pattern = Pattern.compile(ind.getPolaPencocokan(), Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(teksEkstraksi);
                    if (matcher.find()) {
                        String matchedText = matcher.group();
                        DetailAnalisis detail = new DetailAnalisis();
                        detail.setIndikator(ind);
                        detail.setTeksDitemukan(matchedText);
                        detail.setBobotTerhitung(ind.getBobotRisiko());
                        hasil.add(detail);
                    }
                } else {
                    // KEYWORD MATCHING
                    String keyword = ind.getPolaPencocokan().toLowerCase();
                    if (teksLower.contains(keyword)) {
                        DetailAnalisis detail = new DetailAnalisis();
                        detail.setIndikator(ind);
                        detail.setTeksDitemukan(keyword);
                        detail.setBobotTerhitung(ind.getBobotRisiko());
                        hasil.add(detail);
                    }
                }
            } catch (Exception e) {
                logger.warn("Pola regex/keyword tidak valid untuk indikator id {}: {}", ind.getId(), e.getMessage());
            }
        }

        return hasil;
    }
}
