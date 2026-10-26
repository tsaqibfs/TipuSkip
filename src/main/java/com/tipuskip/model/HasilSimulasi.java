package com.tipuskip.model;

import java.time.LocalDateTime;

/**
 * Entitas Hasil Simulasi setelah pengguna menyelesaikan skenario.
 */
public class HasilSimulasi {
    private int id;
    private int penggunaId;
    private int skenarioId;
    private String judulSkenario; // Helper untuk tampilan riwayat
    private int totalSkor;
    private String statusEvaluasi; // LULUS_AMAN, PERLU_WASPADA, BERISIKO_TINGGI
    private int durasiDetik;
    private LocalDateTime waktuMulai;
    private LocalDateTime waktuSelesai;
    private LocalDateTime createdAt;

    public HasilSimulasi() {
        this.createdAt = LocalDateTime.now();
    }

    public HasilSimulasi(int id, int penggunaId, int skenarioId, int totalSkor,
                         String statusEvaluasi, int durasiDetik, LocalDateTime waktuMulai,
                         LocalDateTime waktuSelesai) {
        this.id = id;
        this.penggunaId = penggunaId;
        this.skenarioId = skenarioId;
        this.totalSkor = totalSkor;
        this.statusEvaluasi = statusEvaluasi;
        this.durasiDetik = durasiDetik;
        this.waktuMulai = waktuMulai;
        this.waktuSelesai = waktuSelesai;
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

    public int getSkenarioId() {
        return skenarioId;
    }

    public void setSkenarioId(int skenarioId) {
        this.skenarioId = skenarioId;
    }

    public String getJudulSkenario() {
        return judulSkenario;
    }

    public void setJudulSkenario(String judulSkenario) {
        this.judulSkenario = judulSkenario;
    }

    public int getTotalSkor() {
        return totalSkor;
    }

    public void setTotalSkor(int totalSkor) {
        this.totalSkor = totalSkor;
    }

    public String getStatusEvaluasi() {
        return statusEvaluasi;
    }

    public void setStatusEvaluasi(String statusEvaluasi) {
        this.statusEvaluasi = statusEvaluasi;
    }

    public int getDurasiDetik() {
        return durasiDetik;
    }

    public void setDurasiDetik(int durasiDetik) {
        this.durasiDetik = durasiDetik;
    }

    public LocalDateTime getWaktuMulai() {
        return waktuMulai;
    }

    public void setWaktuMulai(LocalDateTime waktuMulai) {
        this.waktuMulai = waktuMulai;
    }

    public LocalDateTime getWaktuSelesai() {
        return waktuSelesai;
    }

    public void setWaktuSelesai(LocalDateTime waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
