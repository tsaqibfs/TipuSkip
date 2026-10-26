package com.tipuskip.dao;

import com.tipuskip.model.AnalisisTangkapanLayar;

import java.util.List;
import java.util.Optional;

public interface AnalisisTangkapanLayarDAO extends GenericDAO<AnalisisTangkapanLayar, Integer> {
    List<AnalisisTangkapanLayar> findByPenggunaId(int penggunaId);
    Optional<AnalisisTangkapanLayar> findWithDetailsById(int id);
}
