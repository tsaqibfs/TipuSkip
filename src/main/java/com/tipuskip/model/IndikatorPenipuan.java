package com.tipuskip.model;

import java.time.LocalDateTime;

/**
 * Entitas Indikator Penipuan (Pola / Regex untuk pencocokan analisis teks OCR).
 */
public class IndikatorPenipuan {
    private int id;
    private String namaIndikator;
    private String polaPencocokan;
    private TipePencocokan tipePencocokan;
    private int bobotRisiko;
    private String deskripsi;
    private String saranVerifikasi;
    private boolean statusAktif;
    private LocalDateTime createdAt;

    public IndikatorPenipuan() {
        this.tipePencocokan = TipePencocokan.KEYWORD;
        this.bobotRisiko = 10;
        this.statusAktif = true;
        this.createdAt = LocalDateTime.now();
    }

    public IndikatorPenipuan(int id, String namaIndikator, String polaPencocokan,
                             TipePencocokan tipePencocokan, int bobotRisiko,
                             String deskripsi, String saranVerifikasi, boolean statusAktif) {
        this.id = id;
        this.namaIndikator = namaIndikator;
        this.polaPencocokan = polaPencocokan;
        this.tipePencocokan = tipePencocokan != null ? tipePencocokan : TipePencocokan.KEYWORD;
        this.bobotRisiko = bobotRisiko;
        this.deskripsi = deskripsi;
        this.saranVerifikasi = saranVerifikasi;
        this.statusAktif = statusAktif;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaIndikator() {
        return namaIndikator;
    }

    public void setNamaIndikator(String namaIndikator) {
        this.namaIndikator = namaIndikator;
    }

    public String getPolaPencocokan() {
        return polaPencocokan;
    }

    public void setPolaPencocokan(String polaPencocokan) {
        this.polaPencocokan = polaPencocokan;
    }

    public TipePencocokan getTipePencocokan() {
        return tipePencocokan;
    }

    public void setTipePencocokan(TipePencocokan tipePencocokan) {
        this.tipePencocokan = tipePencocokan;
    }

    public int getBobotRisiko() {
        return bobotRisiko;
    }

    public void setBobotRisiko(int bobotRisiko) {
        this.bobotRisiko = bobotRisiko;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getSaranVerifikasi() {
        return saranVerifikasi;
    }

    public void setSaranVerifikasi(String saranVerifikasi) {
        this.saranVerifikasi = saranVerifikasi;
    }

    public boolean isStatusAktif() {
        return statusAktif;
    }

    public void setStatusAktif(boolean statusAktif) {
        this.statusAktif = statusAktif;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return namaIndikator + " (Bobot: " + bobotRisiko + ")";
    }
}
