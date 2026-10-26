package com.tipuskip.service.impl;

import com.tipuskip.dao.IndikatorPenipuanDAO;
import com.tipuskip.dao.SkenarioDAO;
import com.tipuskip.dao.impl.IndikatorPenipuanDAOImpl;
import com.tipuskip.dao.impl.SkenarioDAOImpl;
import com.tipuskip.model.IndikatorPenipuan;
import com.tipuskip.model.Skenario;
import com.tipuskip.service.IAdminService;

import java.util.List;

/**
 * Implementasi IAdminService untuk mengelola Skenario dan Indikator Penipuan.
 * Bagian Tanggung Jawab Anggota 2.
 */
public class AdminServiceImpl implements IAdminService {
    private final SkenarioDAO skenarioDAO;
    private final IndikatorPenipuanDAO indikatorDAO;

    public AdminServiceImpl() {
        this(new SkenarioDAOImpl(), new IndikatorPenipuanDAOImpl());
    }

    public AdminServiceImpl(SkenarioDAO skenarioDAO, IndikatorPenipuanDAO indikatorDAO) {
        this.skenarioDAO = skenarioDAO;
        this.indikatorDAO = indikatorDAO;
    }

    @Override
    public List<Skenario> getAllSkenario() {
        return skenarioDAO.findAll();
    }

    @Override
    public boolean tambahSkenario(Skenario skenario) {
        return skenarioDAO.save(skenario);
    }

    @Override
    public boolean ubahSkenario(Skenario skenario) {
        return skenarioDAO.update(skenario);
    }

    @Override
    public boolean hapusSkenario(int skenarioId) {
        return skenarioDAO.deleteById(skenarioId);
    }

    @Override
    public List<IndikatorPenipuan> getAllIndikator() {
        return indikatorDAO.findAll();
    }

    @Override
    public boolean tambahIndikator(IndikatorPenipuan indikator) {
        return indikatorDAO.save(indikator);
    }

    @Override
    public boolean ubahIndikator(IndikatorPenipuan indikator) {
        return indikatorDAO.update(indikator);
    }

    @Override
    public boolean hapusIndikator(int indikatorId) {
        return indikatorDAO.deleteById(indikatorId);
    }

    @Override
    public boolean ubahStatusAktifIndikator(int indikatorId, boolean statusAktif) {
        return indikatorDAO.toggleStatus(indikatorId, statusAktif);
    }
}
