package com.tipuskip.simulation;

import com.tipuskip.model.HasilSimulasi;
import com.tipuskip.model.LangkahSkenario;
import com.tipuskip.model.PilihanRespons;
import com.tipuskip.model.Skenario;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Kelas Abstrak Simulasi (Penerapan Prinsip PBO Abstraksi).
 * Mendefinisikan kerangka kerja dan kontrak umum untuk semua jenis simulasi penipuan.
 */
public abstract class Simulasi {
    protected Skenario skenario;
    protected int skorTerkumpul;
    protected int indeksLangkahSaatIni;
    protected List<PilihanRespons> riwayatResponsPengguna;
    protected LocalDateTime waktuMulai;
    protected LocalDateTime waktuSelesai;
    protected boolean selesai;

    public Simulasi(Skenario skenario) {
        if (skenario == null) {
            throw new IllegalArgumentException("Skenario tidak boleh null.");
        }
        this.skenario = skenario;
        this.skorTerkumpul = 0;
        this.indeksLangkahSaatIni = 0;
        this.riwayatResponsPengguna = new ArrayList<>();
        this.selesai = false;
    }

    /**
     * Memulai jalannya simulasi.
     * Masing-masing turunan mengimplementasikan logika inisialisasi yang spesifik (Polimorfisme).
     */
    public abstract void jalankan();

    /**
     * Memproses pilihan respons pengguna pada langkah tertentu.
     * @param respons Pilihan respons yang dipilih pengguna.
     * @return true jika simulasi berlanjut, false jika skenario selesai.
     */
    public abstract boolean prosesRespons(PilihanRespons respons);

    /**
     * Menghitung total skor akhir dari akumulasi respons pengguna.
     */
    public abstract int hitungSkor();

    /**
     * Memberikan status evaluasi akhir kelulusan (LULUS_AMAN, WASPADA, BERISIKO).
     */
    public abstract String evaluasiHasil();

    /**
     * Menghasilkan teks ringkasan edukasi untuk pengguna setelah simulasi selesai.
     */
    public abstract String dapatkanRingkasanPembelajaran();

    // ==========================================
    // Concrete Common Methods (Reusability OOP)
    // ==========================================

    public LangkahSkenario getLangkahSaatIni() {
        List<LangkahSkenario> langkahList = skenario.getDaftarLangkah();
        if (langkahList == null || indeksLangkahSaatIni >= langkahList.size()) {
            return null;
        }
        return langkahList.get(indeksLangkahSaatIni);
    }

    public boolean isSelesai() {
        return selesai;
    }

    public int getSkorTerkumpul() {
        return skorTerkumpul;
    }

    public int getDurasiDetik() {
        if (waktuMulai == null) return 0;
        LocalDateTime end = waktuSelesai != null ? waktuSelesai : LocalDateTime.now();
        return (int) Duration.between(waktuMulai, end).getSeconds();
    }

    public HasilSimulasi buatHasilSimulasi(int penggunaId) {
        HasilSimulasi hasil = new HasilSimulasi();
        hasil.setPenggunaId(penggunaId);
        hasil.setSkenarioId(skenario.getId());
        hasil.setJudulSkenario(skenario.getJudul());
        hasil.setTotalSkor(hitungSkor());
        hasil.setStatusEvaluasi(evaluasiHasil());
        hasil.setDurasiDetik(getDurasiDetik());
        hasil.setWaktuMulai(waktuMulai);
        hasil.setWaktuSelesai(waktuSelesai != null ? waktuSelesai : LocalDateTime.now());
        return hasil;
    }

    public Skenario getSkenario() {
        return skenario;
    }

    public List<PilihanRespons> getRiwayatResponsPengguna() {
        return riwayatResponsPengguna;
    }
}
