package com.tipuskip.model;

/**
 * Tingkat risiko hasil analisis tangkapan layar.
 */
public enum TingkatRisikoAnalisis {
    RENDAH("Risiko Rendah", "#27ae60", "Indikator penipuan minimal atau tidak terdeteksi secara dominan."),
    SEDANG("Risiko Sedang", "#e67e22", "Terdapat beberapa pola mencurigakan yang memerlukan verifikasi manual."),
    TINGGI("Risiko Tinggi", "#c0392b", "Ditemukan indikator kuat pola manipulasi / rekayasa penipuan digital.");

    private final String label;
    private final String color;
    private final String keterangan;

    TingkatRisikoAnalisis(String label, String color, String keterangan) {
        this.label = label;
        this.color = color;
        this.keterangan = keterangan;
    }

    public String getLabel() {
        return label;
    }

    public String getColor() {
        return color;
    }

    public String getKeterangan() {
        return keterangan;
    }
}
