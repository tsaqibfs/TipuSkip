package com.tipuskip.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entitas Skenario Simulasi Penipuan Digital.
 */
public class Skenario {
    private int id;
    private String judul;
    private String deskripsi;
    private JenisSkenario jenis;
    private String kategori;
    private TingkatKesulitan tingkatKesulitan;
    private boolean statusAktif;
    private LocalDateTime createdAt;
    private List<LangkahSkenario> daftarLangkah;

    public Skenario() {
        this.daftarLangkah = new ArrayList<>();
        this.statusAktif = true;
        this.tingkatKesulitan = TingkatKesulitan.MUDAH;
        this.createdAt = LocalDateTime.now();
    }

    public Skenario(int id, String judul, String deskripsi, JenisSkenario jenis,
                    String kategori, TingkatKesulitan tingkatKesulitan, boolean statusAktif) {
        this.id = id;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.jenis = jenis;
        this.kategori = kategori;
        this.tingkatKesulitan = tingkatKesulitan;
        this.statusAktif = statusAktif;
        this.daftarLangkah = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public JenisSkenario getJenis() {
        return jenis;
    }

    public void setJenis(JenisSkenario jenis) {
        this.jenis = jenis;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public TingkatKesulitan getTingkatKesulitan() {
        return tingkatKesulitan;
    }

    public void setTingkatKesulitan(TingkatKesulitan tingkatKesulitan) {
        this.tingkatKesulitan = tingkatKesulitan;
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

    public List<LangkahSkenario> getDaftarLangkah() {
        return daftarLangkah;
    }

    public void setDaftarLangkah(List<LangkahSkenario> daftarLangkah) {
        this.daftarLangkah = daftarLangkah != null ? daftarLangkah : new ArrayList<>();
    }

    public void tambahLangkah(LangkahSkenario langkah) {
        this.daftarLangkah.add(langkah);
    }

    @Override
    public String toString() {
        return judul + " (" + jenis + " - " + tingkatKesulitan + ")";
    }
}
