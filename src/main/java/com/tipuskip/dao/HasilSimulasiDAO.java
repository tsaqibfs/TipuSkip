package com.tipuskip.dao;

import com.tipuskip.model.HasilSimulasi;

import java.util.List;

public interface HasilSimulasiDAO extends GenericDAO<HasilSimulasi, Integer> {
    List<HasilSimulasi> findByPenggunaId(int penggunaId);
    List<HasilSimulasi> findBySkenarioId(int skenarioId);
}
