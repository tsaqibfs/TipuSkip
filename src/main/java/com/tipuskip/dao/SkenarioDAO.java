package com.tipuskip.dao;

import com.tipuskip.model.JenisSkenario;
import com.tipuskip.model.Skenario;

import java.util.List;
import java.util.Optional;

public interface SkenarioDAO extends GenericDAO<Skenario, Integer> {
    List<Skenario> findByJenis(JenisSkenario jenis);
    List<Skenario> findByKategori(String kategori);
    Optional<Skenario> findWithFullStepsById(int id);
    List<Skenario> findAllActive();
}
