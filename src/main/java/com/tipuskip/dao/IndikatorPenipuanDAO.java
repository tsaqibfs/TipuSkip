package com.tipuskip.dao;

import com.tipuskip.model.IndikatorPenipuan;

import java.util.List;

public interface IndikatorPenipuanDAO extends GenericDAO<IndikatorPenipuan, Integer> {
    List<IndikatorPenipuan> findAllActive();
    boolean toggleStatus(int id, boolean statusAktif);
}
