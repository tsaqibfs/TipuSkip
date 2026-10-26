package com.tipuskip.model;

/**
 * Entitas Detail Analisis (mencatat indikator apa saja yang cocok dalam suatu analisis screenshot).
 */
public class DetailAnalisis {
    private int id;
    private int analisisId;
    private int indikatorId;
    private IndikatorPenipuan indikator; // Object reference untuk kemudahan akses
    private String teksDitemukan;
    private int bobotTerhitung;

    public DetailAnalisis() {
    }

    public DetailAnalisis(int id, int analisisId, int indikatorId, String teksDitemukan, int bobotTerhitung) {
        this.id = id;
        this.analisisId = analisisId;
        this.indikatorId = indikatorId;
        this.teksDitemukan = teksDitemukan;
        this.bobotTerhitung = bobotTerhitung;
    }

    public DetailAnalisis(int analisisId, IndikatorPenipuan indikator, String teksDitemukan) {
        this.analisisId = analisisId;
        this.indikator = indikator;
        if (indikator != null) {
            this.indikatorId = indikator.getId();
            this.bobotTerhitung = indikator.getBobotRisiko();
        }
        this.teksDitemukan = teksDitemukan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnalisisId() {
        return analisisId;
    }

    public void setAnalisisId(int analisisId) {
        this.analisisId = analisisId;
    }

    public int getIndikatorId() {
        return indikatorId;
    }

    public void setIndikatorId(int indikatorId) {
        this.indikatorId = indikatorId;
    }

    public IndikatorPenipuan getIndikator() {
        return indikator;
    }

    public void setIndikator(IndikatorPenipuan indikator) {
        this.indikator = indikator;
        if (indikator != null) {
            this.indikatorId = indikator.getId();
            this.bobotTerhitung = indikator.getBobotRisiko();
        }
    }

    public String getTeksDitemukan() {
        return teksDitemukan;
    }

    public void setTeksDitemukan(String teksDitemukan) {
        this.teksDitemukan = teksDitemukan;
    }

    public int getBobotTerhitung() {
        return bobotTerhitung;
    }

    public void setBobotTerhitung(int bobotTerhitung) {
        this.bobotTerhitung = bobotTerhitung;
    }
}
