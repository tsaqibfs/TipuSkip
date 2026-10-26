package com.tipuskip.service;

import com.tipuskip.model.AnalisisTangkapanLayar;

import java.io.File;
import java.util.List;

/**
 * Kontrak Layanan Analisis Tangkapan Layar (OCR + Pencocokan Indikator + Risiko).
 * Penghubung antara UI Analisis (Anggota 1) dan Engine OCR/Analyzer (Anggota 3).
 */
public interface IAnalisisService {
    /**
     * Menganalisis gambar screenshot percakapan mencurigakan.
     * @param imageFile Berkas gambar yang diunggah pengguna.
     * @param penggunaId ID Pengguna yang melakukan analisis.
     * @return AnalisisTangkapanLayar lengkap dengan tingkat risiko, alasan, dan saran.
     * @throws Exception Jika OCR atau pemrosesan gagal.
     */
    AnalisisTangkapanLayar prosesAnalisis(File imageFile, int penggunaId) throws Exception;

    List<AnalisisTangkapanLayar> getRiwayatAnalisisPengguna(int penggunaId);
}
