package com.tipuskip.service;

import com.tipuskip.model.HasilSimulasi;
import com.tipuskip.model.JenisSkenario;
import com.tipuskip.model.Skenario;
import com.tipuskip.simulation.Simulasi;

import java.util.List;
import java.util.Optional;

/**
 * Kontrak Layanan Simulasi Pesan & Telepon.
 * Penghubung antara UI Controller (Anggota 1) dan Database/Model (Anggota 2).
 */
public interface ISimulasiService {
    List<Skenario> getDaftarSkenarioAktif();
    List<Skenario> getDaftarSkenarioBerdasarkanJenis(JenisSkenario jenis);
    Optional<Skenario> getDetailSkenarioLengkap(int skenarioId);
    Simulasi inisialisasiSimulasi(int skenarioId);
    boolean simpanHasilSimulasi(HasilSimulasi hasilSimulasi);
    List<HasilSimulasi> getRiwayatSimulasiPengguna(int penggunaId);
}
