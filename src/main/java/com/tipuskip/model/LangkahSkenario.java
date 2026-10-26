package com.tipuskip.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entitas Langkah dalam suatu Skenario Simulasi.
 */
public class LangkahSkenario {
    private int id;
    private int skenarioId;
    private int nomorLangkah;
    private String namaPengirim;
    private String pesanPengirim;
    private String tipeKonten; // e.g., "TEXT", "IMAGE", "AUDIO_CALL"
    private List<PilihanRespons> daftarPilihanRespons;

    public LangkahSkenario() {
        this.daftarPilihanRespons = new ArrayList<>();
        this.tipeKonten = "TEXT";
    }

    public LangkahSkenario(int id, int skenarioId, int nomorLangkah, String namaPengirim,
                           String pesanPengirim, String tipeKonten) {
        this.id = id;
        this.skenarioId = skenarioId;
        this.nomorLangkah = nomorLangkah;
        this.namaPengirim = namaPengirim;
        this.pesanPengirim = pesanPengirim;
        this.tipeKonten = tipeKonten != null ? tipeKonten : "TEXT";
        this.daftarPilihanRespons = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSkenarioId() {
        return skenarioId;
    }

    public void setSkenarioId(int skenarioId) {
        this.skenarioId = skenarioId;
    }

    public int getNomorLangkah() {
        return nomorLangkah;
    }

    public void setNomorLangkah(int nomorLangkah) {
        this.nomorLangkah = nomorLangkah;
    }

    public String getNamaPengirim() {
        return namaPengirim;
    }

    public void setNamaPengirim(String namaPengirim) {
        this.namaPengirim = namaPengirim;
    }

    public String getPesanPengirim() {
        return pesanPengirim;
    }

    public void setPesanPengirim(String pesanPengirim) {
        this.pesanPengirim = pesanPengirim;
    }

    public String getTipeKonten() {
        return tipeKonten;
    }

    public void setTipeKonten(String tipeKonten) {
        this.tipeKonten = tipeKonten;
    }

    public List<PilihanRespons> getDaftarPilihanRespons() {
        return daftarPilihanRespons;
    }

    public void setDaftarPilihanRespons(List<PilihanRespons> daftarPilihanRespons) {
        this.daftarPilihanRespons = daftarPilihanRespons != null ? daftarPilihanRespons : new ArrayList<>();
    }

    public void tambahPilihanRespons(PilihanRespons respons) {
        this.daftarPilihanRespons.add(respons);
    }
}
