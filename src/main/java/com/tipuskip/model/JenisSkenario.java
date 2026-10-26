package com.tipuskip.model;

/**
 * Jenis simulasi pencegahan penipuan digital.
 */
public enum JenisSkenario {
    PESAN("Simulasi Pesan"),
    TELEPON("Simulasi Telepon");

    private final String label;

    JenisSkenario(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
