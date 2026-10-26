package com.tipuskip.service;

import com.tipuskip.model.IndikatorPenipuan;
import com.tipuskip.model.Skenario;

import java.util.List;

/**
 * Kontrak Layanan Pengelolaan Admin (Skenario & Indikator Penipuan).
 * Bagian Tanggung Jawab Anggota 2.
 */
public interface IAdminService {
    // Kelola Skenario
    List<Skenario> getAllSkenario();
    boolean tambahSkenario(Skenario skenario);
    boolean ubahSkenario(Skenario skenario);
    boolean hapusSkenario(int skenarioId);

    // Kelola Indikator
    List<IndikatorPenipuan> getAllIndikator();
    boolean tambahIndikator(IndikatorPenipuan indikator);
    boolean ubahIndikator(IndikatorPenipuan indikator);
    boolean hapusIndikator(int indikatorId);
    boolean ubahStatusAktifIndikator(int indikatorId, boolean statusAktif);
}
