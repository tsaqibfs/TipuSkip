package com.tipuskip.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entitas Hasil Analisis Tangkapan Layar.
 */
public class AnalisisTangkapanLayar {
    private int id;
    private int penggunaId;
    private String namaFile;
    private String teksEkstraksi;
    private TingkatRisikoAnalisis tingkatRisiko;
    private int totalSkorRisiko;
    private String alasanPenilaian;
    private String saranVerifikasi;
    private String disclaimer;
    private LocalDateTime createdAt;
    private List<DetailAnalisis> daftarDetail;

    public static final String DEFAULT_DISCLAIMER =
            "Pernyataan Edukasi: Hasil analisis ini bersifat deteksi awal untuk edukasi dan tidak menetapkan secara pasti bahwa suatu pesan atau pengirim adalah penipu. Selalu verifikasi langsung ke pihak resmi.";

    public AnalisisTangkapanLayar() {
        this.daftarDetail = new ArrayList<>();
        this.tingkatRisiko = TingkatRisikoAnalisis.RENDAH;
        this.disclaimer = DEFAULT_DISCLAIMER;
        this.createdAt = LocalDateTime.now();
    }

    public AnalisisTangkapanLayar(int id, int penggunaId, String namaFile, String teksEkstraksi,
                                 TingkatRisikoAnalisis tingkatRisiko, int totalSkorRisiko,
                                 String alasanPenilaian, String saranVerifikasi) {
        this.id = id;
        this.penggunaId = penggunaId;
        this.namaFile = namaFile;
        this.teksEkstraksi = teksEkstraksi;
        this.tingkatRisiko = tingkatRisiko;
        this.totalSkorRisiko = totalSkorRisiko;
        this.alasanPenilaian = alasanPenilaian;
        this.saranVerifikasi = saranVerifikasi;
        this.disclaimer = DEFAULT_DISCLAIMER;
        this.daftarDetail = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPenggunaId() {
        return penggunaId;
    }

    public void setPenggunaId(int penggunaId) {
        this.penggunaId = penggunaId;
    }

    public String getNamaFile() {
        return namaFile;
    }

    public void setNamaFile(String namaFile) {
        this.namaFile = namaFile;
    }

    public String getTeksEkstraksi() {
        return teksEkstraksi;
    }

    public void setTeksEkstraksi(String teksEkstraksi) {
        this.teksEkstraksi = teksEkstraksi;
    }

    public TingkatRisikoAnalisis getTingkatRisiko() {
        return tingkatRisiko;
    }

    public void setTingkatRisiko(TingkatRisikoAnalisis tingkatRisiko) {
        this.tingkatRisiko = tingkatRisiko;
    }

    public int getTotalSkorRisiko() {
        return totalSkorRisiko;
    }

    public void setTotalSkorRisiko(int totalSkorRisiko) {
        this.totalSkorRisiko = totalSkorRisiko;
    }

    public String getAlasanPenilaian() {
        return alasanPenilaian;
    }

    public void setAlasanPenilaian(String alasanPenilaian) {
        this.alasanPenilaian = alasanPenilaian;
    }

    public String getSaranVerifikasi() {
        return saranVerifikasi;
    }

    public void setSaranVerifikasi(String saranVerifikasi) {
        this.saranVerifikasi = saranVerifikasi;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<DetailAnalisis> getDaftarDetail() {
        return daftarDetail;
    }

    public void setDaftarDetail(List<DetailAnalisis> daftarDetail) {
        this.daftarDetail = daftarDetail != null ? daftarDetail : new ArrayList<>();
    }

    public void tambahDetail(DetailAnalisis detail) {
        this.daftarDetail.add(detail);
    }
}
