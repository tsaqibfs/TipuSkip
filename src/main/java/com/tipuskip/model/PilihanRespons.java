package com.tipuskip.model;

/**
 * Entitas Pilihan Respons Pengguna pada suatu Langkah Skenario.
 */
public class PilihanRespons {
    private int id;
    private int langkahId;
    private String teksRespons;
    private TingkatRisiko tingkatRisiko;
    private int poinSkor;
    private String umpanBalik;
    private String konsekuensi;
    private Integer langkahBerikutnya; // Nullable (jika null, skenario selesai)

    public PilihanRespons() {
        this.tingkatRisiko = TingkatRisiko.AMAN;
    }

    public PilihanRespons(int id, int langkahId, String teksRespons, TingkatRisiko tingkatRisiko,
                          int poinSkor, String umpanBalik, String konsekuensi, Integer langkahBerikutnya) {
        this.id = id;
        this.langkahId = langkahId;
        this.teksRespons = teksRespons;
        this.tingkatRisiko = tingkatRisiko;
        this.poinSkor = poinSkor;
        this.umpanBalik = umpanBalik;
        this.konsekuensi = konsekuensi;
        this.langkahBerikutnya = langkahBerikutnya;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLangkahId() {
        return langkahId;
    }

    public void setLangkahId(int langkahId) {
        this.langkahId = langkahId;
    }

    public String getTeksRespons() {
        return teksRespons;
    }

    public void setTeksRespons(String teksRespons) {
        this.teksRespons = teksRespons;
    }

    public TingkatRisiko getTingkatRisiko() {
        return tingkatRisiko;
    }

    public void setTingkatRisiko(TingkatRisiko tingkatRisiko) {
        this.tingkatRisiko = tingkatRisiko;
    }

    public int getPoinSkor() {
        return poinSkor;
    }

    public void setPoinSkor(int poinSkor) {
        this.poinSkor = poinSkor;
    }

    public String getUmpanBalik() {
        return umpanBalik;
    }

    public void setUmpanBalik(String umpanBalik) {
        this.umpanBalik = umpanBalik;
    }

    public String getKonsekuensi() {
        return konsekuensi;
    }

    public void setKonsekuensi(String konsekuensi) {
        this.konsekuensi = konsekuensi;
    }

    public Integer getLangkahBerikutnya() {
        return langkahBerikutnya;
    }

    public void setLangkahBerikutnya(Integer langkahBerikutnya) {
        this.langkahBerikutnya = langkahBerikutnya;
    }

    @Override
    public String toString() {
        return teksRespons + " [" + tingkatRisiko + "]";
    }
}
