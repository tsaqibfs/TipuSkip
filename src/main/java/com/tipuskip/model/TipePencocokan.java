package com.tipuskip.model;

/**
 * Metode pencocokan pola indikator penipuan (Keyword biasa atau Regex).
 */
public enum TipePencocokan {
    KEYWORD("Kata Kunci"),
    REGEX("Regular Expression");

    private final String label;

    TipePencocokan(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
