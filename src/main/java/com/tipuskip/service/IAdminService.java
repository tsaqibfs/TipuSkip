package com.tipuskip.service;

import com.tipuskip.model.IndikatorPenipuan;
import com.tipuskip.model.Skenario;

import java.util.List;

/**
 * Kontrak Layanan Pengelolaan Admin (Skenario & Indikator Penipuan).
 * Bagian Tanggung Jawab Anggota 2.
 */
public interface IAdminService {
    // Kelola Skenario (FR-12)
    List<Skenario> getAllSkenario();
    boolean tambahSkenario(Skenario skenario);
    boolean ubahSkenario(Skenario skenario);
    boolean hapusSkenario(int skenarioId);
    boolean ubahStatusAktifSkenario(int skenarioId, boolean statusAktif);

    // Kelola Indikator (FR-13)
    List<IndikatorPenipuan> getAllIndikator();
    boolean tambahIndikator(IndikatorPenipuan indikator);
    boolean ubahIndikator(IndikatorPenipuan indikator);
    boolean hapusIndikator(int indikatorId);
    boolean ubahStatusAktifIndikator(int indikatorId, boolean statusAktif);
}
