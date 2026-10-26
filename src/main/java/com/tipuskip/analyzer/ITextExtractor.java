package com.tipuskip.analyzer;

import java.io.File;

/**
 * Interface Ekstraksi Teks dari Gambar Tangkapan Layar (Abstraksi OCR).
 * Memungkinkan implementasi Tesseract OCR nyata maupun Mock untuk pengujian.
 * Bagian Tanggung Jawab Anggota 3.
 */
public interface ITextExtractor {
    /**
     * Mengekstraksi teks dari berkas gambar.
     * @param imageFile Berkas gambar screenshot percakapan.
     * @return Teks hasil ekstraksi OCR.
     * @throws Exception Jika file tidak valid atau terjadi kegagalan proses.
     */
    String extractText(File imageFile) throws Exception;

    /**
     * Memeriksa apakah engine OCR siap digunakan.
     */
    boolean isAvailable();
}
