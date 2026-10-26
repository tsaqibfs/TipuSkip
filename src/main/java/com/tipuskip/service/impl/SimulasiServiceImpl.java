package com.tipuskip.service.impl;

import com.tipuskip.dao.HasilSimulasiDAO;
import com.tipuskip.dao.SkenarioDAO;
import com.tipuskip.dao.impl.HasilSimulasiDAOImpl;
import com.tipuskip.dao.impl.SkenarioDAOImpl;
import com.tipuskip.model.HasilSimulasi;
import com.tipuskip.model.JenisSkenario;
import com.tipuskip.model.Skenario;
import com.tipuskip.service.ISimulasiService;
import com.tipuskip.simulation.Simulasi;
import com.tipuskip.simulation.SimulasiPesan;
import com.tipuskip.simulation.SimulasiTelepon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Implementasi ISimulasiService menggunakan JDBC DAO.
 */
public class SimulasiServiceImpl implements ISimulasiService {
    private static final Logger logger = LoggerFactory.getLogger(SimulasiServiceImpl.class);

    private final SkenarioDAO skenarioDAO;
    private final HasilSimulasiDAO hasilSimulasiDAO;

    public SimulasiServiceImpl() {
        this(new SkenarioDAOImpl(), new HasilSimulasiDAOImpl());
    }

    public SimulasiServiceImpl(SkenarioDAO skenarioDAO, HasilSimulasiDAO hasilSimulasiDAO) {
        this.skenarioDAO = skenarioDAO;
        this.hasilSimulasiDAO = hasilSimulasiDAO;
    }

    @Override
    public List<Skenario> getDaftarSkenarioAktif() {
        return skenarioDAO.findAllActive();
    }

    @Override
    public List<Skenario> getDaftarSkenarioBerdasarkanJenis(JenisSkenario jenis) {
        return skenarioDAO.findByJenis(jenis);
    }

    @Override
    public Optional<Skenario> getDetailSkenarioLengkap(int skenarioId) {
        return skenarioDAO.findWithFullStepsById(skenarioId);
    }

    @Override
    public Simulasi inisialisasiSimulasi(int skenarioId) {
        Optional<Skenario> opt = skenarioDAO.findWithFullStepsById(skenarioId);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Skenario ID " + skenarioId + " tidak ditemukan.");
        }

        Skenario skenario = opt.get();
        Simulasi simulasi;

        // Polimorfisme: Instansiasi turunan Simulasi sesuai jenis skenario
        if (skenario.getJenis() == JenisSkenario.TELEPON) {
            simulasi = new SimulasiTelepon(skenario);
        } else {
            simulasi = new SimulasiPesan(skenario);
        }

        simulasi.jalankan();
        logger.info("Simulasi berhasil diinisialisasi: {} ({})", skenario.getJudul(), skenario.getJenis());
        return simulasi;
    }

    @Override
    public boolean simpanHasilSimulasi(HasilSimulasi hasilSimulasi) {
        return hasilSimulasiDAO.save(hasilSimulasi);
    }

    @Override
    public List<HasilSimulasi> getRiwayatSimulasiPengguna(int penggunaId) {
        return hasilSimulasiDAO.findByPenggunaId(penggunaId);
    }
}
