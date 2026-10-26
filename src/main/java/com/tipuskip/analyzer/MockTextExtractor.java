package com.tipuskip.analyzer;

import java.io.File;

/**
 * Mock OCR Extractor untuk pengujian dan development offline.
 * Menghindari error crash bagi anggota tim yang belum mengunduh binary Tesseract.
 */
public class MockTextExtractor implements ITextExtractor {
    @Override
    public String extractText(File imageFile) {
        if (imageFile == null) {
            return "";
        }
        String fileName = imageFile.getName().toLowerCase();
        // Simulasi ekstraksi teks berdasarkan nama file gambar
        if (fileName.contains("otp")) {
            return "Halo, ini dari pihak bank. Tolong segera kirimkan kode verifikasi OTP 6 digit yang masuk ke SMS Anda sekarang juga sebelum rekening dibekukan!";
        } else if (fileName.contains("loker") || fileName.contains("job")) {
            return "Selamat Kak! Anda diterima kerja freelance Like YouTube. Klik link ini: https://s.id/tugas-komisi dan lakukan deposit awal Rp200.000 untuk bonus harian Rp1.000.000.";
        } else if (fileName.contains("paket") || fileName.contains("apk")) {
            return "Paket Anda tertahan di gudang J&T. Silakan unduh dan pasang berkas Surat_JNT_Ekspedisi.apk ini sekarang juga untuk melacak status.";
        } else {
            return "Halo Kak, mohon konfirmasi transfer deposit ke rekening berikut dalam 5 menit. Jangan bagikan kode rahasia atau OTP kepada siapapun.";
        }
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
