package com.tipuskip.dao;

import com.tipuskip.model.PilihanRespons;

import java.util.List;

/**
 * Interface DAO untuk entitas PilihanRespons.
 * Bagian Tanggung Jawab Anggota 2.
 */
public interface PilihanResponsDAO extends GenericDAO<PilihanRespons, Integer> {
    List<PilihanRespons> findByLangkahId(int langkahId);
    boolean deleteByLangkahId(int langkahId);
}
