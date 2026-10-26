package com.tipuskip.analyzer;

import com.tipuskip.model.DetailAnalisis;
import com.tipuskip.model.TingkatRisikoAnalisis;

import java.util.List;

/**
 * Data Transfer Object (DTO) untuk hasil kalkulasi penilaian risiko.
 */
public class HasilPenilaianRisiko {
    private final TingkatRisikoAnalisis tingkatRisiko;
    private final int totalSkorRisiko;
    private final String alasanPenilaian;
    private final String saranVerifikasi;
    private final List<DetailAnalisis> detailAnalisisList;

    public HasilPenilaianRisiko(TingkatRisikoAnalisis tingkatRisiko, int totalSkorRisiko,
                                String alasanPenilaian, String saranVerifikasi,
                                List<DetailAnalisis> detailAnalisisList) {
        this.tingkatRisiko = tingkatRisiko;
        this.totalSkorRisiko = totalSkorRisiko;
        this.alasanPenilaian = alasanPenilaian;
        this.saranVerifikasi = saranVerifikasi;
        this.detailAnalisisList = detailAnalisisList;
    }

    public TingkatRisikoAnalisis getTingkatRisiko() {
        return tingkatRisiko;
    }

    public int getTotalSkorRisiko() {
        return totalSkorRisiko;
    }

    public String getAlasanPenilaian() {
        return alasanPenilaian;
    }

    public String getSaranVerifikasi() {
        return saranVerifikasi;
    }

    public List<DetailAnalisis> getDetailAnalisisList() {
        return detailAnalisisList;
    }
}
