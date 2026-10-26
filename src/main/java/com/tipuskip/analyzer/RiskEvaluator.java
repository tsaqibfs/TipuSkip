package com.tipuskip.analyzer;

import com.tipuskip.model.DetailAnalisis;
import com.tipuskip.model.TingkatRisikoAnalisis;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Mesin Penilai Risiko Penipuan Digital (Risk Evaluation Engine).
 * Menghitung skor akumulasi bobot, menentukan ambang risiko (RENDAH, SEDANG, TINGGI),
 * serta menyusun alasan penilaian dan rekomendasi verifikasi secara edukatif.
 * Bagian Tanggung Jawab Anggota 3.
 */
public class RiskEvaluator {

    // Ambang Batas Skor Risiko (Business Rule)
    public static final int THRESHOLD_SEDANG = 25;
    public static final int THRESHOLD_TINGGI = 55;

    /**
     * Mengevaluasi daftar detail indikator yang cocok menjadi kesimpulan risiko.
     * @param daftarCocok Daftar indikator yang berhasil terdeteksi pada tangkapan layar.
     * @return HasilPenilaianRisiko lengkap dengan alasan dan saran verifikasi.
     */
    public HasilPenilaianRisiko evaluasi(List<DetailAnalisis> daftarCocok) {
        if (daftarCocok == null || daftarCocok.isEmpty()) {
            return new HasilPenilaianRisiko(
                    TingkatRisikoAnalisis.RENDAH,
                    0,
                    "Tidak ditemukan kata kunci atau pola indikator penipuan yang mencurigakan pada tangkapan layar.",
                    "Tetap waspada terhadap komunikasi digital. Pastikan bertransaksi hanya melalui aplikasi resmi.",
                    new ArrayList<>()
            );
        }

        int totalSkor = 0;
        List<String> alasanList = new ArrayList<>();
        Set<String> saranSet = new LinkedHashSet<>();

        for (DetailAnalisis da : daftarCocok) {
            totalSkor += da.getBobotTerhitung();
            if (da.getIndikator() != null) {
                alasanList.add(String.format("- Terdeteksi '%s' (pola: \"%s\"): %s",
                        da.getIndikator().getNamaIndikator(),
                        da.getTeksDitemukan(),
                        da.getIndikator().getDeskripsi()));

                if (da.getIndikator().getSaranVerifikasi() != null && !da.getIndikator().getSaranVerifikasi().isBlank()) {
                    saranSet.add(da.getIndikator().getSaranVerifikasi());
                }
            }
        }

        TingkatRisikoAnalisis level;
        if (totalSkor >= THRESHOLD_TINGGI) {
            level = TingkatRisikoAnalisis.TINGGI;
        } else if (totalSkor >= THRESHOLD_SEDANG) {
            level = TingkatRisikoAnalisis.SEDANG;
        } else {
            level = TingkatRisikoAnalisis.RENDAH;
        }

        StringBuilder alasanBuilder = new StringBuilder();
        alasanBuilder.append(String.format("Tingkat risiko dinilai %s (Total Skor Indikator: %d).\n", level.getLabel(), totalSkor));
        alasanBuilder.append("Indikator terdeteksi:\n");
        for (String a : alasanList) {
            alasanBuilder.append(a).append("\n");
        }

        StringBuilder saranBuilder = new StringBuilder();
        int nomor = 1;
        for (String s : saranSet) {
            saranBuilder.append(nomor++).append(". ").append(s).append("\n");
        }
        if (saranSet.isEmpty()) {
            saranBuilder.append("1. Lakukan verifikasi kontak pengirim ke kanal informasi resmi.");
        }

        return new HasilPenilaianRisiko(level, totalSkor, alasanBuilder.toString().trim(), saranBuilder.toString().trim(), daftarCocok);
    }
}
