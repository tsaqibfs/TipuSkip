package com.tipuskip.model;

/**
 * Tingkat risiko dari pilihan respons pengguna pada langkah simulasi.
 */
public enum TingkatRisiko {
    AMAN("Aman", "#2ecc71"),
    WASPADA("Waspada", "#f39c12"),
    BAHAYA("Bahaya", "#e74c3c");

    private final String label;
    private final String hexColor;

    TingkatRisiko(String label, String hexColor) {
        this.label = label;
        this.hexColor = hexColor;
    }

    public String getLabel() {
        return label;
    }

    public String getHexColor() {
        return hexColor;
    }
}
