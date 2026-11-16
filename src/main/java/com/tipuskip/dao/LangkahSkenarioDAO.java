package com.tipuskip.dao;

import com.tipuskip.model.LangkahSkenario;

import java.util.List;

/**
 * Interface DAO untuk entitas LangkahSkenario.
 * Bagian Tanggung Jawab Anggota 2.
 */
public interface LangkahSkenarioDAO extends GenericDAO<LangkahSkenario, Integer> {
    List<LangkahSkenario> findBySkenarioId(int skenarioId);
    boolean deleteBySkenarioId(int skenarioId);
}
